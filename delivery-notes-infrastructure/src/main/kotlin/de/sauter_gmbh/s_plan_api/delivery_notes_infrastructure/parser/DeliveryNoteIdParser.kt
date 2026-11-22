package de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.parser

import org.springframework.stereotype.Component

data class ParsedDeliveryNoteId(
  val factoryNr: Long,
  val deliveryNoteNr: Int,
  val date: String,
  val time: String,
)

@Component
internal class DeliveryNoteIdParser {

  private val datePattern = Regex("^\\d{8}$")
  private val timePattern = Regex("^\\d{6}$")

  fun parse(deliveryNoteId: String): ParsedDeliveryNoteId {
    val parts = deliveryNoteId.split("_")
    require(parts.size == 4) { "Invalid delivery note id format: $deliveryNoteId" }
    val date = parts[2]
    val time = parts[3]
    require(datePattern.matches(date)) { "Invalid date in delivery note id: $deliveryNoteId" }
    require(timePattern.matches(time)) { "Invalid time in delivery note id: $deliveryNoteId" }
    return ParsedDeliveryNoteId(
      factoryNr = parts[0].toLong(),
      deliveryNoteNr = parts[1].toInt(),
      date = date,
      time = time,
    )
  }
}
