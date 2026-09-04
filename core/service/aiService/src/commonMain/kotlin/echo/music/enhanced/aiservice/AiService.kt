package echo.music.enhanced.aiservice

import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatResponseFormat
import com.aallam.openai.api.chat.JsonSchema
import com.aallam.openai.api.chat.chatCompletionRequest
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import com.aallam.openai.client.OpenAIHost
import com.aallam.openai.client.OpenAIHost.Companion.Gemini
import echo.music.enhanced.domain.data.model.metadata.Line
import echo.music.enhanced.domain.data.model.metadata.Lyrics
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import kotlinx.serialization.json.putJsonObject

class AiService(
    private val aiHost: AIHost = AIHost.GEMINI,
    private val apiKey: String,
    private val customModelId: String? = null,
    private val customBaseUrl: String? = null,
    private val customHeaders: Map<String, String>? = null,
) {
    private val json =
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            explicitNulls = false
        }
    private val openAI: OpenAI by lazy {
        when (aiHost) {
            AIHost.GEMINI -> {
                OpenAI(host = Gemini, token = apiKey)
            }

            AIHost.OPENAI -> {
                OpenAI(token = apiKey)
            }

            AIHost.CUSTOM_OPENAI -> {
                val baseUrl = customBaseUrl ?: "https://api.openai.com/v1/"
                val config =
                    OpenAIConfig(
                        token = apiKey,
                        host = OpenAIHost(baseUrl = baseUrl),
                        headers = customHeaders ?: emptyMap(),
                    )
                OpenAI(config)
            }

            AIHost.OPENROUTER -> {
                val baseUrl = customBaseUrl ?: "https://openrouter.ai/api/v1/"
                val config =
                    OpenAIConfig(
                        token = apiKey,
                        host = OpenAIHost(baseUrl = baseUrl),
                        headers = customHeaders ?: emptyMap(),
                    )
                OpenAI(config)
            }
        }
    }

    private val model by lazy {
        if (!customModelId.isNullOrEmpty()) {
            ModelId(customModelId)
        } else {
            when (aiHost) {
                AIHost.GEMINI -> ModelId("gemini-2.0-flash")
                AIHost.OPENAI -> ModelId("gpt-4o")
                AIHost.CUSTOM_OPENAI -> ModelId("gpt-4o")
                AIHost.OPENROUTER -> ModelId("google/gemini-2.5-flash-lite")
            }
        }
    }

    suspend fun translateLyrics(
        inputLyrics: Lyrics,
        targetLanguage: String,
    ): Lyrics {
        val lines = inputLyrics.lines ?: throw IllegalStateException("No lyrics lines to translate")

        // Build key-value map: index -> words (only non-empty lines)
        val indexToWords = mutableMapOf<String, String>()
        lines.forEachIndexed { index, line ->
            val words = line.words.trim()
            if (words.isNotEmpty() && words != "♫") {
                indexToWords[index.toString()] = words
            }
        }

        if (indexToWords.isEmpty()) {
            throw IllegalStateException("No translatable lyrics lines found")
        }

        val inputJson = json.encodeToString(MapSerializer(String.serializer(), String.serializer()), indexToWords)

        val request =
            chatCompletionRequest {
                this.model = this@AiService.model
                responseFormat = ChatResponseFormat.jsonSchema(aiResponseJsonSchema)
                messages {
                    system {
                        content =
                            "You are a song lyrics translation assistant.\n" +
                            "\n" +
                            "TASK:\n" +
                            "- You will receive a JSON object where keys are line indices and values are lyrics text.\n" +
                            "- FIRST, detect the dominant language of the input lyrics.\n" +
                            "- If the detected language is the SAME as the target language code, return an EMPTY \"translations\" object. Do NOT translate. Do NOT paraphrase.\n" +
                            "- Otherwise, translate ONLY the values to the target language.\n" +
                            "- When translating: keep ALL keys exactly the same, output MUST have the EXACT same number of entries as the input, do NOT merge/split/add/remove any entries, and preserve the song's meaning, tone, and emotion.\n" +
                            "\n" +
                            "OUTPUT:\n" +
                            "- A JSON object with the \"translations\" field containing the same keys mapped to translated values (or an empty object when the input is already in the target language)."
                    }
                    user {
                        content {
                            text("Target language: $targetLanguage")
                        }
                        content {
                            text("Input lyrics: $inputJson")
                        }
                    }
                }
            }
        val completion: ChatCompletion = openAI.chatCompletion(request)
        val jsonContent =
            completion.choices
                .firstOrNull()
                ?.message
                ?.content ?: throw IllegalStateException("No response from AI")
        val jsonData =
            Regex(
                "```json\\s*([\\s\\S]*?)```",
            ).find(jsonContent)
                ?.groups
                ?.firstOrNull()
                ?.value ?: jsonContent
        val cleanedJson = jsonData.replace("```json", "").replace("```", "")
        val translationResponse = json.decodeFromString<TranslationResponse>(cleanedJson)
        val translatedMap = translationResponse.translations
        if (translatedMap.isEmpty()) {
            throw IllegalStateException(
                "Input lyrics are already in the target language ($targetLanguage). Translation aborted.",
            )
        }

        // Map translated text back to original lines, preserving all timestamps
        val translatedLines = lines.mapIndexed { index, originalLine ->
            val translatedWords = translatedMap[index.toString()]
            if (translatedWords != null) {
                Line(
                    startTimeMs = originalLine.startTimeMs,
                    endTimeMs = originalLine.endTimeMs,
                    words = translatedWords,
                    syllables = null,
                )
            } else {
                // Non-translatable line (empty or ♫): keep original
                Line(
                    startTimeMs = originalLine.startTimeMs,
                    endTimeMs = originalLine.endTimeMs,
                    words = originalLine.words,
                    syllables = originalLine.syllables,
                )
            }
        }

        return Lyrics(
            error = false,
            lines = translatedLines,
            syncType = inputLyrics.syncType,
        )
    }

    private fun extractJsonContent(completion: ChatCompletion): String {
        val jsonContent =
            completion.choices
                .firstOrNull()
                ?.message
                ?.content ?: throw IllegalStateException("No response from AI")
        val jsonData =
            Regex(
                "```json\\s*([\\s\\S]*?)```",
            ).find(jsonContent)
                ?.groups
                ?.firstOrNull()
                ?.value ?: jsonContent
        return jsonData.replace("```json", "").replace("```", "")
    }

    suspend fun generatePlaylist(
        prompt: String,
        songCount: Int,
    ): AiPlaylistSuggestionResponse {
        val request =
            chatCompletionRequest {
                this.model = this@AiService.model
                responseFormat = ChatResponseFormat.jsonSchema(playlistSuggestionJsonSchema)
                messages {
                    system {
                        content =
                            "You are a music playlist generation assistant.\n" +
                            "\n" +
                            "TASK:\n" +
                            "- The user will describe a playlist they want.\n" +
                            "- Generate EXACTLY $songCount real songs that match the request.\n" +
                            "- Verify each song's release year, movie/album, and artist are accurate before including it — do not guess.\n" +
                            "- If the request mentions a decade, era, or specific artist, match it literally, not loosely.\n" +
                            "\n" +
                            "OUTPUT:\n" +
                            "- A JSON object with a \"name\" field (a short, fitting playlist name) and a \"songs\" " +
                            "field: a list of exactly $songCount objects, each with \"title\" and \"artist\"."
                    }
                    user {
                        content { text(prompt) }
                    }
                }
            }
        val completion: ChatCompletion = openAI.chatCompletion(request)
        return json.decodeFromString<AiPlaylistSuggestionResponse>(extractJsonContent(completion))
    }

    suspend fun modifyPlaylist(
        existingSongs: List<Pair<String, String>>,
        prompt: String,
    ): AiPlaylistModificationResponse {
        val songsList = existingSongs.joinToString("\n") { (id, label) -> "- $id: $label" }
        val request =
            chatCompletionRequest {
                this.model = this@AiService.model
                responseFormat = ChatResponseFormat.jsonSchema(playlistModificationJsonSchema)
                messages {
                    system {
                        content =
                            "You are a music playlist editing assistant.\n" +
                            "\n" +
                            "TASK:\n" +
                            "- You will receive the current songs in a playlist (id: \"title by artist\") and a " +
                            "user instruction describing how to change it.\n" +
                            "- Decide which existing songs to remove (by id) and which new real songs to add.\n" +
                            "- Only remove songs the instruction actually implies should go; only add songs that " +
                            "genuinely fit.\n" +
                            "\n" +
                            "OUTPUT:\n" +
                            "- A JSON object with \"removeIds\" (a list of ids to remove, may be empty) and " +
                            "\"additions\" (a list of objects with \"title\" and \"artist\" to add, may be empty)."
                    }
                    user {
                        content { text("Current playlist:\n$songsList") }
                        content { text("Instruction: $prompt") }
                    }
                }
            }
        val completion: ChatCompletion = openAI.chatCompletion(request)
        return json.decodeFromString<AiPlaylistModificationResponse>(extractJsonContent(completion))
    }

    suspend fun generateRecommendations(
        librarySongs: List<String>,
        songCount: Int,
    ): AiPlaylistSuggestionResponse {
        val libraryList = librarySongs.joinToString("\n") { "- $it" }
        val request =
            chatCompletionRequest {
                this.model = this@AiService.model
                responseFormat = ChatResponseFormat.jsonSchema(playlistSuggestionJsonSchema)
                messages {
                    system {
                        content =
                            "You are a music recommendation assistant.\n" +
                            "\n" +
                            "TASK:\n" +
                            "- You will receive a list of songs from the user's library.\n" +
                            "- Recommend EXACTLY $songCount real songs the user would likely enjoy, based on the " +
                            "styles/artists/genres in their library.\n" +
                            "- Do NOT recommend songs already in the list.\n" +
                            "\n" +
                            "OUTPUT:\n" +
                            "- A JSON object with a \"name\" field (always \"Recommended by AI\") and a \"songs\" " +
                            "field: a list of exactly $songCount objects, each with \"title\" and \"artist\"."
                    }
                    user {
                        content { text("Library:\n$libraryList") }
                    }
                }
            }
        val completion: ChatCompletion = openAI.chatCompletion(request)
        return json.decodeFromString<AiPlaylistSuggestionResponse>(extractJsonContent(completion))
    }

    companion object {
        private val translationJsonSchema: JsonObject =
            buildJsonObject {
                put("type", "object")
                putJsonObject("properties") {
                    putJsonObject("translations") {
                        put("type", "object")
                        putJsonObject("additionalProperties") {
                            put("type", "string")
                        }
                    }
                }
                putJsonArray("required") {
                    add("translations")
                }
            }
        private val aiResponseJsonSchema =
            JsonSchema(
                name = "ai_translation_schema",
                schema = translationJsonSchema,
                strict = false,
            )

        private val playlistSongSchema: JsonObject =
            buildJsonObject {
                put("type", "object")
                putJsonObject("properties") {
                    putJsonObject("title") { put("type", "string") }
                    putJsonObject("artist") { put("type", "string") }
                }
                putJsonArray("required") {
                    add("title")
                    add("artist")
                }
            }
        private val playlistSuggestionSchema: JsonObject =
            buildJsonObject {
                put("type", "object")
                putJsonObject("properties") {
                    putJsonObject("name") { put("type", "string") }
                    putJsonObject("songs") {
                        put("type", "array")
                        put("items", playlistSongSchema)
                    }
                }
                putJsonArray("required") {
                    add("name")
                    add("songs")
                }
            }
        private val playlistSuggestionJsonSchema =
            JsonSchema(
                name = "ai_playlist_suggestion_schema",
                schema = playlistSuggestionSchema,
                strict = false,
            )
        private val playlistModificationSchema: JsonObject =
            buildJsonObject {
                put("type", "object")
                putJsonObject("properties") {
                    putJsonObject("removeIds") {
                        put("type", "array")
                        putJsonObject("items") { put("type", "string") }
                    }
                    putJsonObject("additions") {
                        put("type", "array")
                        put("items", playlistSongSchema)
                    }
                }
                putJsonArray("required") {
                    add("removeIds")
                    add("additions")
                }
            }
        private val playlistModificationJsonSchema =
            JsonSchema(
                name = "ai_playlist_modification_schema",
                schema = playlistModificationSchema,
                strict = false,
            )
    }
}

@kotlinx.serialization.Serializable
data class TranslationResponse(
    val translations: Map<String, String> = emptyMap(),
)

enum class AIHost {
    GEMINI,
    OPENAI,
    CUSTOM_OPENAI,
    OPENROUTER,
}

@kotlinx.serialization.Serializable
data class AiPlaylistSongSuggestion(
    val title: String,
    val artist: String,
)

@kotlinx.serialization.Serializable
data class AiPlaylistSuggestionResponse(
    val name: String,
    val songs: List<AiPlaylistSongSuggestion> = emptyList(),
)

@kotlinx.serialization.Serializable
data class AiPlaylistModificationResponse(
    val removeIds: List<String> = emptyList(),
    val additions: List<AiPlaylistSongSuggestion> = emptyList(),
)