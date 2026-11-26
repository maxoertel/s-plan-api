package de.sauter_gmbh.s_plan_api.location_infrastructure.mapper

import de.sauter_gmbh.s_plan_api.location_domain.model.SitePosition
import de.sauter_gmbh.s_plan_api.location_infrastructure.datasource.entity.SitePositionEntity

internal fun SitePosition.toEntity(): SitePositionEntity =
  SitePositionEntity(
    latitude = latitude.toString(),
    longitude = longitude.toString(),
    radiusInMeter = radiusInMeter,
  )
