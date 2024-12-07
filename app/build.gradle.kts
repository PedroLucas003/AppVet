plugins {
    alias(libs.plugins.android.application) // Gerencia com alias
    alias(libs.plugins.kotlin.android)     // Para suporte ao Kotlin
    id("kotlin-kapt")                      // Plugin KAPT
    id("com.google.gms.google-services")   // Firebase Google Services
}

android {
    namespace = "com.example.appvet"
    compileSdk = 35  // Atualizado para 35

    defaultConfig {
        applicationId = "com.example.appvet"
        minSdk = 24
        targetSdk = 35  // Atualizado para 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    dependencies {

        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.lifecycle.runtime.ktx)
        implementation(libs.androidx.activity.compose)
        implementation(platform(libs.androidx.compose.bom))
        implementation(libs.androidx.ui)
        implementation(libs.androidx.ui.graphics)
        implementation(libs.androidx.ui.tooling.preview)
        implementation(libs.androidx.material3)
        implementation(libs.androidx.foundation.android)
        implementation(libs.androidx.ui.text.android)
        implementation(libs.androidx.navigation.runtime.ktx)
        implementation(libs.androidx.navigation.compose)
        implementation(libs.firebase.auth.ktx)
        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
        androidTestImplementation(platform(libs.androidx.compose.bom))
        androidTestImplementation(libs.androidx.ui.test.junit4)
        debugImplementation(libs.androidx.ui.tooling)
        debugImplementation(libs.androidx.ui.test.manifest)

        implementation(libs.retrofit)
        implementation(libs.converter.gson)
        implementation(libs.androidx.room.runtime)
        implementation(libs.androidx.room.ktx)
        kapt(libs.androidx.room.compiler)
        implementation(libs.androidx.runtime.livedata)
        implementation(libs.androidx.appcompat)
        implementation(libs.androidx.navigation.runtime.ktx)
        implementation(libs.androidx.navigation.compose)
        //firebase
        implementation(libs.firebase.bom)
        implementation(libs.firebase.analytics)
        //login
        implementation(libs.firebase.bom.v3280)
        implementation(libs.firebase.auth)

        implementation(libs.firebase.bom.v3220)
        implementation(libs.firebase.analytics.ktx)
        implementation(libs.google.firebase.auth.ktx)
    }
}
dependencies {
    implementation(libs.androidx.room.common)
}
