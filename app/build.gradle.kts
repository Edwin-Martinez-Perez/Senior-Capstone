import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-android")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
    // for API key
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
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

        //TODO change to be safe
        //for Places API
        buildConfigField("String", "PLACES_API_KEY", "AIzaSyCIozrnAqwwZP3U7iywRMmUDwDW9c0EHiU")
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
        viewBinding = true
        //material3 test
        //compose = true
        buildConfig = true
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
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("com.google.firebase:firebase-auth-ktx:22.3.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    //navigation
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.navigation:navigation-fragment-ktx: 2.5.3")
    implementation("androidx.room:room-ktx:2.4.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.3.0")
    implementation("io.coil-kt:coil-compose:2.5.0")

    //Place SDK
    implementation("com.google.android.libraries.places:places:3.3.0")

}

//for API key
secrets {
    // Optionally specify a different file name containing your secrets.
    // The plugin defaults to "local.properties"
    propertiesFileName = "app/secrets.properties"

    //TODO
    // A properties file containing default secret values. This file can be
    // checked in version control.
    defaultPropertiesFileName = "app/local.defaults.properties"

    // Configure which keys should be ignored by the plugin by providing regular expressions.
    // "sdk.dir" is ignored by default.
    ignoreList.add("keyToIgnore") // Ignore the key "keyToIgnore"
    ignoreList.add("sdk.*")       // Ignore all keys matching the regexp "sdk.*"
}
