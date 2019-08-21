package ru.prsolution.winstrike.domain.models.arena

class ArenaSchema(
    val name: String? = null,
    val roomPid: String? = null,
    val createAt: String? = null,
    val publicId: String? = null,
    val seats: List<Seat>? = null,
    val walls: List<Wall>? = null,
    val labels: List<Label>? = null
)

class Seat(
    val offerPid: String? = null,
    val isHidden: Boolean? = null,
    val computer: Computer? = null,
    val publicId: String? = null,
    val offer: Offer? = null,
    val computerPid: String? = null,
    val roomLayoutPid: String? = null,
    val name: String? = null,
    val createAt: String? = null,
    val coors: Coors? = null,
    val status: String? = null
)

class Computer(
    val active: Boolean? = null,
    val name: String? = null,
    val publicId: String? = null,
    val createAt: String? = null
)

class Coors(
    val id: String? = null,
    val angle: Double? = null,
    val type: Int? = null,
    val x: Int? = null,
    val y: Int? = null,
    val xn: Int? = null,
    val yn: Int? = null
)

class Offer(
    val name: String? = null,
    val cost: Int? = null,
    val publicId: String? = null,
    val createAt: String? = null
)

class Wall(
    val start: Start? = null,
    val end: End? = null
)

class Start(
    val x: Int? = null,
    val y: Int? = null
)

class End(
    val x: Int? = null,
    val y: Int? = null
)
