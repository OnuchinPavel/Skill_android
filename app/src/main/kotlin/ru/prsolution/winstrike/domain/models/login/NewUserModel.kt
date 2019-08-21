package ru.prsolution.winstrike.domain.models.login

import ru.prsolution.winstrike.datasource.model.login.NewUserEntity

/**
 * Created by oleg on 07/03/2018.
 */

class NewUserModel(
    val phone: String?,
    val password: String
)

fun NewUserModel.mapToDataSource(): NewUserEntity = NewUserEntity(
    phone = this.phone,
    password = this.password
)
