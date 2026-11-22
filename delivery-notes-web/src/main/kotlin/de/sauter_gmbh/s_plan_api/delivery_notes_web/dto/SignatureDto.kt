package de.sauter_gmbh.s_plan_api.delivery_notes_web.dto

import com.fasterxml.jackson.annotation.JsonProperty

internal data class SignatureDto(
  @JsonProperty("device_id") val deviceId: String?,
  @JsonProperty("status") val status: String?,
  @JsonProperty("driver_signature_set") val driverSignatureSet: Boolean,
  @JsonProperty("driver_signature_placeholder") val driverSignaturePlaceholder: Boolean,
  @JsonProperty("driver_signature_timestamp_millis") val driverSignatureTimestampMillis: Long?,
  @JsonProperty("customer_signature_set") val customerSignatureSet: Boolean,
  @JsonProperty("customer_signature_timestamp_millis") val customerSignatureTimestampMillis: Long?,
  @JsonProperty("customer_signature_placeholder") val customerSignaturePlaceholder: Boolean,
  @JsonProperty("customer_signature_collective") val customerSignatureCollective: Boolean,
)
