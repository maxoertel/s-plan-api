package de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.parser

import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.DeliveryNotePdfId
import de.sauter_gmbh.s_plan_api.infra_db.formatter.DATE_ID_FORMATTER
import de.sauter_gmbh.s_plan_api.infra_db.formatter.TIME_ID_FORMATTER
import de.sauter_gmbh.s_plan_api.infra_db.formatter.TIME_MICROS_ID_FORMATTER
import java.time.LocalDate
import java.time.LocalTime
import org.springframework.stereotype.Component

data class ParsedDeliveryNotePdfId(
  val factoryNr: Long,
  val date: LocalDate,
  val time: LocalTime,
  val deliveryNoteNr: Int,
  val deliveryNoteCircle: Int,
  val creationDate: LocalDate,
  val creationTime: LocalTime,
  val storageDate: LocalDate,
  val storageTime: LocalTime,
)

@Component
internal class DeliveryNotePdfIdParser {

  private val datePattern = Regex("^\\d{8}$")
  private val timePattern = Regex("^\\d{6}$")

  // Creation and storage times carry microseconds (`HHmmssSSSSSS`) because the legacy system
  // stores them with fractional seconds and two pdfs may share the same whole second. Plain
  // `HHmmss` is still accepted for ids issued before microseconds were part of the id.
  private val preciseTimePattern = Regex("^\\d{6}(\\d{6})?$")

  fun parse(pdfId: DeliveryNotePdfId): ParsedDeliveryNotePdfId {
    val raw = pdfId.value
    val parts = raw.split("_")
    require(parts.size == 9) { "Invalid delivery note pdf id format: $raw" }
    val date = parts[1]
    val time = parts[2]
    val creationDate = parts[5]
    val creationTime = parts[6]
    val storageDate = parts[7]
    val storageTime = parts[8]
    require(datePattern.matches(date)) { "Invalid date in delivery note pdf id: $raw" }
    require(timePattern.matches(time)) { "Invalid time in delivery note pdf id: $raw" }
    require(datePattern.matches(creationDate)) {
      "Invalid creation date in delivery note pdf id: $raw"
    }
    require(preciseTimePattern.matches(creationTime)) {
      "Invalid creation time in delivery note pdf id: $raw"
    }
    require(datePattern.matches(storageDate)) {
      "Invalid storage date in delivery note pdf id: $raw"
    }
    require(preciseTimePattern.matches(storageTime)) {
      "Invalid storage time in delivery note pdf id: $raw"
    }
    return ParsedDeliveryNotePdfId(
      factoryNr = parts[0].toLong(),
      date = LocalDate.parse(date, DATE_ID_FORMATTER),
      time = LocalTime.parse(time, TIME_ID_FORMATTER),
      deliveryNoteNr = parts[3].toInt(),
      deliveryNoteCircle = parts[4].toInt(),
      creationDate = LocalDate.parse(creationDate, DATE_ID_FORMATTER),
      creationTime = parsePreciseTime(creationTime),
      storageDate = LocalDate.parse(storageDate, DATE_ID_FORMATTER),
      storageTime = parsePreciseTime(storageTime),
    )
  }

  fun format(
    factoryNr: Long,
    date: LocalDate,
    time: LocalTime,
    deliveryNoteNr: Int,
    deliveryNoteCircle: Int,
    creationDate: LocalDate,
    creationTime: LocalTime,
    storageDate: LocalDate,
    storageTime: LocalTime,
  ): DeliveryNotePdfId =
    DeliveryNotePdfId(
      "${factoryNr}_${date.format(DATE_ID_FORMATTER)}_${time.format(TIME_ID_FORMATTER)}" +
        "_${deliveryNoteNr}_${deliveryNoteCircle}" +
        "_${creationDate.format(DATE_ID_FORMATTER)}_${creationTime.format(TIME_MICROS_ID_FORMATTER)}" +
        "_${storageDate.format(DATE_ID_FORMATTER)}_${storageTime.format(TIME_MICROS_ID_FORMATTER)}"
    )

  private fun parsePreciseTime(raw: String): LocalTime =
    if (raw.length == PLAIN_TIME_LENGTH) LocalTime.parse(raw, TIME_ID_FORMATTER)
    else LocalTime.parse(raw, TIME_MICROS_ID_FORMATTER)

  companion object {
    private const val PLAIN_TIME_LENGTH = 6
  }
}
