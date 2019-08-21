package ru.prsolution.winstrike.domain.models.orders

data class ComputerModel(
    val active: Boolean,
    val createAt: String,
    val name: String,
    val publicId: String,
    val roomPid: String
)

