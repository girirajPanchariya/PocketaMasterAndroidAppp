plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    // KAPT
    id("kotlin-kapt")
}

android {

    namespace = "com.cscorner.pocketmaster"

    compileSdk {
        version = release(36)
    }

    defaultConfig {

        applicationId = "com.cscorner.pocketmaster"

        minSdk = 29
        targetSdk = 36

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {

        release {

            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true
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

    // =========================
    // Android Core
    // =========================

    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.appcompat)

    implementation(libs.androidx.activity)

    implementation(libs.androidx.fragment.ktx)

    implementation(libs.androidx.constraintlayout)

    implementation(libs.androidx.legacy.support.v4)

    implementation(libs.material)


    // =========================
    // Lifecycle
    // =========================

    implementation(libs.androidx.lifecycle.livedata.ktx)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    implementation(
        "androidx.lifecycle:lifecycle-runtime-ktx:2.11.0"
    )


    // =========================
    // ROOM DATABASE
    // =========================

    implementation(libs.androidx.room.runtime)

    implementation(libs.androidx.room.ktx)

    kapt(libs.androidx.room.compiler)


    // =========================
    // Chart
    // =========================

    implementation(
        "com.github.PhilJay:MPAndroidChart:v3.1.0"
    )


    // =========================
    // Coroutines
    // =========================

    implementation(
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4"
    )


    // =========================
    // Testing
    // =========================

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)

    androidTestImplementation(
        libs.androidx.espresso.core
    )
}