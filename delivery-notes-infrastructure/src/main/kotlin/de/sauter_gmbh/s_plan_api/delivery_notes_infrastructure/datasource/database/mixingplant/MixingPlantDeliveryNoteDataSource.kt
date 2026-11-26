package de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.datasource.database.mixingplant

import de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.datasource.database.DeliveryNoteDataSource
import de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.datasource.entity.DeliveryNoteEntity
import de.sauter_gmbh.s_plan_api.infra_db.condition.ConditionalOnAppMode
import de.sauter_gmbh.s_plan_api.shared_core.mode.AppMode
import java.time.LocalDate
import java.time.LocalTime
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

private const val NOT_IMPLEMENTED_MESSAGE =
  "Delivery note data source is not implemented for mixing plant database system"

@Component
@ConditionalOnAppMode(AppMode.S_WORK)
internal class MixingPlantDeliveryNoteDataSource(private val jdbcTemplate: JdbcTemplate) :
  DeliveryNoteDataSource {

  override fun findDriverSignatureJpeg(deliveryNoteId: String): ByteArray? =
    throw NotImplementedError(NOT_IMPLEMENTED_MESSAGE)

  override fun findCustomerSignatureJpeg(deliveryNoteId: String): ByteArray? =
    throw NotImplementedError(NOT_IMPLEMENTED_MESSAGE)

  override fun findById(deliveryNoteId: String): DeliveryNoteEntity? =
    throw NotImplementedError(NOT_IMPLEMENTED_MESSAGE)

  override fun updateStatusTimes(deliveryNoteId: String, columnTimes: Map<String, LocalTime>) =
    throw NotImplementedError(NOT_IMPLEMENTED_MESSAGE)

  override fun findByDate(date: LocalDate): List<DeliveryNoteEntity> =
    throw NotImplementedError(NOT_IMPLEMENTED_MESSAGE)
}
