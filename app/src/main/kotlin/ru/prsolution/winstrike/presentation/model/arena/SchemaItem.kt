package ru.prsolution.winstrike.presentation.model.arena

import ru.prsolution.winstrike.domain.models.arena.ArenaSchema
import ru.prsolution.winstrike.domain.models.arena.Label
import ru.prsolution.winstrike.domain.models.arena.Seat
import ru.prsolution.winstrike.domain.models.arena.Wall

data class SchemaItem(
    val name: String? = null,
    val roomPid: String? = null,
    val createAt: String? = null,
    val publicId: String? = null,
    val seats: List<Seat>? = null,
    val walls: List<Wall>? = null,
    val labels: List<Label>? = null
)

/**
 * Presentation layer should be responsible of mapping the domain model to an
 * appropriate presentation model and the presentation model to a domain model if needed.
 *
 * This is because domain should contain only business logic
 * and shouldn't know at all about presentation or data layers.
 */


fun ArenaSchema.mapToPresentation(): SchemaItem =
    SchemaItem(
        name = name,
        roomPid = roomPid,
        createAt = createAt,
        publicId = publicId,
        seats = seats,
        walls = walls,
        labels = labels
    )

fun SchemaItem.mapToDomain(): ArenaSchema =
    ArenaSchema(
        name = name,
        roomPid = roomPid,
        createAt = createAt,
        publicId = publicId,
        seats = seats,
        walls = walls,
        labels = labels
    )
