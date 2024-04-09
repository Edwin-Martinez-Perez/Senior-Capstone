plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-android")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.collectivetrek"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.collectivetrek"
        minSdk = 23
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

    dataBinding {
        enable = true
    }

    buildFeatures {
        dataBinding = true
        //material3 test
        //compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8

        //material3 test
        //kotlinCompilerExtensionVersion = "1.1.1"

    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    //changed to use compose compiler
//    implementation("androidx.core:core-ktx:1.7.20")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")

    // for bottom navigation
    implementation ("com.google.android.material:material:1.5.0")
    //implementation("com.google.android.material:material:1.11.0")

    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("com.google.firebase:firebase-database:20.3.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    //navigation
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.navigation:navigation-fragment-ktx: 2.5.3")
    //retrofit implementation
    // implementation("com.square.retrofit2:retrofit:2.9.0") //commented out to connect to firebase
    // implementation("com.square.retrofit2:converter-scalars:2.9.0") // commented out to connect to firebase
    //room implementation
    implementation("androidx.room:room-ktx:2.4.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.3.0")
    implementation("io.coil-kt:coil-compose:2.5.0")
}