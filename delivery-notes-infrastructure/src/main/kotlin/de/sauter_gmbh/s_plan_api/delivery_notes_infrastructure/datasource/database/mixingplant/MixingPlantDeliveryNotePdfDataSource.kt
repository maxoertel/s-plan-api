package de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.datasource.database.mixingplant

import de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.datasource.database.DeliveryNotePdfDataSource
import de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.datasource.entity.DeliveryNotePdfEntity
import de.sauter_gmbh.s_plan_api.infra_db.condition.ConditionalOnAppMode
import de.sauter_gmbh.s_plan_api.shared_core.mode.AppMode
import java.time.LocalDate
import java.time.LocalTime
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

private const val NOT_IMPLEMENTED_MESSAGE =
  "Delivery note pdf data source is not implemented for mixing plant database system"

@Component
@ConditionalOnAppMode(AppMode.S_WORK)
internal class MixingPlantDeliveryNotePdfDataSource(private val jdbcTemplate: JdbcTemplate) :
  DeliveryNotePdfDataSource {

  override fun findAllByDeliveryNote(
    factoryNr: Long,
    date: LocalDate,
    time: LocalTime,
    deliveryNoteNr: Int
  ): List<DeliveryNotePdfEntity> = throw NotImplementedError(NOT_IMPLEMENTED_MESSAGE)

  override fun findBytes(
    factoryNr: Long,
    date: LocalDate,
    time: LocalTime,
    deliveryNoteNr: Int,
    deliveryNoteCircle: Int,
    creationDate: LocalDate,
    creationTime: LocalTime,
    storageDate: LocalDate,
    storageTime: LocalTime
  ): ByteArray? = throw NotImplementedError(NOT_IMPLEMENTED_MESSAGE)
}
