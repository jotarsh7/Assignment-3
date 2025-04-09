plugins {
    // If you’re not using a version catalog, replace these alias entries with the full plugin IDs.
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.kapt")
    id("com.google.gms.google-services") version "4.4.2"
}

android {
    namespace = "com.example.favoritemovieapp"
    compileSdk = 35
    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
    }
    defaultConfig {
        applicationId = "com.example.favoritemovieapp"
        minSdk = 33
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        viewBinding = true
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // Add the Firebase BOM (update the version as needed; here using an example version)
    implementation(platform("com.google.firebase:firebase-bom:31.2.0"))

    // Now add Firebase dependencies without versions:
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-database-ktx")

    // Other dependencies...
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("com.github.bumptech.glide:glide:4.12.0")
    implementation ("androidx.cardview:cardview:1.0.0")

    //kapt("com.github.bumptech.glide:compiler:4.12.0")
}

