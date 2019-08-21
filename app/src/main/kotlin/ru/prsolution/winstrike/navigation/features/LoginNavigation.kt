package ru.prsolution.winstrike.navigation.features

import android.content.Intent
import ru.prsolution.winstrike.navigation.loadIntentOrNull

object LoginNavigation : DynamicFeature<Intent> {

    private const val LOGIN = "com.sanogueralorenzo.login.LoginActivity"

    override val dynamicStart: Intent?
        get() = LOGIN.loadIntentOrNull()
}
