package de.sauter_gmbh.s_plan_api.delivery_notes_domain.model

data class Site(
  val nr: Long?,
  val matchCode: String?,
  val description1: String?,
  val description2: String?,
  val description3: String?,
  val description4: String?,
  val street: String?,
  val houseNr: String?,
  val country: String?,
  val zip: String?,
  val city: String?,
  val contact: String?,
  val note: String?,
  val phone: String?,
  val mobile: String?,
  val latitude: Double? = null,
  val longitude: Double? = null,
)
