package ru.prsolution.winstrike.presentation.utils.pref

import android.content.Context
import android.content.SharedPreferences

import ru.prsolution.winstrike.App

/**
 * Date: 18.01.2016
 * Time: 15:01
 *
 */
object SharedPrefFactory {
    private const val PREF_NAME = "winstrike_preferences"

    val prefs: SharedPreferences
        get() = App.instance.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE)

    val editor: SharedPreferences.Editor
        get() = prefs.edit()
}
