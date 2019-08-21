package ru.prsolution.winstrike.presentation.model.login

import ru.prsolution.winstrike.domain.models.login.LoginModel

/**
 * Created by oleg on 07/03/2018.
 */

class LoginInfo(
    val phone: String?,
    val password: String
)

fun LoginInfo.mapToDomain(): LoginModel = LoginModel(
    phone = this.phone,
    password = this.password
)
