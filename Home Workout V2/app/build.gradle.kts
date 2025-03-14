plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "fitnessapp.workout.homeworkout"
    compileSdk = 35

    defaultConfig {
        applicationId = "fitnessapp.workout.homeworkout"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        minSdk = 24
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
    dataBinding {
        enable = true
    }
    buildFeatures {
        buildConfig = true
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    //Third Part Dependencies
    implementation(libs.gson)
    implementation(libs.okhttp)
    implementation(libs.circleimageview)
    implementation(libs.image.cropper)
    implementation(libs.dexter)
    implementation(libs.glide)
    annotationProcessor(libs.glide.compiler)
    implementation(libs.firebase.messaging)
//    implementation(libs.firebase.analytics)
    implementation(libs.materialdrawer)
    implementation(libs.lifecycle.extensions)
    implementation(libs.sdp)
    implementation(libs.ssp)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    testImplementation(libs.retrofit.mock)
    implementation(libs.expandable.recyclerview)
    implementation(libs.number.picker)
    implementation(libs.multitype)
    implementation(libs.preference.ktx)
    implementation(libs.billing.ktx)
//    implementation(libs.play.services.ads)
//    implementation(libs.audience.network)
//    implementation(libs.audience.network.old)
    implementation(libs.work.runtime.ktx)
    implementation ("com.airbnb.android:lottie:3.4.4")

    implementation(project(":MPChartLib"))
    implementation(project(":horizontal-picker"))

}