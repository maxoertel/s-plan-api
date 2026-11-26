package de.sauter_gmbh.s_plan_api.user_management_infrastructure.datasource.database.disposition

import de.sauter_gmbh.s_plan_api.infra_db.DbTimestampConverter
import de.sauter_gmbh.s_plan_api.infra_db.condition.ConditionalOnAppMode
import de.sauter_gmbh.s_plan_api.shared_core.mode.AppMode
import de.sauter_gmbh.s_plan_api.user_management_infrastructure.datasource.database.UserDataSource
import de.sauter_gmbh.s_plan_api.user_management_infrastructure.datasource.database.entity.UserEntity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component

@Component
@ConditionalOnAppMode(AppMode.S_PLAN)
internal class DispositionUserDataSource(
  private val jdbcTemplate: JdbcTemplate,
  private val timestampConverter: DbTimestampConverter,
) : UserDataSource {

  private val rowMapper = RowMapper { rs, _ ->
    UserEntity(
      id = rs.getString("benutzer"),
      salutation = rs.getString("anrede"),
      title = rs.getString("titel"),
      firstName = rs.getString("vorname"),
      lastName = rs.getString("nachname"),
      authType = rs.getString("authentifizierungstyp"),
      authUser = rs.getString("authentifizierungsbenutzer"),
      createdAtMillis =
        rs.getTimestamp("erstellungdatumzeit")?.let(timestampConverter::toEpochMillis),
      createdBy = rs.getString("erstellungsbediener"),
      updatedAtMillis =
        rs.getTimestamp("letzteaenderungdatumzeit")?.let(timestampConverter::toEpochMillis),
      updatedBy = rs.getString("letzteaenderungbediener"),
      role = rs.getString("rolle"),
    )
  }

  private val selectClause =
    """
    SELECT benutzer, anrede, titel, vorname, nachname, authentifizierungstyp,
           authentifizierungsbenutzer, erstellungdatumzeit, erstellungsbediener,
           letzteaenderungdatumzeit, letzteaenderungbediener, rolle
    FROM sauter_strack_benutzer
    """
      .trimIndent()

  override fun findAll(): List<UserEntity> =
    jdbcTemplate.query("$selectClause ORDER BY benutzer", rowMapper)

  override fun findById(id: String): UserEntity? =
    jdbcTemplate.query("$selectClause WHERE benutzer = ?", rowMapper, id).firstOrNull()

  override fun insert(
    id: String,
    salutation: String?,
    title: String?,
    firstName: String?,
    lastName: String?,
    authType: String?,
    authUser: String?,
    role: String?,
    createdBy: String,
    timestampMillis: Long,
  ) {
    val sql =
      """
      INSERT INTO sauter_strack_benutzer (
        benutzer, anrede, titel, vorname, nachname, authentifizierungstyp,
        authentifizierungsbenutzer, rolle, erstellungdatumzeit, erstellungsbediener,
        letzteaenderungdatumzeit, letzteaenderungbediener
      ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
      """
        .trimIndent()
    val timestamp = timestampConverter.toTimestamp(timestampMillis)

    jdbcTemplate.update(
      sql,
      id,
      salutation,
      title,
      firstName,
      lastName,
      authType,
      authUser,
      role,
      timestamp,
      createdBy,
      timestamp,
      createdBy,
    )
  }

  override fun update(
    id: String,
    salutation: String?,
    title: String?,
    firstName: String?,
    lastName: String?,
    authType: String?,
    authUser: String?,
    role: String?,
    updatedBy: String,
    timestampMillis: Long,
  ): Int {
    val sql =
      """
      UPDATE sauter_strack_benutzer
      SET anrede = ?,
          titel = ?,
          vorname = ?,
          nachname = ?,
          authentifizierungstyp = ?,
          authentifizierungsbenutzer = ?,
          rolle = ?,
          letzteaenderungdatumzeit = ?,
          letzteaenderungbediener = ?
      WHERE benutzer = ?
      """
        .trimIndent()

    return jdbcTemplate.update(
      sql,
      salutation,
      title,
      firstName,
      lastName,
      authType,
      authUser,
      role,
      timestampConverter.toTimestamp(timestampMillis),
      updatedBy,
      id,
    )
  }

  override fun delete(id: String): Int =
    jdbcTemplate.update("DELETE FROM sauter_strack_benutzer WHERE benutzer = ?", id)
}
