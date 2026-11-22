package de.sauter_gmbh.s_plan_api.location_infrastructure.mapper

import de.sauter_gmbh.s_plan_api.infra_db.parser.CoordinateKind
import de.sauter_gmbh.s_plan_api.infra_db.parser.parseLegacyCoordinate
import de.sauter_gmbh.s_plan_api.location_domain.model.Factory
import de.sauter_gmbh.s_plan_api.location_infrastructure.datasource.entity.FactoryEntity

internal fun FactoryEntity.toModel(): Factory =
  Factory(
    id = id,
    matchCode = matchCode,
    name = name,
    street = street,
    houseNumber = houseNumber,
    zip = zip,
    city = city,
    country = country,
    phone = phone,
    latitude = parseLegacyCoordinate(latitude, CoordinateKind.LATITUDE),
    longitude = parseLegacyCoordinate(longitude, CoordinateKind.LONGITUDE),
    radiusInMeter = radiusInMeter,
  )
