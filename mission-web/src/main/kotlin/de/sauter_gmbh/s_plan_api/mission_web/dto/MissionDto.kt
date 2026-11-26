package de.sauter_gmbh.s_plan_api.mission_web.dto

import com.fasterxml.jackson.annotation.JsonProperty
import de.sauter_gmbh.s_plan_api.mission_domain.model.DeliveryType

internal data class MissionDto(
  @JsonProperty("id") val id: String,
  @JsonProperty("order_nr") val orderNr: Int,
  @JsonProperty("load_nr") val loadNr: Int,
  @JsonProperty("delivery_nr") val deliveryNr: Int,
  @JsonProperty("delivery_nr_external") val deliveryNrExternal: String?,
  @JsonProperty("date") val date: String,
  @JsonProperty("order_type") val orderType: String?,
  @JsonProperty("delivery_type") val deliveryType: DeliveryType?,
  @JsonProperty("article_nr") val articleNr: String?,
  @JsonProperty("recipe") val recipe: String?,
  @JsonProperty("planned_quantity") val plannedQuantity: String?,
  @JsonProperty("actual_quantity") val actualQuantity: String?,
  @JsonProperty("load_quantity") val loadQuantity: String?,
  @JsonProperty("note_1") val note1: String?,
  @JsonProperty("note_2") val note2: String?,
  @JsonProperty("vehicle_id") val vehicleId: String?,
  @JsonProperty("vehicle_license_plate") val vehicleLicensePlate: String?,
  @JsonProperty("driver") val driver: String?,
  @JsonProperty("driver_note") val driverNote: String?,
  @JsonProperty("driver_started_at") val driverStartedAt: Long?,
  @JsonProperty("driver_accepted_at") val driverAcceptedAt: Long?,
  @JsonProperty("time") val time: String?,
  @JsonProperty("dispatch_time") val dispatchTime: String?,
  @JsonProperty("delivery_time") val deliveryTime: String?,
  @JsonProperty("next_time") val nextTime: String?,
  @JsonProperty("factory_summary") val factorySummary: FactorySummaryDto,
  @JsonProperty("return_factory_summary") val returnFactorySummary: FactorySummaryDto?,
  @JsonProperty("site_summary") val siteSummary: SiteSummaryDto?,
  @JsonProperty("customer") val customer: CustomerDto,
  @JsonProperty("status") val status: StatusDto,
  @JsonProperty("signature") val signature: SignatureDto,
  @JsonProperty("water_correction_unloading") val waterCorrectionUnloading: Float?,
  @JsonProperty("super_plasticizer_unloading") val superPlasticizerUnloading: Float?,
  @JsonProperty("unsigned_previous_missions")
  val unsignedPreviousMissions: List<UnsignedMissionSummaryDto>,
  @JsonProperty("delivery_note_pdf_exists") val deliveryNotePdfExists: Boolean,
)
