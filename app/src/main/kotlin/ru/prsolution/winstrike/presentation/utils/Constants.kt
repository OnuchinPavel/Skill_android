package ru.prsolution.winstrike.presentation.utils

object Constants {

    const val TOKEN_TYPE_BEARER = "Bearer "
    const val URL_WINSTRIKE_HOST = "winstrike.ru"

    const val URL_WINSTRIKE = "https://winstrike.gg"
    const val URL_CONDITION = "file:///android_asset/rules.html"
    const val URL_POLITIKA = "file:///android_asset/politika.html"

    const val URL_GOOGLE_PLAY = "https://play.google.com/store/apps/details?id=ru.prsolution.winstrike"
    const val URL_VK = "https://vk.com/winstrikearena"
    const val URL_INSTAGRAM = "https://www.instagram.com/winstrikearena/"
    const val URL_TWEETER = "https://twitter.com/winstrikearena"
    const val URL_FACEBOOK = "https://www.facebook.com/winstrikegg/"
    const val URL_TWITCH = "https://www.twitch.tv/winstrikearena"



    const val ORDER_URL = "https://dev.winstrike.ru/api/v1/orders"

    const val PHONE_MASK = "(___) ___-__-__"
    const val PHONE_LENGTH = 15
    const val PASSWORD_LENGTH = 6
    const val CODE_LENGTH = 6
    const val NAME_LENGTH = 4

    const val IMAGE_TYPE = "image/jpg"
    const val SHARE_DRAWABLE = "/drawable/"
    const val SHARE_IMG = "winstrike_share"
    const val SHARE_IMG_TITLE = "Send"
    const val ANDROID_RESOURCES_PATH = "android.resource://"

    // UI
    const val EXIT_DURATION = 1L
    const val ENTER_DURATION = 1L

    // Screen width in px:
    const val SCREEN_WIDTH_PX_720 = 720
    const val SCREEN_WIDTH_PX_1080 = 1080
    const val SCREEN_WIDTH_PX_1440 = 1440

    // Screen height in px:
    const val SCREEN_HEIGHT_PX_1280 = 1280
    const val SCREEN_HEIGHT_PX_1920 = 1920
    const val SCREEN_HEIGHT_PX_2560 = 2560

    // Margin of screen:
    const val SCREEN_MARGIN_350: Int = -350
    const val SCREEN_MARGIN_450: Int = -450
    const val SCREEN_MARGIN_600: Int = -600
    const val LEGEND_MAP_TOP_MARGIN = 200

    // Date and time
    const val SECOND = 1000
    private val MINUTE = 60 * SECOND
    private val HOUR = 60 * MINUTE
}