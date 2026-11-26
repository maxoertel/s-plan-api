package de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.datasource.entity

import java.time.LocalDate
import java.time.LocalTime

internal data class DeliveryNotePdfEntity(
  val factoryNr: Long,
  val date: LocalDate,
  val time: LocalTime,
  val deliveryNoteNr: Int,
  val deliveryNoteCircle: Int,
  val creationDate: LocalDate,
  val creationTime: LocalTime,
  val storageDate: LocalDate,
  val storageTime: LocalTime,
  val pdfBytes: ByteArray?,
  val waterCorrectionUnloading: Double? = null,
  val creationOperator: String? = null,
  val dataSource: String? = null,
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is DeliveryNotePdfEntity) return false
    return factoryNr == other.factoryNr &&
      date == other.date &&
      time == other.time &&
      deliveryNoteNr == other.deliveryNoteNr &&
      deliveryNoteCircle == other.deliveryNoteCircle &&
      creationDate == other.creationDate &&
      creationTime == other.creationTime &&
      storageDate == other.storageDate &&
      storageTime == other.storageTime &&
      pdfBytes.contentEqualsNullable(other.pdfBytes) &&
      waterCorrectionUnloading == other.waterCorrectionUnloading &&
      creationOperator == other.creationOperator &&
      dataSource == other.dataSource
  }

  override fun hashCode(): Int {
    var result = factoryNr.hashCode()
    result = 31 * result + date.hashCode()
    result = 31 * result + time.hashCode()
    result = 31 * result + deliveryNoteNr
    result = 31 * result + deliveryNoteCircle
    result = 31 * result + creationDate.hashCode()
    result = 31 * result + creationTime.hashCode()
    result = 31 * result + storageDate.hashCode()
    result = 31 * result + storageTime.hashCode()
    result = 31 * result + (pdfBytes?.contentHashCode() ?: 0)
    result = 31 * result + (waterCorrectionUnloading?.hashCode() ?: 0)
    result = 31 * result + (creationOperator?.hashCode() ?: 0)
    result = 31 * result + (dataSource?.hashCode() ?: 0)
    return result
  }

  private fun ByteArray?.contentEqualsNullable(other: ByteArray?): Boolean =
    when {
      this == null && other == null -> true
      this == null || other == null -> false
      else -> this.contentEquals(other)
    }
}
