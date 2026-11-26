package de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.repository

import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.DeliveryNote
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.DeliveryNoteId
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.DeliveryNotePdf
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.DeliveryNotePdfId
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.StatusUpdate
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.repository.DeliveryNoteRepository
import de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.datasource.database.DeliveryNoteDataSource
import de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.datasource.database.DeliveryNotePdfDataSource
import de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.mapper.toColumnTimes
import de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.mapper.toModel
import de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.parser.DeliveryNoteIdParser
import de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.parser.DeliveryNotePdfIdParser
import de.sauter_gmbh.s_plan_api.infra_db.formatter.DATE_ID_FORMATTER
import de.sauter_gmbh.s_plan_api.infra_db.formatter.TIME_ID_FORMATTER
import java.time.LocalDate
import java.time.LocalTime
import org.springframework.stereotype.Repository

@Repository
internal class DeliveryNoteRepositoryImpl(
  private val deliveryNoteDataSource: DeliveryNoteDataSource,
  private val pdfDataSource: DeliveryNotePdfDataSource,
  private val deliveryNoteIdParser: DeliveryNoteIdParser,
  private val pdfIdParser: DeliveryNotePdfIdParser,
) : DeliveryNoteRepository {

  override fun findByDate(date: LocalDate): List<DeliveryNote> =
    deliveryNoteDataSource.findByDate(date).map { it.toModel() }

  override fun findById(deliveryNoteId: DeliveryNoteId): DeliveryNote? =
    deliveryNoteDataSource.findById(deliveryNoteId.value)?.toModel()

  override fun findDriverSignatureJpeg(deliveryNoteId: String): ByteArray? =
    deliveryNoteDataSource.findDriverSignatureJpeg(deliveryNoteId)

  override fun findCustomerSignatureJpeg(deliveryNoteId: String): ByteArray? =
    deliveryNoteDataSource.findCustomerSignatureJpeg(deliveryNoteId)

  override fun findPdfsByDeliveryNote(deliveryNoteId: DeliveryNoteId): List<DeliveryNotePdf> {
    val parsed = deliveryNoteIdParser.parse(deliveryNoteId.value)
    val date = LocalDate.parse(parsed.date, DATE_ID_FORMATTER)
    val time = LocalTime.parse(parsed.time, TIME_ID_FORMATTER)
    return pdfDataSource
      .findAllByDeliveryNote(
        factoryNr = parsed.factoryNr,
        date = date,
        time = time,
        deliveryNoteNr = parsed.deliveryNoteNr,
      )
      .map { it.toModel(pdfIdParser) }
  }

  override fun findPdfBytes(pdfId: DeliveryNotePdfId): ByteArray? {
    val parsed = pdfIdParser.parse(pdfId)
    return pdfDataSource.findBytes(
      factoryNr = parsed.factoryNr,
      date = parsed.date,
      time = parsed.time,
      deliveryNoteNr = parsed.deliveryNoteNr,
      deliveryNoteCircle = parsed.deliveryNoteCircle,
      creationDate = parsed.creationDate,
      creationTime = parsed.creationTime,
      storageDate = parsed.storageDate,
      storageTime = parsed.storageTime,
    )
  }

  override fun updateStatusTimes(deliveryNoteId: DeliveryNoteId, update: StatusUpdate) {
    deliveryNoteDataSource.updateStatusTimes(deliveryNoteId.value, update.toColumnTimes())
  }
}
