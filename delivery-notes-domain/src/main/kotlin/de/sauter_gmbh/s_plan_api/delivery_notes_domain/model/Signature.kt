package de.sauter_gmbh.s_plan_api.delivery_notes_domain.model

data class Signature(
  val deviceId: String?,
  val status: String?,
  val driverSignatureSet: Boolean = false,
  val driverSignaturePlaceholder: Boolean = false,
  val driverSignatureTimestampMillis: Long? = null,
  val customerSignatureSet: Boolean = false,
  val customerSignatureTimestampMillis: Long? = null,
  val customerSignaturePlaceholder: Boolean = false,
  val customerSignatureCollective: Boolean = false,
)
