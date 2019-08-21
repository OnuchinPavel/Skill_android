package ru.prsolution.winstrike.presentation

import androidx.navigation.NavController
import androidx.navigation.NavDirections

/**
 * Created by Oleg Sitnikov on 2019-02-20
 */

interface NavigationListener {
    fun navigate(action: NavDirections)
    var mNavController: NavController
}
