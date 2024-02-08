import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.climus.climeet"
    compileSdk = 34

    Properties().apply {
        load(FileInputStream("$rootDir/local.properties"))
    }

    defaultConfig {
        applicationId = "com.climus.climeet"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "0.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "BASE_DEV_URL", getProperty("baseDevUrl"))
        buildConfigField("String", "BASE_PROD_URL", getProperty("baseProdUrl"))
        buildConfigField("String", "NAVER_CLIENT_ID", getProperty("naverClientId"))
        buildConfigField("String", "NAVER_CLIENT_SECRET", getProperty("naverClientSecret"))
        buildConfigField("String", "NAVER_CLIENT_NAME", getProperty("naverClientName"))
        buildConfigField("String", "KAKAO_API_KEY", getProperty("kakaoAppKey"))
        manifestPlaceholders["KAKAO_API_KEY"] = getProperty("kakaoAppKeyForManifest")
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
        buildConfig = true
    }
}

fun getProperty(propertyKey: String): String {
    return gradleLocalProperties(rootDir).getProperty(propertyKey)
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // hilt
    val hiltVersion = "2.48"
    implementation("com.google.dagger:hilt-android:${hiltVersion}")
    kapt("com.google.dagger:hilt-android-compiler:${hiltVersion}")

    // retrofit
    val retrofitVersion = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")

    // okHttp
    val okHttpVersion = "5.0.0-alpha.2"
    implementation("com.squareup.okhttp3:okhttp:$okHttpVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okHttpVersion")
    implementation("com.squareup.okhttp3:okhttp-urlconnection:$okHttpVersion")

    // glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.github.bumptech.glide:compose:1.0.0-beta01")

    // room
    val roomVersion = "2.6.0"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    // navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.1")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.1")

    // Circle Indicator
    implementation("me.relex:circleindicator:2.1.6")

    // naver login
    implementation("com.navercorp.nid:oauth-jdk8:5.6.0")

    // kakao login
    implementation("com.kakao.sdk:v2-all:2.17.0")

    // CircleImageView
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // Live Data
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")

    // Calendar
    implementation ("com.github.prolificinteractive:material-calendarview:2.0.1")
    implementation ("com.jakewharton.threetenabp:threetenabp:1.1.1")
    implementation ("com.kizitonwose.calendar:view:2.4.1")

    // View Pager
    implementation ("androidx.viewpager2:viewpager2:1.0.0")

    // firebase
    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))

    // video compressor
    implementation("com.github.AbedElazizShe:LightCompressor:1.3.2")

    // exoplayer2
    implementation("com.google.android.exoplayer:exoplayer:2.19.1")


}