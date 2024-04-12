plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    kotlin("kapt") version "1.9.10"
}

android {
    namespace = "com.android.gutendex"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.android.gutendex"
        minSdk = 28
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
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
    // Retrofit
    val retrofit = "2.9.0"

    // OkHttp3
    val okhttp3 = "4.9.2"


    // AndroidX
    val android_ktx = "1.12.0"
    val androix_activity_compose = "1.8.0"
    val androidx_lifecycle = "2.6.0-alpha02"
    val cl_version = "1.0.1"


    // Android Hilt
    val hilt_version = "2.48.1"
    val hilt_compose_navigation = "1.0.0"
    val hilt_compiler = "1.0.0"

    // Google Accompanist
    val google_accompanist = "0.32.0"
    val flow_layout_version = "0.28.0"




    implementation("androidx.navigation:navigation-compose:2.7.6")

    implementation("androidx.core:core-splashscreen:1.0.1")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    debugImplementation("androidx.compose.ui:ui-tooling")
    // Choose one of the following:
    // Material Design 3
    implementation("androidx.compose.material3:material3:1.2.0")
    implementation("androidx.compose.material3:material3-window-size-class:1.2.0")
    implementation("androidx.compose.material3:material3-adaptive:1.0.0-alpha06")
    implementation("androidx.compose.material3:material3-adaptive-navigation-suite:1.0.0-alpha03")
    // or skip Material Design and build directly on top of foundational components
    implementation("androidx.compose.foundation:foundation")
    // or only import the main APIs for the underlying toolkit systems,
    // such as input and measurement/layout
    implementation("androidx.compose.ui:ui")

    // Optional - Included automatically by material, only add when you need
    // the icons but not the material library (e.g. when using Material3 or a
    // custom design system based on Foundation)
    implementation("androidx.compose.material:material-icons-core")

    // Android Studio Preview support

    // hilt
    implementation("com.google.dagger:hilt-android:$hilt_version")
    implementation("androidx.hilt:hilt-navigation-compose:$hilt_compose_navigation")
    implementation("androidx.hilt:hilt-common:$hilt_compiler")
    implementation("androidx.hilt:hilt-work:$hilt_compiler")

    kapt("com.google.dagger:hilt-android-compiler:$hilt_version")
    kapt("androidx.hilt:hilt-compiler:$hilt_compiler")

    //Accompanist
    implementation("com.google.accompanist:accompanist-systemuicontroller:$google_accompanist")
    implementation("com.google.accompanist:accompanist-navigation-animation:$google_accompanist")
    implementation("com.google.accompanist:accompanist-pager:$google_accompanist")
    implementation("com.google.accompanist:accompanist-pager-indicators:$google_accompanist")
    implementation("com.google.accompanist:accompanist-placeholder-material:$google_accompanist")
    implementation("com.google.accompanist:accompanist-flowlayout:$flow_layout_version")
    implementation("com.google.accompanist:accompanist-permissions:$google_accompanist")
    implementation("com.google.accompanist:accompanist-webview:$google_accompanist")
    implementation("com.google.accompanist:accompanist-permissions:0.32.0")


    //AndroidX
    implementation("androidx.core:core-ktx:$android_ktx")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$androidx_lifecycle")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.constraintlayout:constraintlayout-compose:$cl_version")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:$androidx_lifecycle")
    implementation("androidx.work:work-runtime-ktx:2.9.0")


    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.10")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // OkHttp3
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttp3")

    //DataStore
    implementation("androidx.datastore:datastore-preferences-core:1.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    implementation("androidx.datastore:datastore-preferences-rxjava2:1.0.0")
    implementation("androidx.datastore:datastore-preferences-rxjava3:1.0.0")

    //Google Services & Maps
    implementation("com.google.android.gms:play-services-location:21.1.0")
    implementation("com.google.maps.android:maps-compose:4.3.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0")


    implementation("com.google.maps.android:maps-compose-utils:4.2.0")
    implementation("com.google.maps:google-maps-services:0.15.0")
    implementation("com.google.maps.android:android-maps-utils:3.7.0")

    implementation("org.json:json:20210307")
}