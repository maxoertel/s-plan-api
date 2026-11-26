package de.sauter_gmbh.s_plan_api.mission_domain.model

data class SiteSummary(
  val id: Long,
  val name: String?,
  val latitude: Double?,
  val longitude: Double?,
  val radiusInMeter: Int?,
  val description1: String?,
  val description2: String?,
  val description3: String?,
  val description4: String?,
  val matchCode: String?,
  val street: String?,
  val houseNr: String?,
  val city: String?,
  val zip: String?,
  val country: String?,
  val phone: String?,
  val deliveryNoteInfo: String?,
)
