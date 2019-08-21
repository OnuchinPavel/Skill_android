package ru.prsolution.winstrike.presentation.model.login

import ru.prsolution.winstrike.domain.models.login.NewUserModel

/**
 * Created by oleg on 07/03/2018.
 */

class NewUserInfo(
    val phone: String?,
    val password: String
)

fun NewUserInfo.mapToDomain(): NewUserModel = NewUserModel(
    phone = this.phone,
    password = this.password
)
