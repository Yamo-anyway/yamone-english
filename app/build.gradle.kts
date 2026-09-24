import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

val localKeystorePropertiesFile = rootProject.file("keystore.properties")
val localKeystoreProperties = Properties().apply {
    if (localKeystorePropertiesFile.isFile) {
        localKeystorePropertiesFile.inputStream().use(::load)
    }
}

fun signingSecret(environmentName: String, propertyName: String): String? =
    providers.environmentVariable(environmentName).orNull?.takeIf { it.isNotBlank() }
        ?: localKeystoreProperties.getProperty(propertyName)?.takeIf { it.isNotBlank() }

val releaseStorePath = signingSecret("YAMONE_UPLOAD_STORE_FILE", "storeFile")
val releaseStorePassword = signingSecret("YAMONE_UPLOAD_STORE_PASSWORD", "storePassword")
val releaseKeyAlias = signingSecret("YAMONE_UPLOAD_KEY_ALIAS", "keyAlias")
val releaseKeyPassword = signingSecret("YAMONE_UPLOAD_KEY_PASSWORD", "keyPassword")
val releaseStoreFile = releaseStorePath?.let(rootProject::file)
val hasReleaseSigning = releaseStoreFile?.isFile == true &&
    !releaseStorePassword.isNullOrBlank() &&
    !releaseKeyAlias.isNullOrBlank() &&
    !releaseKeyPassword.isNullOrBlank()

android {
    namespace = "com.yamone.english"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.yamone.english"
        minSdk = 26
        targetSdk = 36
        versionCode = 33
        versionName = "0.8.5"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    signingConfigs {
        if (hasReleaseSigning) {
            create("uploadRelease") {
                storeFile = requireNotNull(releaseStoreFile)
                storePassword = requireNotNull(releaseStorePassword)
                keyAlias = requireNotNull(releaseKeyAlias)
                keyPassword = requireNotNull(releaseKeyPassword)
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            if (hasReleaseSigning) {
                signingConfig = signingConfigs.getByName("uploadRelease")
            }
        }
    }

    lint {
        abortOnError = true
        checkReleaseBuilds = true
    }

    packaging {
        resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2024.12.01"))
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.activity:activity-compose:1.10.0")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")
    debugImplementation("androidx.compose.ui:ui-tooling")
    testImplementation("junit:junit:4.13.2")
}
