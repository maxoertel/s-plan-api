package de.sauter_gmbh.s_plan_api.location_infrastructure.datasource.database.disposition

import de.sauter_gmbh.s_plan_api.infra_db.condition.ConditionalOnAppMode
import de.sauter_gmbh.s_plan_api.location_infrastructure.datasource.database.SiteDataSource
import de.sauter_gmbh.s_plan_api.location_infrastructure.datasource.entity.SiteEntity
import de.sauter_gmbh.s_plan_api.location_infrastructure.datasource.entity.SiteKeyEntity
import de.sauter_gmbh.s_plan_api.location_infrastructure.datasource.entity.SitePositionEntity
import de.sauter_gmbh.s_plan_api.shared_core.mode.AppMode
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component

@Component
@ConditionalOnAppMode(AppMode.S_PLAN)
internal class DispositionSiteDataSource(private val jdbcTemplate: JdbcTemplate) : SiteDataSource {

  private val rowMapper = RowMapper { rs, _ ->
    SiteEntity(
      customerNr = rs.getLong("kundennr"),
      siteNr = rs.getLong("baustnr"),
      matchCode = rs.getString("matchcode"),
      description1 = rs.getString("bezeichnung1"),
      description2 = rs.getString("bezeichnung2"),
      description3 = rs.getString("bezeichnung3"),
      description4 = rs.getString("bezeichnung4"),
      street = rs.getString("strasse"),
      houseNumber = rs.getString("hausnummer"),
      zip = rs.getString("plz"),
      city = rs.getString("ort"),
      country = rs.getString("land"),
      phone = rs.getString("telefon"),
      mobile = rs.getString("mobil"),
      fax = rs.getString("fax"),
      contact = rs.getString("ansprechpartner"),
      note = rs.getString("bemerkung"),
      internalInfo = rs.getString("interneinfo"),
      deliveryNoteInfo = rs.getString("lsinfo"),
      latitude = rs.getString("baustellenlatitude"),
      longitude = rs.getString("baustellenlongitude"),
      radiusInMeter = rs.getObject("radiusumbaustelleinmeter") as? Int,
      factoryNr = rs.getObject("baustellewerksnummer") as? Long,
      externalId = rs.getString("externeid"),
      isActive = rs.getObject("baustelleaktiv") as? Boolean,
      isCompleted = rs.getObject("baustelleabgeschlossen") as? Boolean,
      isDeleted = rs.getObject("baustellegeloescht") as? Boolean,
      releaseFrom = rs.getTimestamp("baustellefreigabevon")?.toString(),
      releaseUntil = rs.getTimestamp("baustellefreigabebis")?.toString(),
      firstDelivery = rs.getTimestamp("erstebelieferung")?.toString(),
      lastDelivery = rs.getDate("letztebelieferung")?.toLocalDate()?.toString(),
      createdAt = rs.getTimestamp("baustelleerstellungdatumzeit")?.toString(),
      updatedAt = rs.getTimestamp("baustelleletzteaenderungdatumzeit")?.toString(),
    )
  }

  private val selectClause =
    """
    SELECT kundennr, baustnr, matchcode, bezeichnung1, bezeichnung2, bezeichnung3, bezeichnung4,
           strasse, hausnummer, plz, ort, land, telefon, mobil, fax, ansprechpartner, bemerkung,
           interneinfo, lsinfo, baustellenlatitude, baustellenlongitude, radiusumbaustelleinmeter,
           baustellewerksnummer, externeid, baustelleaktiv, baustelleabgeschlossen,
           baustellegeloescht, baustellefreigabevon, baustellefreigabebis, erstebelieferung,
           letztebelieferung, baustelleerstellungdatumzeit, baustelleletzteaenderungdatumzeit
    FROM baust
    """
      .trimIndent()

  override fun findAll(): List<SiteEntity> = jdbcTemplate.query(selectClause, rowMapper)

  // A site number alone is ambiguous: it repeats across customers. Both key columns must be matched
  // together, otherwise the same site number pulls in every customer's site.
  override fun findBySiteKeys(siteKeys: Collection<SiteKeyEntity>): List<SiteEntity> {
    if (siteKeys.isEmpty()) return emptyList()
    val placeholders = siteKeys.joinToString(",") { "(?, ?)" }
    val arguments = siteKeys.flatMap { listOf(it.customerNr, it.siteNr) }
    return jdbcTemplate.query(
      "$selectClause WHERE (kundennr, baustnr) IN ($placeholders)",
      rowMapper,
      *arguments.toTypedArray(),
    )
  }

  override fun updatePosition(siteKey: SiteKeyEntity, position: SitePositionEntity): Int =
    jdbcTemplate.update(
      """
      UPDATE baust
      SET baustellenlatitude = ?, baustellenlongitude = ?, radiusumbaustelleinmeter = ?
      WHERE kundennr = ? AND baustnr = ?
      """
        .trimIndent(),
      position.latitude,
      position.longitude,
      position.radiusInMeter,
      siteKey.customerNr,
      siteKey.siteNr,
    )
}
