package ru.prsolution.winstrike.presentation.model.login

import ru.prsolution.winstrike.domain.models.login.ProfileModel


/**
 * Created by oleg on 07/03/2018.
 */

class ProfileInfo(
    val phone: String,
    val name: String
)


fun ProfileInfo.mapToDomain(): ProfileModel = ProfileModel(
    phone = this.phone,
    name = this.name
)
