package de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.datasource.database

import de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.datasource.entity.DeliveryNoteEntity
import java.time.LocalDate
import java.time.LocalTime

internal interface DeliveryNoteDataSource {
  fun findDriverSignatureJpeg(deliveryNoteId: String): ByteArray?

  fun findCustomerSignatureJpeg(deliveryNoteId: String): ByteArray?

  fun findById(deliveryNoteId: String): DeliveryNoteEntity?

  fun updateStatusTimes(deliveryNoteId: String, columnTimes: Map<String, LocalTime>)

  fun findByDate(date: LocalDate): List<DeliveryNoteEntity>
}
