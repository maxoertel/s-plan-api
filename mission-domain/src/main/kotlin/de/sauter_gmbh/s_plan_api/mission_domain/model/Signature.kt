package de.sauter_gmbh.s_plan_api.mission_domain.model

data class Signature(
  val driverSignatureSet: Boolean = false,
  val driverSignatureTimestampMillis: Long? = null,
  val customerSignatureSet: Boolean = false,
  val customerSignatureTimestampMillis: Long? = null,
)
