plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
    id("kotlin-kapt")
}

android {
    namespace = "com.androhub.networkmodule"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        dataBinding = true
    }
}

publishing {
    publications {
        register<MavenPublication>("release") {
            afterEvaluate {
                from(components["release"])
            }
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // retrofit dependencies
    //retrofit  dependency
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")

////    SDP
    implementation("com.intuit.sdp:sdp-android:1.1.0")
    implementation("com.intuit.ssp:ssp-android:1.0.6")


//    implementation("com.intuit.sdp:sdp-android:1.1.0")
//    implementation("com.intuit.ssp:ssp-android:1.0.6")
    implementation("com.github.bumptech.glide:glide:4.11.0")
    implementation("androidx.databinding:databinding-runtime:8.2.2")
//
    annotationProcessor("com.github.bumptech.glide:compiler:4.11.0")
//
    //Room DB
    val room_version = "2.5.0"
    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    testImplementation("androidx.room:room-testing:$room_version")

    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation("me.yokeyword:fragmentationx-core:1.0.0")
    implementation("me.yokeyword:fragmentationx:1.0.0")
    implementation("me.yokeyword:fragmentationx-swipeback:1.0.0")
    implementation("androidx.cardview:cardview:1.0.0")
//    //For ticket
    implementation("com.github.mreram:ticketview:1.0.0")
//
//    //runtime permission
    implementation("io.github.ParkSangGwon:tedpermission-normal:3.3.0")
//
//    //helpers for kotlin Android SDK
    implementation("org.jetbrains.anko:anko-sdk15:0.8.2")
//    //multidex
    implementation("androidx.multidex:multidex:2.0.1")
    // internet connectivity
    implementation("com.novoda:merlin:1.2.1")
    ///location
    implementation("com.google.android.gms:play-services-location:20.0.0")

    // ViewModel and LiveData
    implementation("android.arch.lifecycle:extensions:1.1.1")
    implementation("android.arch.lifecycle:runtime:1.1.1")
    implementation("android.arch.lifecycle:compiler:1.1.1")
    implementation("net.danlew:android.joda:2.10.12.2")
    implementation("com.github.dhaval2404:imagepicker:2.1")
    implementation("com.github.MackHartley:RoundedProgressBar:3.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
}