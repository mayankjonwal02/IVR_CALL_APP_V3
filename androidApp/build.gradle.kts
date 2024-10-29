plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    id ("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.example.ivr_call_app_v3.android"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.example.ivr_call_app_v3.android"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(projects.shared)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.androidx.activity.compose)
    debugImplementation(libs.compose.ui.tooling)
    implementation ("androidx.compose.material:material-icons-core:1.7.0")
    implementation ("androidx.compose.material:material-icons-extended:1.7.0")


    implementation ("io.ktor:ktor-client-core:2.0.0")
    implementation ("io.ktor:ktor-client-android:2.0.0") // You can use a different engine if needed
    implementation ("io.ktor:ktor-client-serialization:2.0.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation ("io.ktor:ktor-client-logging:2.0.0")
    implementation("io.ktor:ktor-client-content-negotiation:2.0.0") // Replace with the correct version
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.0.0")


    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // For JSON serialization
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3") // For logging

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")


    implementation ("androidx.navigation:navigation-compose:2.6.0")

    implementation ("androidx.localbroadcastmanager:localbroadcastmanager:1.0.0")











}