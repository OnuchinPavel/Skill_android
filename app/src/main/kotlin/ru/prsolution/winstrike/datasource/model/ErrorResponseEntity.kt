package ru.prsolution.winstrike.datasource.model

import com.squareup.moshi.Json

data class ErrorResponseEntity(
    @field:Json(name = "message")
    override val message: String
): ErrorResponse