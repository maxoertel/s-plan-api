package de.sauter_gmbh.s_plan_api.vehicle_infrastructure.datasource.database.disposition

import de.sauter_gmbh.s_plan_api.infra_db.DbTimestampConverter
import de.sauter_gmbh.s_plan_api.infra_db.condition.ConditionalOnAppMode
import de.sauter_gmbh.s_plan_api.shared_core.mode.AppMode
import de.sauter_gmbh.s_plan_api.vehicle_infrastructure.datasource.database.VehicleDataSource
import de.sauter_gmbh.s_plan_api.vehicle_infrastructure.datasource.entity.VehicleEntity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component

@Component
@ConditionalOnAppMode(AppMode.S_PLAN)
internal class DispositionVehicleDataSource(
  private val jdbcTemplate: JdbcTemplate,
  private val timestampConverter: DbTimestampConverter,
) : VehicleDataSource {

  private val rowMapper = RowMapper { rs, _ ->
    VehicleEntity(
      id = rs.getLong("fahrzeugnr"),
      factoryId = rs.getObject("werksnummer") as? Long,
      factoryName = rs.getString("werksname"),
      licensePlate = rs.getString("kennzeichen"),
      latitude = rs.getString("fahrzeuglatitude"),
      longitude = rs.getString("fahrzeuglongitude"),
      trackingId = rs.getString("ortungsid"),
      alias = rs.getString("trackeralias"),
      driverFirstName = rs.getString("fahrervorname"),
      driverLastName = rs.getString("fahrername"),
      lastSignalTimestamp =
        rs.getTimestamp("fahrzeugortungdatumzeit")?.let(timestampConverter::toEpochMillis),
      lastStatusId = rs.getObject("letzterfzstatus") as? Int,
      vehicleType = rs.getObject("fahrzeugart") as? Int,
      fleetGroupNr = rs.getObject("fahrzeugfuhrparkgruppennr") as? Int,
      fleetGroupName = rs.getString("fuhrparkgruppenname"),
      loadCapacity = rs.getObject("ladekapazitaet") as? Double,
      createdAt =
        rs.getTimestamp("fahrzeugerstellungdatumzeit")?.let(timestampConverter::toEpochMillis),
      updatedAt =
        rs.getTimestamp("fahrzeugletzteaenderungdatumzeit")?.let(timestampConverter::toEpochMillis),
    )
  }

  private val selectClause =
    """
    SELECT f.fahrzeugnr, f.werksnummer, f.kennzeichen, f.fahrzeuglatitude, f.fahrzeuglongitude,
           f.ortungsid, f.trackeralias, f.fahrervorname, f.fahrername, f.fahrzeugortungdatumzeit,
           f.letzterfzstatus, f.fahrzeugart, f.fahrzeugfuhrparkgruppennr, f.ladekapazitaet,
           f.fahrzeugerstellungdatumzeit, f.fahrzeugletzteaenderungdatumzeit,
           w.werksname, fg.fahrzeugfuhrparkgruppenbezeichnung AS fuhrparkgruppenname
    FROM fahrzeug f
    LEFT JOIN werke w ON f.werksnummer = w.werksnummer
    LEFT JOIN fahrzeugfuhrparkgruppen fg ON f.fahrzeugfuhrparkgruppennr = fg.fahrzeugfuhrparkgruppennr
    """
      .trimIndent()

  override fun findAll(): List<VehicleEntity> = jdbcTemplate.query(selectClause, rowMapper)

  override fun findById(id: Long): VehicleEntity? =
    jdbcTemplate.query("$selectClause WHERE f.fahrzeugnr = ?", rowMapper, id).firstOrNull()

  override fun update(
    id: Long,
    licensePlate: String?,
    alias: String?,
    vehicleType: Int?,
    loadCapacity: Double?,
    updatedBy: String,
    timestampMillis: Long,
  ): Int {
    val sql =
      """
      UPDATE fahrzeug
      SET kennzeichen = ?,
          trackeralias = ?,
          fahrzeugart = ?,
          ladekapazitaet = ?,
          fahrzeugletzteaenderungbediener = ?,
          fahrzeugletzteaenderungdatumzeit = ?
      WHERE fahrzeugnr = ?
      """
        .trimIndent()
    return jdbcTemplate.update(
      sql,
      licensePlate,
      alias,
      vehicleType,
      loadCapacity,
      updatedBy,
      timestampConverter.toTimestamp(timestampMillis),
      id,
    )
  }
}
