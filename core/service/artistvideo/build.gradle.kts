plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.android.lint)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    android {
        namespace = "echo.music.enhanced.artistvideo"
        compileSdk = 37
        minSdk = 26
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.okhttp)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.ktor.client.encoding)

                implementation(libs.compose.ui)
                implementation(libs.runtime)
                implementation(libs.foundation)
                implementation(libs.animation)

                // ExoPlayer, to play the artist background video.
                implementation(libs.media3.exoplayer)
                implementation(libs.media3.ui)
                implementation(libs.media3.datasource.okhttp)
            }
        }
    }
}
