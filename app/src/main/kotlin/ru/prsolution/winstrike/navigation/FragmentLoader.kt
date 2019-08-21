package ru.prsolution.winstrike.navigation

import androidx.fragment.app.Fragment

internal fun String.loadFragmentOrNull(): Fragment? =
    try {
        this.loadClassOrNull<Fragment>()?.newInstance()
    } catch (e: ClassNotFoundException) {
        null
    }
