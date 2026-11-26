package de.sauter_gmbh.s_plan_api.delivery_notes_web.dto

import com.fasterxml.jackson.annotation.JsonProperty

internal data class DeliveryNotePdfDto(
  @JsonProperty("id") val id: String,
  @JsonProperty("factory_nr") val factoryNr: Long,
  @JsonProperty("date") val date: String,
  @JsonProperty("time") val time: String,
  @JsonProperty("delivery_note_nr") val deliveryNoteNr: Int,
  @JsonProperty("delivery_note_circle") val deliveryNoteCircle: Int,
  @JsonProperty("creation_date") val creationDate: String,
  @JsonProperty("creation_time") val creationTime: String,
  @JsonProperty("storage_date") val storageDate: String,
  @JsonProperty("storage_time") val storageTime: String,
  @JsonProperty("kind") val kind: String,
  @JsonProperty("water_correction_unloading") val waterCorrectionUnloading: Float?,
)
