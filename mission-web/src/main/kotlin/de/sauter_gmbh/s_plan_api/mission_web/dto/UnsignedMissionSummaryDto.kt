package de.sauter_gmbh.s_plan_api.mission_web.dto

import com.fasterxml.jackson.annotation.JsonProperty

internal data class UnsignedMissionSummaryDto(
  @JsonProperty("id") val id: String,
  @JsonProperty("driver_signature_set") val driverSignatureSet: Boolean,
  @JsonProperty("customer_signature_set") val customerSignatureSet: Boolean,
  @JsonProperty("customer_signature_collective") val customerSignatureCollective: Boolean,
  @JsonProperty("order_nr") val orderNr: Int,
  @JsonProperty("load_nr") val loadNr: Int,
  @JsonProperty("delivery_nr") val deliveryNr: Int,
  @JsonProperty("delivery_nr_external") val deliveryNrExternal: String?,
  @JsonProperty("date") val date: String,
  @JsonProperty("article_nr") val articleNr: String?,
  @JsonProperty("recipe") val recipe: String?,
  @JsonProperty("planned_quantity") val plannedQuantity: String?,
  @JsonProperty("actual_quantity") val actualQuantity: String?,
  @JsonProperty("load_quantity") val loadQuantity: String?,
  @JsonProperty("water_correction_unloading") val waterCorrectionUnloading: Float?,
  @JsonProperty("status") val status: StatusDto,
)
