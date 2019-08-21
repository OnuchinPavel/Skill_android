package ru.prsolution.winstrike.domain.models.login

import ru.prsolution.winstrike.datasource.model.login.LoginEntity

/**
 * Created by oleg on 07/03/2018.
 */

class LoginModel(
    val phone: String?,
    val password: String
)

fun LoginModel.mapToDataSource(): LoginEntity = LoginEntity(
    phone = this.phone,
    password = this.password
)
