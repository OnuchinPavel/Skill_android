package ru.prsolution.winstrike.navigation

import android.content.Intent

private const val PACKAGE_NAME = "ru.prsolution.winstrike"

private fun intentTo(className: String): Intent =
        Intent(Intent.ACTION_VIEW).setClassName(PACKAGE_NAME, className)

internal fun String.loadIntentOrNull(): Intent? =
        try {
            Class.forName(this).run { intentTo(this@loadIntentOrNull) }
        } catch (e: ClassNotFoundException) {
            null
        }
