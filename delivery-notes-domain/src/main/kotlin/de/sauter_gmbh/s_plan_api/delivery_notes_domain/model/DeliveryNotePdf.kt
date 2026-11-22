package de.sauter_gmbh.s_plan_api.delivery_notes_domain.model

import java.time.LocalDate
import java.time.LocalTime

data class DeliveryNotePdf(
  val id: DeliveryNotePdfId,
  val factoryNr: Long,
  val date: LocalDate,
  val time: LocalTime,
  val deliveryNoteNr: Int,
  val deliveryNoteCircle: Int,
  val creationDate: LocalDate,
  val creationTime: LocalTime,
  val storageDate: LocalDate,
  val storageTime: LocalTime,
  val kind: DeliveryNotePdfKind,
  val waterCorrectionUnloading: Float? = null,
)
