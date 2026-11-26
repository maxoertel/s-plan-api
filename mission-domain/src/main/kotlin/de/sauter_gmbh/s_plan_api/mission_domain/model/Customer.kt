package de.sauter_gmbh.s_plan_api.mission_domain.model

data class Customer(
  val nr: String?,
  val matchCode: String?,
  val name1: String?,
  val name2: String?,
  val name3: String?,
  val name4: String?,
  val street: String?,
  val houseNr: String?,
  val country: String?,
  val zip: String?,
  val city: String?,
  val phone: String?,
  val fax: String?,
  val mobile: String?,
  val email1: String?,
  val email2: String?,
  val note: String?,
)
