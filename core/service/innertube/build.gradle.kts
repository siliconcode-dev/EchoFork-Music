plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.android.lint)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    android {
        namespace = "echo.music.enhanced.innertube"
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
                implementation(libs.brotli.dec)
                // libs.newpipe.extractor's pinned tag isn't actually resolvable from any
                // configured repository (jitpack 404s on it) — kotlinYtmusicScraper already
                // solved this by depending on the maxrave-dev PipePipe/Brave forks instead,
                // which vendor the same org.schabi.newpipe.extractor.* API surface this module
                // needs (PoTokenProvider, YoutubeJavaScriptPlayerManager, StreamInfo, etc.).
                implementation(libs.pipepipe.extractor)
                implementation(libs.brave.extractor)
            }
        }
    }
}

// PipePipe brings full com.google.protobuf:protobuf-java, Brave brings protobuf-javalite; both
// occupy the com.google.protobuf.* namespace and trigger DEX duplicate-class failures together.
// Same exclusion kotlinYtmusicScraper's build.gradle.kts applies for the same combo.
configurations.all {
    exclude(group = "com.google.protobuf", module = "protobuf-java")
}
