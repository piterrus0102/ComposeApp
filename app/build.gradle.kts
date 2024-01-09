plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "com.example.composeapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.composeapp"
        minSdk = 21
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    //AppModules
    implementation(project(":feature-test-camera"))
    implementation(project(":feature-test-battery"))
    implementation(project(":feature-test-audio"))
    implementation(project(":test-core"))
    //ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    //Compose
    implementation(libs.compose.ui)
    implementation(libs.compose.graphics)
    implementation(libs.compose.preview)
    debugImplementation(libs.compose.tooling)
    debugImplementation(libs.compose.test.manifest)
    androidTestImplementation(libs.compose.test.junit4)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(platform(libs.compose.bom))
    //ComposeActivity
    implementation(libs.compose.activity)
    //Navigation
    implementation(libs.compose.navigation)
    //Core Ktx
    implementation(libs.core.ktx)
    //Immutable Collections
    implementation(libs.immutable.collections)
    //Material 3
    implementation(libs.material3)
    //JUnit4 + Espresso
    testImplementation(libs.junit)
    androidTestImplementation(libs.junit.extensions)
    androidTestImplementation(libs.espresso.core)
    //Koin
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    //Accompanist
    implementation(libs.google.accompanist)
    // Camera Api for getting CameraSelector
    implementation(libs.androidx.camera)
    // Coil
    implementation(libs.coil.kt)
    //Dagger 2
    implementation(libs.dagger.core)
    kapt(libs.dagger.compiler)
    implementation(kotlin("reflect"))

}