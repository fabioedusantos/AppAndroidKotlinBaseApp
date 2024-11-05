plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    //Necessário para o Firebase
    alias(libs.plugins.google.services)

    //necessário para o ROOM (DB)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.androidx.room)
}

room {
    // ROOM Gera/usa os JSONs de esquema aqui
    schemaDirectory("$projectDir/schemas")
}

android {
    namespace = "br.com.fbsantos.baseapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "br.com.fbsantos.baseapp"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"

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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //KOIN - INJEÇÃO DE DEPÊNDENCIA
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    //ANIMAÇÕES
    implementation(libs.accompanist.navigation.animation)
    implementation(libs.androidx.compose.animation)

    //ICONES
    implementation(libs.androidx.material.icons.extended)

    //RECAPTCHA
    implementation(libs.recaptcha)

    //FIREBASE: AUTENTICAÇÃO GOOGLE
    implementation(libs.firebase.ui.auth)

    //MARKDOWN
    implementation(libs.markdown.renderer.m3)
    implementation(libs.markdown.renderer)

    //RETROFIT
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp)
    implementation(libs.gson)

    //ROOM: NOSSO BANCO DE DADOS
    implementation(libs.androidx.room.common.jvm)
    implementation(libs.androidx.room.runtime.android)
    kapt(libs.androidx.room.compiler)

    //COIL: PARA USAR AsyncImage
    implementation(libs.coil.compose)
}