package ru.prsolution.winstrike.presentation.splash

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fmt_splash.*
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.presentation.NavigationListener
import ru.prsolution.winstrike.presentation.utils.inflate
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils

/**
 * Created by Oleg Sitnikov on 2019-02-18
 */

class SplashFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return context?.inflate(R.layout.fmt_splash)
    }

    private var navigateToCity: Boolean = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        arguments?.let {
            val saveArgs = SplashFragmentArgs.fromBundle(it)
            navigateToCity = saveArgs.navigateToCity
        }

        animation_view.imageAssetsFolder = "images"
        animation_view.setAnimation("data.json")
        animation_view.repeatCount = 0
        animation_view.scale = 1f
        animation_view.addAnimatorListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                start()
            }
        })
    }

    private fun start() {
        // If user is sign out -> go to login screen. else -> check if user is exist on server and if Ok -- go to Main screen.
        when {
            navigateToCity -> {
                val action = SplashFragmentDirections.actionToCityList()
                (activity as NavigationListener).navigate(action)
            }

            // Token NOT Empty AND City pid NOT Empty -> Home
            !PrefUtils.token?.isEmpty()!! && !PrefUtils.cityPid?.isEmpty()!! -> {
                val action = SplashFragmentDirections.actionToHome()
                (activity as NavigationListener).navigate(action)
            }

            // Token NOT Empty  -> City List
            !PrefUtils.token?.isEmpty()!! -> {
                val action = SplashFragmentDirections.actionToCityList()
                (activity as NavigationListener).navigate(action)
            }

            // Login
            else -> {
                val action = SplashFragmentDirections.actionToLogin()
                (activity as NavigationListener).navigate(action)
            }
        }
    }

}