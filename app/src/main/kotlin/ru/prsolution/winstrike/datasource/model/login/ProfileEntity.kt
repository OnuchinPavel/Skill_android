package ru.prsolution.winstrike.datasource.model.login

import com.squareup.moshi.Json

/**
 * Created by oleg on 07/03/2018.
 */

class ProfileEntity(

    @field:Json(name = "phone")
    val phone: String?,

    @field:Json(name = "name")
    val name: String?

/*    @field:Json(name = "password")
    val password: String?*/

)

