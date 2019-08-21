package ru.prsolution.winstrike.navigation.features

import android.content.Intent
import ru.prsolution.winstrike.navigation.loadIntentOrNull

object HomeNavigation : DynamicFeature<Intent> {

    private const val HOME = "com.sanogueralorenzo.home.HomeActivity"

    override val dynamicStart: Intent?
        get() = HOME.loadIntentOrNull()
}
