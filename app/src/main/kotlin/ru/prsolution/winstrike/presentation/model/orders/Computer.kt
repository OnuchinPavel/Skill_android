package ru.prsolution.winstrike.presentation.model.orders

data class Computer(
    val active: Boolean,
    val createAt: String = " ",
    val name: String = " ",
    val publicId: String = " ",
    val roomPid: String = " "
)