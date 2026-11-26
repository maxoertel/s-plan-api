package de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.datasource.database

import de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.datasource.entity.DeliveryNotePdfEntity
import java.time.LocalDate
import java.time.LocalTime

internal interface DeliveryNotePdfDataSource {
  fun findAllByDeliveryNote(
    factoryNr: Long,
    date: LocalDate,
    time: LocalTime,
    deliveryNoteNr: Int
  ): List<DeliveryNotePdfEntity>

  fun findBytes(
    factoryNr: Long,
    date: LocalDate,
    time: LocalTime,
    deliveryNoteNr: Int,
    deliveryNoteCircle: Int,
    creationDate: LocalDate,
    creationTime: LocalTime,
    storageDate: LocalDate,
    storageTime: LocalTime
  ): ByteArray?
}
