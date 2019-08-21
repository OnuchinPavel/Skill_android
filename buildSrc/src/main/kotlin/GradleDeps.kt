object AppVersion {
	// App version information
	const val majorAppVersion = 0
	const val minorAppVersion = 55
	const val patchAppVersion = 40
	const val buildAppVersion = 0
}


object ApplicationId {
	const val id = "ru.prsolution.winstrike"
}

object Android {
	const val compileSdkVersion = 28
	const val minSdkVersion = 21
	const val targetSdkVersion = 28
}


object Versions {
	const val gradle = "3.5.0-alpha10"

	const val googleServicies = "4.2.0"
	const val kotlin = "1.3.21"
    const val anko = "0.10.8"
	const val androidx = "1.1.0-alpha02"
	const val lifecycle = "2.0.0"
	const val ktx = "1.0.0-alpha1"

	const val navigation = "1.0.0-rc02"

	const val constraintLayout = "2.0.0-alpha3"


	const val retrofit = "2.5.0"
	const val okhttp = "3.13.1"
	const val coroutinesRetrofit = "0.9.2"
	const val coroutines = "1.1.1"

	const val fresco = "1.13.0"
	const val lottie = "3.0.0-beta3"
	const val decoro = "1.3.5"
	const val showhidepasswordedittext = "0.8"

	const val chuck = "1.1.0"
	const val timber = "4.7.1"

	const val firebase = "16.0.7"
	const val firebaseMessaging = "17.4.0"
	const val fabric = "1.30.0"
	const val crashlytics = "2.9.8"

	const val leakCanary = "1.6.3"

	const val rxpaper = "1.4.0"

	const val koin = "2.0.0-beta-1"

	const val materialDialog = "2.0.3"
}


object Libraries {
	const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
	const val androidx = "androidx.appcompat:appcompat:${Versions.androidx}"
	const val ktx = "androidx.core:core-ktx:${Versions.ktx}"
	const val lifecycle = "android.arch.lifecycle:extensions:${Versions.lifecycle}"

    const val navigationFragment ="android.arch.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigationUI =  "android.arch.navigation:navigation-ui-ktx:${Versions.navigation}"


	const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
	const val fresco = "com.facebook.fresco:fresco:${Versions.fresco}"

	const val lottie = "com.airbnb.android:lottie:${Versions.lottie}"
	const val decoro = "ru.tinkoff.decoro:decoro:${Versions.decoro}"
	const val showhidepasswordedittext = "com.github.scottyab:showhidepasswordedittext:${Versions.showhidepasswordedittext}"

	const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
	const val moshi = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"
	const val retrofitCoroutines = "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:${Versions.coroutinesRetrofit}"
	const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"

	const val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okhttp}"
	const val okhttpLoging = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}"

	const val anko = "org.jetbrains.anko:anko:${Versions.anko}"

	const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
	const val fireBase = "com.google.firebase:firebase-core:${Versions.firebase}"
	const val fireBaseMessaging = "com.google.firebase:firebase-messaging:${Versions.firebaseMessaging}"

	const val chuck = "com.readystatesoftware.chuck:library:${Versions.chuck}"
	const val chuckRelease = "com.readystatesoftware.chuck:library-no-op:${Versions.chuck}"

  	const val leakCanaryAndroid = "com.squareup.leakcanary:leakcanary-android:${Versions.leakCanary}"
	const val leakCanaryAndroidNoOp = "com.squareup.leakcanary:leakcanary-android-no-op:${Versions.leakCanary}"
	const val leakCanaryAndroidSupportFragment = "com.squareup.leakcanary:leakcanary-support-fragment:${Versions.leakCanary}"

	const val rxpaper = "com.github.pakoito:RxPaper2:${Versions.rxpaper}"

	const val koinAndroid = "org.koin:koin-android:${Versions.koin}"
	const val koinViewModel = "org.koin:koin-androidx-viewmodel:${Versions.koin}"

	const val materialDialog = "com.afollestad.material-dialogs:core:${Versions.materialDialog}"

}

// API URL
object Constants {
	const val DEV_URL = "\"https://winstrike.ml/api/v1/\""
	const val PROD_URL = "\"https://winstrike.ml/api/v1/\""
}

object Modules {
	const val app = ":app"
	const val datepicker = ":datepicker"
	const val preference = ":preference"
}

