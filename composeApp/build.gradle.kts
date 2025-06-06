
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val version = "1.0.40"
val versionNumber = getVersionInt()

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.android)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.kotlin.test.junit)
            implementation(libs.coil.network.okhttp)
            implementation(libs.accompanist.permissions)
        }

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(project.dependencies.platform(libs.compose.bom))
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.logging)
            implementation(libs.kotlinx.io.core)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlin.serialization)
            implementation(libs.androidx.datastore.preferences.core)
            implementation(libs.coil.mp)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)

            // Test dependencies
            implementation(libs.kotlin.test)
            implementation(libs.kotlin.coroutines.test)
            implementation(libs.ktor.server.test.host)
            implementation(libs.assertj.core)
            implementation(libs.ktor.client.mock)
            implementation(libs.turbine)
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.appdirs)
        }
    }
}

android {
    namespace = "com.adriantache"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.adriantache"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = versionNumber
        versionName = version
    }
    signingConfigs {
        @Suppress("unused")
        val release by creating {
            storeFile = file("../keystore.jks")
            storePassword = System.getenv("RELEASE_KEYSTORE_PASSWORD")
            keyAlias = System.getenv("RELEASE_KEYSTORE_ALIAS")
            keyPassword = System.getenv("RELEASE_KEY_PASSWORD")
        }
    }
    packaging {

        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/licenses/ASM"
            excludes += "/META-INF/DEPENDENCIES"
            excludes += "win32-x86-64/attach_hotspot_windows.dll"
            excludes += "win32-x86/attach_hotspot_windows.dll"
            excludes += "/META-INF/LICENSE.md"
            excludes += "/META-INF/LICENSE-notice.md"
            excludes += "/META-INF/versions/9/module-info.class"
            excludes += "META-INF/versions/9/module-info.class"
            excludes += "META-INF/DEPENDENCIES.txt"
            excludes += "META-INF/LICENSE"
            excludes += "META-INF/LICENSE.txt"
            excludes += "META-INF/license.txt"
            excludes += "META-INF/NOTICE"
            excludes += "META-INF/NOTICE.txt"
            excludes += "META-INF/notice.txt"
            excludes += "META-INF/INDEX.LIST"
            excludes += "META-INF/versions"
            excludes += "**/module-info.class"
            excludes += "META-INF/versions/9/OSGI-INF/MANIFEST.MF"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs["release"]
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            // This adds the target OS to the name of binaries, in order to differentiate the two DMG files
            // which are generated on macOS, one for Intel processors and one for ARM.
            val targetOS = System.getenv()["TARGET_OS"]?.trim() ?: "LOCAL"
            packageName = "GPT Assistant $targetOS"
            packageVersion = version

            // Required by the file we build with DataStore, or something.
            // Full explanation here: https://stackoverflow.com/a/61758667/9038481
            modules("jdk.unsupported")

            windows {
                iconFile.set(project.file("icon.png"))
            }
        }
    }
}

private fun getVersionInt(): Int {
    return version.replace(".", "").toInt()
}
