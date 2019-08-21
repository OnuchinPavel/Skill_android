package ru.prsolution.winstrike.datasource.model.login

import com.squareup.moshi.Json
import ru.prsolution.winstrike.domain.models.login.PasswordModel

class PasswordEntity(

    @field:Json(name = "username")
    var username: String?,

    @field:Json(name = "new_password")
    var new_password: String?
)

fun PasswordEntity.mapToDomain(): PasswordModel = PasswordModel(
    username = this.username,
    password = this.new_password
)
