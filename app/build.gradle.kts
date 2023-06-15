import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    id("org.jetbrains.kotlin.kapt")
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "org.calamarfederal.fetchexercise"
    compileSdk = 33

    defaultConfig {
        applicationId = "org.calamarfederal.fetchexercise"
        minSdk = 33
        targetSdk = 33
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
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.current() // fixes jvm version incompatibility using hilt
//        targetCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.current() // fixes jvm version incompatibility using hilt
    }
    kotlinOptions {
//        jvmTarget = "1.8"
        jvmTarget = "17" // fixes jvm version incompatibility using hilt
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.7"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildToolsVersion = "34.0.0"
}

dependencies {

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.06.00"))
    implementation("androidx.compose.ui:ui:1.4.3")
    implementation("androidx.compose.ui:ui-graphics:1.4.3")
    implementation("androidx.compose.ui:ui-tooling-preview:1.4.3")
    implementation("androidx.compose.material3:material3:1.1.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.06.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.4.3")
    debugImplementation("androidx.compose.ui:ui-tooling:1.4.3")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.4.3")

    /**
     * # Dagger Hilt
     */
    implementation("com.google.dagger:hilt-android:2.46.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    kapt("com.google.dagger:hilt-compiler:2.46.1")

    // For instrumentation tests
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.46.1")
    androidTestAnnotationProcessor("com.google.dagger:hilt-compiler:2.46.1")

    // For local unit tests
    testImplementation("com.google.dagger:hilt-android-testing:2.46.1")
    testAnnotationProcessor("com.google.dagger:hilt-compiler:2.46.1")

    /**
     * # Retrofit
     */
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
}

kapt {
    correctErrorTypes = true
}
