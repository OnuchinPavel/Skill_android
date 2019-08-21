package ru.prsolution.winstrike.datasource.model.login

import com.squareup.moshi.Json
import ru.prsolution.winstrike.domain.models.login.NewUserModel

/**
 * Created by oleg on 07/03/2018.
 */

class NewUserEntity(
    @field:Json(name = "phone")
    val phone: String?,
    val password: String
)

fun NewUserEntity.maptoDomain(): NewUserModel = NewUserModel(
    phone = this.phone,
    password = this.password
)
