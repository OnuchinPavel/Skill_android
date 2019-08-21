import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.api.BaseVariantOutput
import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import org.jetbrains.kotlin.gradle.internal.AndroidExtensionsExtension

plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    kotlin("android")
    kotlin("android.extensions")
    id("androidx.navigation.safeargs")
    id("com.github.ben-manes.versions") version "0.20.0" // uses gradle depUp ; show old dependencies in terminal
    id("org.jmailen.kotlinter") version "1.21.0"
    id("io.fabric")
}

repositories {
    maven {
        url = uri("https://maven.fabric.io/public")
    }
}

androidExtensions {
    configure(delegateClosureOf<AndroidExtensionsExtension> {
        isExperimental = true
    })
}

android {
    compileSdkVersion(Android.compileSdkVersion)
    defaultConfig {
        applicationId = ApplicationId.id
        targetSdkVersion(Android.targetSdkVersion)
        minSdkVersion(Android.minSdkVersion)
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true

        versionCode =
            (AppVersion.majorAppVersion * 10_000) + (AppVersion.minorAppVersion * 1_000) + (AppVersion.patchAppVersion * 100)

        versionName = "${AppVersion.majorAppVersion}" +
            ".${AppVersion.minorAppVersion}" +
            ".${AppVersion.patchAppVersion}"


        applicationVariants.all(object : Action<ApplicationVariant> {
            override fun execute(variant: ApplicationVariant) {
                println("variant: $variant")
                variant.outputs.all(object : Action<BaseVariantOutput> {
                    override fun execute(output: BaseVariantOutput) {

                        val outputImpl = output as BaseVariantOutputImpl
                        val fileName = output.outputFileName
                            .replace("-release", "-release-v$versionName-vc$versionCode")
                            .replace("-debug", "-debug-v$versionName-vc$versionCode")
                        println("output file name: $fileName")
                        outputImpl.outputFileName = fileName
                    }
                })
            }
        })


        dexOptions {
            preDexLibraries = false
            javaMaxHeapSize = "4g" // 2g should be also OK
        }

    }

    buildTypes {
        getByName("debug") {
            buildConfigField("String", "PROD_URL", Constants.PROD_URL)
            buildConfigField("String", "DEV_URL", Constants.DEV_URL)
        }
        getByName("release") {
            buildConfigField("String", "PROD_URL", Constants.PROD_URL)
            isUseProguard = false // user R8 instead
            isMinifyEnabled = false
        }
    }

    compileOptions {
        setSourceCompatibility(1.8)
        setTargetCompatibility(1.8)
    }

    dataBinding {
        isEnabled = false
    }

    lintOptions {
        isCheckReleaseBuilds = false
    }

    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
    }

    androidExtensions {
        isExperimental = true
    }

}

kotlinter {
    ignoreFailures = false
    indentSize = 4
    continuationIndentSize = 4
    reporter = listOf("checkstyle", "plain").toString()
}

/*dexcount {
    format = "list"
    includeClasses = false
    includeClassCount = false
    includeFieldCount = true
    includeTotalMethodCount = false
    orderByMethodCount = false
    verbose = false
    maxTreeDepth = Integer.MAX_VALUE
    teamCityIntegration = false
    teamCitySlug = null
    runOnEachPackage = true
    maxMethodCount = 100_000
    enabled = true
}*/


dependencies {

    /** Date picker library **/
    implementation(project(Modules.datepicker))

    /** Kotlin */
    implementation(Libraries.kotlin)


    implementation(Libraries.androidx)
    implementation(Libraries.lifecycle)

    /** Navigation */
    implementation(Libraries.navigationFragment)
    implementation(Libraries.navigationUI)

    /** Constraint Layout */
    implementation(Libraries.constraintLayout)

    /** Design */
    // Fresco
    implementation(Libraries.fresco)
    // Lottie
    implementation(Libraries.lottie)

    /** Networkin */
    // Coroutines
    implementation(Libraries.coroutines)
    // Retrofit
    implementation(Libraries.retrofit)
    implementation(Libraries.retrofitCoroutines)
    // OkHttp
    implementation(Libraries.okhttp)
    implementation(Libraries.okhttpLoging)
    // Moshi
    implementation(Libraries.moshi)

    /** Utils */
    // Anko from jetbrain
    implementation(Libraries.anko)

    /** FCM */
    implementation(Libraries.fireBase)
    implementation(Libraries.fireBaseMessaging)

    /** Logs */
    // Timber
    implementation(Libraries.timber)
    // Chuck
//    debugImplementation(Libraries.chuck)
//    releaseImplementation(Libraries.chuckRelease)

/*    debugImplementation(Libraries.leakCanaryAndroid)
    releaseImplementation(Libraries.leakCanaryAndroidNoOp)
    debugImplementation(Libraries.leakCanaryAndroidSupportFragment)*/

    /** 3-trd party libraries */
    // phone mask
    implementation(Libraries.decoro)
    // password
//    implementation(Libraries.showhidepasswordedittext)


    // rxpaper
    implementation(Libraries.rxpaper)

    // koin android
    implementation( Libraries.koinAndroid)
    implementation( Libraries.koinViewModel)

    // material dialog
    implementation(Libraries.materialDialog)

    implementation("com.crashlytics.sdk.android:crashlytics:2.9.9@aar"){isTransitive = true}
}
