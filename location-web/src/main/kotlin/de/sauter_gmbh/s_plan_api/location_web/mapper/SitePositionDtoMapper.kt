package de.sauter_gmbh.s_plan_api.location_web.mapper

import de.sauter_gmbh.s_plan_api.location_domain.model.SitePosition
import de.sauter_gmbh.s_plan_api.location_web.dto.UpdateSitePositionRequestDto

internal fun UpdateSitePositionRequestDto.toModel(): SitePosition =
  SitePosition(latitude = latitude, longitude = longitude, radiusInMeter = radiusInMeter)
