plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.secondandroidhita"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.secondandroidhita"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.squareup.okhttp3:okhttp:4.9.3") // לביצוע בקשות HTTP
    implementation("com.google.code.gson:gson:2.8.9") // לפענוח JSON
    implementation("androidx.recyclerview:recyclerview:1.2.1") // תמיכה ב-RecyclerView
    implementation("com.github.bumptech.glide:glide:4.12.0") // טעינת תמונות מהאינטרנט
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0") // תמיכה ב-Glide
}