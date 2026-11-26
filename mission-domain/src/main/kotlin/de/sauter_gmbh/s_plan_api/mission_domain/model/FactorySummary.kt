package de.sauter_gmbh.s_plan_api.mission_domain.model

data class FactorySummary(
  val id: Long,
  val name: String?,
  val latitude: Double?,
  val longitude: Double?,
  val radiusInMeter: Int?,
  val street: String?,
  val houseNr: String?,
  val city: String?,
  val zip: String?,
  val country: String?,
  val phone: String?,
)
