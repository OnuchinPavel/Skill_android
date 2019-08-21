package ru.prsolution.winstrike.datasource.model.arena

import com.squareup.moshi.Json
import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.domain.models.arena.*

class SchemaEntity(
    @field:Json(name = "room_layout")
    val roomLayout: ArenaSchemaEntity?
)

class ArenaSchemaEntity(
    @field:Json(name = "name")
    val name: String?,

    @field:Json(name = "create_at")
    val createAt: String?,

    @field:Json(name = "labels")
    val labels: List<LabelEntity>?,

    @field:Json(name = "places")
    val places: List<SeatEntity>?,

    @field:Json(name = "room_pid")
    val roomPid: String?,

    @field:Json(name = "public_id")
    val publicId: String?,

    @field:Json(name = "walls")
    val walls: List<WallEntity>?

)

class SeatEntity(

    @field:Json(name = "offer_pid")
    val offerPid: String?,

    @field:Json(name = "is_hidden")
    val isHidden: Boolean?,

    @field:Json(name = "computer")
    val computer: ComputerEntity?,

    @field:Json(name = "public_id")
    val publicId: String?,

    @field:Json(name = "offer")
    val offer: OfferEntity?,

    @field:Json(name = "computer_pid")
    val computerPid: String?,

    @field:Json(name = "room_layout_pid")
    val roomLayoutPid: String?,

    @field:Json(name = "name")
    val name: String?,

    @field:Json(name = "create_at")
    val createAt: String?,

    @field:Json(name = "coors")
    val coors: CoorsEntity?,

    @field:Json(name = "status")
    val status: String?
)

class ComputerEntity(

    @field:Json(name = "active")
    val active: Boolean?,

    @field:Json(name = "name")
    val name: String?,

    @field:Json(name = "public_id")
    val publicId: String?,

    @field:Json(name = "create_at")
    val createAt: String?
)

class OfferEntity(

    @field:Json(name = "name")
    val name: String?,

    @field:Json(name = "cost")
    val cost: Int?,

    @field:Json(name = "public_id")
    val publicId: String?,

    @field:Json(name = "create_at")
    val createAt: String?
)

class CoorsEntity(

    @field:Json(name = "id")
    val id: String?,

    @field:Json(name = "angle")
    val angle: Double?,

    @field:Json(name = "type")
    val type: Int?,

    @field:Json(name = "x")
    val x: Int?,

    @field:Json(name = "y")
    val y: Int?,

    @field:Json(name = "xn")
    val xn: Int?,

    @field:Json(name = "yn")
    val yn: Int?
)

class LabelEntity(

    @field:Json(name = "text")
    val text: String?,

    @field:Json(name = "x")
    val x: Int?,

    @field:Json(name = "y")
    val y: Int?
)

class WallEntity(

    @field:Json(name = "start")
    val start: StartEntity?,

    @field:Json(name = "end")
    val end: EndEntity?
)

class StartEntity(

    @field:Json(name = "x")
    val x: Int?,

    @field:Json(name = "y")
    val y: Int?
)

class EndEntity(

    @field:Json(name = "x")
    val x: Int?,

    @field:Json(name = "y")
    val y: Int?
)

fun ComputerEntity.mapToDomain(): Computer =
    Computer(active, name, publicId, createAt)

fun OfferEntity.mapToDomain(): Offer =
    Offer(name, cost, publicId, createAt)

fun List<SeatEntity>.mapToDomain(): List<Seat> = map { it.mapToDomain() }

fun StartEntity.mapToDomain(): Start =
    Start(x, y)

fun EndEntity.mapToDomain(): End =
    End(x, y)

fun WallEntity.mapToDomain(): Wall =
    Wall(start?.mapToDomain(), end?.mapToDomain())

fun List<WallEntity>.mapWallsToDomain(): List<Wall> = map { it.mapToDomain() }

fun CoorsEntity.mapToDomain(): Coors =
    Coors(id, angle, type, x, y, xn, yn)

fun SeatEntity.mapToDomain(): Seat =
    Seat(
        offerPid,
        isHidden,
        computer?.mapToDomain(),
        publicId,
        offer?.mapToDomain(),
        computerPid,
        roomLayoutPid,
        name,
        createAt,
        coors?.mapToDomain(),
        status
    )

fun LabelEntity.mapToDomain(): Label =
    Label(text, x, y)

fun List<LabelEntity>.mapLabelsToDomain(): List<Label> = map { it.mapToDomain() }

fun ArenaSchemaEntity.mapRoomToDomain(): ArenaSchema =
    ArenaSchema(
        name = name,
        roomPid = roomPid,
        createAt = createAt,
        publicId = publicId,
        seats = places?.mapToDomain(),
        walls = walls?.mapWallsToDomain(),
        labels = labels?.mapLabelsToDomain()
    )


fun Resource<SchemaEntity>.mapToDomain(): Resource<ArenaSchema> = Resource<ArenaSchema>(
    state = state,
    data = data?.roomLayout?.mapRoomToDomain(),
    message = message
)
