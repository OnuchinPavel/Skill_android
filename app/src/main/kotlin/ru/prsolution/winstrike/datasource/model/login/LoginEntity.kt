package ru.prsolution.winstrike.datasource.model.login

import com.squareup.moshi.Json
import ru.prsolution.winstrike.domain.models.login.LoginModel

/**
 * Created by oleg on 07/03/2018.
 */

class LoginEntity(
    @field:Json(name = "username")
    val phone: String?,
    val password: String
)

fun LoginEntity.maptoDomain(): LoginModel = LoginModel(
    phone = this.phone,
    password = this.password
)
