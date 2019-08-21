package ru.prsolution.winstrike.domain.models.login

import ru.prsolution.winstrike.datasource.model.login.ProfileEntity


/**
 * Created by oleg on 07/03/2018.
 */

class ProfileModel(
    val phone: String?,
    val name: String?
)

fun ProfileModel.mapToDataSource(): ProfileEntity = ProfileEntity(
    phone = this.phone,
    name = this.name
)
