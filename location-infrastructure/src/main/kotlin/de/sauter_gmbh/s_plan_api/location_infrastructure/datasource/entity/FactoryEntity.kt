package de.sauter_gmbh.s_plan_api.location_infrastructure.datasource.entity

internal data class FactoryEntity(
  val id: Long,
  val matchCode: String?,
  val name: String?,
  val street: String?,
  val houseNumber: String?,
  val zip: String?,
  val city: String?,
  val country: String?,
  val phone: String?,
  val latitude: String?,
  val longitude: String?,
  val radiusInMeter: Int?,
)
