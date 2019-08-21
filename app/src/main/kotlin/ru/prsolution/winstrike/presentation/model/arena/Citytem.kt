package ru.prsolution.winstrike.presentation.model.arena

import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.domain.models.city.City

data class CityItem(
        val id: String,
        val name: String
)

/**
 * Presentation layer should be responsible of mapping the domain model to an
 * appropriate presentation model and the presentation model to a domain model if needed.
 *
 * This is because domain should contain only business logic
 * and shouldn't know at all about presentation or data layers.
 */

fun List<City>.mapToPresentation(): List<CityItem> =
        map { CityItem(it.publicId, it.name) }



fun Resource<List<City>>.mapToPresentation(): Resource<List<CityItem>> = Resource<List<CityItem>>(
        state = state,
        data = data?.mapToPresentation(),
        message = message
)

