package de.sauter_gmbh.s_plan_api.delivery_notes_domain.model

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@JvmInline
value class DeliveryNoteId(val value: String) {

  companion object {
    private val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
    private val TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("HHmmss")

    fun from(
      factoryNr: Long,
      deliveryNoteNr: Int,
      date: LocalDate,
      time: LocalTime
    ): DeliveryNoteId =
      DeliveryNoteId(
        "${factoryNr}_${deliveryNoteNr}_${date.format(DATE_FORMATTER)}_${time.format(TIME_FORMATTER)}"
      )
  }
}
