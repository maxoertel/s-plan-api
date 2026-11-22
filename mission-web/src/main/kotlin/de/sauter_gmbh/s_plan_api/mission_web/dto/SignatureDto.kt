package de.sauter_gmbh.s_plan_api.mission_web.dto

import com.fasterxml.jackson.annotation.JsonProperty

internal data class SignatureDto(
  @JsonProperty("driver_signature_set") val driverSignatureSet: Boolean,
  @JsonProperty("driver_signature_timestamp_millis") val driverSignatureTimestampMillis: Long?,
  @JsonProperty("customer_signature_set") val customerSignatureSet: Boolean,
  @JsonProperty("customer_signature_timestamp_millis") val customerSignatureTimestampMillis: Long?,
)
