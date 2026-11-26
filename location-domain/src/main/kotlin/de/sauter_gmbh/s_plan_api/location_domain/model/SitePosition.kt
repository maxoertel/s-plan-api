package de.sauter_gmbh.s_plan_api.location_domain.model

data class SitePosition(
  val latitude: Double,
  val longitude: Double,
  val radiusInMeter: Int,
)
