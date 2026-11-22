package de.sauter_gmbh.s_plan_api.location_web.mapper

import de.sauter_gmbh.s_plan_api.location_domain.model.Factory
import de.sauter_gmbh.s_plan_api.location_web.dto.FactoryDto

internal fun Factory.toDto(): FactoryDto =
  FactoryDto(
    id = id,
    matchCode = matchCode,
    name = name,
    street = street,
    houseNumber = houseNumber,
    zip = zip,
    city = city,
    country = country,
    phone = phone,
    latitude = latitude,
    longitude = longitude,
    radiusInMeter = radiusInMeter,
  )
