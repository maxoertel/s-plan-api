package de.sauter_gmbh.s_plan_api.location_infrastructure.datasource.entity

internal data class SitePositionEntity(
  val latitude: String,
  val longitude: String,
  val radiusInMeter: Int,
)
