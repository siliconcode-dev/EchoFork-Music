plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.android.lint)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    android {
        namespace = "echo.music.enhanced.paxsenixlyrics"
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
                implementation(projects.common)
                implementation(projects.betterlyrics)

                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.okhttp)
                implementation(libs.ktor.client.cio)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.ktor.client.encoding)
            }
        }
    }
}
