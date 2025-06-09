import java.util.Properties


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.amaurypm.videogamesrf"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.amaurypm.videogamesrf"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val localPropertiesMap = Properties()
        val localPropertiesFileMap = rootProject.file("local.properties")
        if(localPropertiesFileMap.exists()){
            localPropertiesMap.load(localPropertiesFileMap.inputStream())
        }

        val mapsApiKey = localPropertiesMap.getProperty("MAPS_API_KEY")

        manifestPlaceholders["MAPS_API_KEY"] = mapsApiKey

        val localProperties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if(localPropertiesFile.exists()){
            localProperties.load(localPropertiesFile.inputStream())
            val apiKey = localProperties.getProperty("API_KEY")

            buildConfigField("String", "API_KEY", "\"$apiKey\"")
        }

        buildFeatures{
            viewBinding = true
            buildConfig = true
        }


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

    buildFeatures{
        viewBinding = true
        buildConfig = true
    }




}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

//Google Maps (Play services de Google Maps, tanto para vistas XML como para Compose)
    implementation(libs.play.services.maps)

//API'S opcionales para la ubicación (XML y Compose). Ej. Clase FusedLocationProviderClient
    implementation(libs.play.services.location)

//Para corrutinas con alcance viewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)


    //Para retrofit y Gson
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    //Adicional para el interceptor
    implementation(libs.logging.interceptor)

    //Glide y Picasso
    implementation(libs.glide)
    implementation(libs.picasso)

    //Para las corrutinas con alcance lifecycle (opcional)
    //implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")

    //Imágenes con bordes redondeados
    implementation(libs.roundedimageview)
    implementation(libs.firebase.auth)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.core.splashscreen)

    //Biblioteca para videos en YouTube
    implementation(libs.core)

}