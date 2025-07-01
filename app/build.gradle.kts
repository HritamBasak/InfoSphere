plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.infosphere"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.infosphere"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures{
        viewBinding=true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation (libs.lottie)
    implementation(libs.firebase.auth)
    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))
    implementation(libs.play.services.auth)
    implementation (libs.material.v190)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.database)
    implementation (libs.picasso)
    implementation (libs.news.api.java.v102)
    implementation(libs.volley)
    implementation(libs.generativeai)
    implementation (libs.swiperefreshlayout)
    implementation(libs.google.guava)
    implementation(libs.kotlin.script.runtime)
    implementation(libs.reactive.streams)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(kotlin("script-runtime"))
}