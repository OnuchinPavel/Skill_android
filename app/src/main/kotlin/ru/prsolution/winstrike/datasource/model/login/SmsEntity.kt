package ru.prsolution.winstrike.datasource.model.login

import com.squareup.moshi.Json

class SmsEntity(
    @field:Json(name = "username")
    var phone: String?)
