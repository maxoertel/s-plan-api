package de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.datasource.database.disposition

import de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.datasource.database.DeliveryNotePdfDataSource
import de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.datasource.entity.DeliveryNotePdfEntity
import de.sauter_gmbh.s_plan_api.infra_db.condition.ConditionalOnAppMode
import de.sauter_gmbh.s_plan_api.infra_db.table.LegacyTableInitializer
import de.sauter_gmbh.s_plan_api.infra_db.table.LegacyTableNames
import de.sauter_gmbh.s_plan_api.shared_core.logger.logger
import de.sauter_gmbh.s_plan_api.shared_core.mode.AppMode
import java.sql.Date
import java.sql.Time
import java.time.LocalDate
import java.time.LocalTime
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component

@Component
@ConditionalOnAppMode(AppMode.S_PLAN)
internal class DispositionDeliveryNotePdfDataSource(
  private val jdbcTemplate: JdbcTemplate,
  private val legacyTableInitializer: LegacyTableInitializer,
) : DeliveryNotePdfDataSource {

  private val log by logger()

  private val rowMapper = RowMapper { rs, _ ->
    DeliveryNotePdfEntity(
      factoryNr = rs.getLong("werksnummer"),
      date = rs.getDate("datum").toLocalDate(),
      time = rs.getTime("zeit").toLocalTime(),
      deliveryNoteNr = rs.getInt("lsnummer"),
      deliveryNoteCircle = rs.getInt("lskreis"),
      creationDate = rs.getDate("erstellungdatum").toLocalDate(),
      creationTime = rs.getObject("erstellungzeit", LocalTime::class.java),
      storageDate = rs.getDate("ablagedatum").toLocalDate(),
      storageTime = rs.getObject("ablagezeit", LocalTime::class.java),
      pdfBytes = rs.getBytes("pdflieferschein"),
      waterCorrectionUnloading = rs.getObject("entladungwasserkorrektur") as? Double,
      dataSource = rs.getString("datenquelle"),
    )
  }

  private val metadataRowMapper = RowMapper { rs, _ ->
    DeliveryNotePdfEntity(
      factoryNr = rs.getLong("werksnummer"),
      date = rs.getDate("datum").toLocalDate(),
      time = rs.getTime("zeit").toLocalTime(),
      deliveryNoteNr = rs.getInt("lsnummer"),
      deliveryNoteCircle = rs.getInt("lskreis"),
      creationDate = rs.getDate("erstellungdatum").toLocalDate(),
      creationTime = rs.getObject("erstellungzeit", LocalTime::class.java),
      storageDate = rs.getDate("ablagedatum").toLocalDate(),
      storageTime = rs.getObject("ablagezeit", LocalTime::class.java),
      pdfBytes = null,
      waterCorrectionUnloading = rs.getObject("entladungwasserkorrektur") as? Double,
      dataSource = rs.getString("datenquelle"),
    )
  }

  override fun findAllByDeliveryNote(
    factoryNr: Long,
    date: LocalDate,
    time: LocalTime,
    deliveryNoteNr: Int,
  ): List<DeliveryNotePdfEntity> {
    val tableName = LegacyTableNames.deliveryNotePdfsTable(date)
    val deliveryNotesTable = LegacyTableNames.deliveryNotesTable(date)
    legacyTableInitializer.createIfNotExists(
      tableName,
      LegacyTableNames.DELIVERY_NOTE_PDFS_TEMPLATE
    )
    return try {
      jdbcTemplate.query(
        """
        SELECT p.werksnummer, p.datum, p.zeit, p.lsnummer, p.lskreis,
               p.erstellungdatum, p.erstellungzeit, p.ablagedatum, p.ablagezeit,
               p.datenquelle, ls.entladungwasserkorrektur
        FROM $tableName p
        LEFT JOIN $deliveryNotesTable ls
          ON ls.werksnummer = p.werksnummer AND ls.datum = p.datum
         AND ls.zeit = p.zeit AND ls.lsnummer = p.lsnummer
        WHERE p.werksnummer = ? AND p.datum = ? AND p.zeit = ? AND p.lsnummer = ?
        ORDER BY p.ablagedatum ASC, p.ablagezeit ASC
        """
          .trimIndent(),
        metadataRowMapper,
        factoryNr,
        Date.valueOf(date),
        Time.valueOf(time),
        deliveryNoteNr,
      )
    } catch (e: DataAccessException) {
      log.warn("Query failed for $tableName, returning empty list: ${e.message}")
      emptyList()
    }
  }

  override fun findBytes(
    factoryNr: Long,
    date: LocalDate,
    time: LocalTime,
    deliveryNoteNr: Int,
    deliveryNoteCircle: Int,
    creationDate: LocalDate,
    creationTime: LocalTime,
    storageDate: LocalDate,
    storageTime: LocalTime,
  ): ByteArray? {
    val tableName = LegacyTableNames.deliveryNotePdfsTable(date)
    legacyTableInitializer.createIfNotExists(
      tableName,
      LegacyTableNames.DELIVERY_NOTE_PDFS_TEMPLATE
    )
    // erstellungzeit/ablagezeit are time(6) columns: they are read and bound as LocalTime so the
    // fractional seconds survive (java.sql.Time would silently truncate them to whole seconds).
    val sql =
      """
      SELECT pdflieferschein FROM $tableName
      WHERE werksnummer = ? AND datum = ? AND zeit = ? AND lsnummer = ? AND lskreis = ?
        AND erstellungdatum = ? AND erstellungzeit = ?
        AND ablagedatum = ? AND ablagezeit = ?
      """
        .trimIndent()
    val requestedKey =
      "werksnummer=$factoryNr datum=$date zeit=$time lsnummer=$deliveryNoteNr " +
        "lskreis=$deliveryNoteCircle erstellungdatum=$creationDate erstellungzeit=$creationTime " +
        "ablagedatum=$storageDate ablagezeit=$storageTime"

    val rows: List<ByteArray?> =
      try {
        jdbcTemplate.query(
          sql,
          { rs, _ -> rs.getBytes("pdflieferschein") },
          factoryNr,
          Date.valueOf(date),
          Time.valueOf(time),
          deliveryNoteNr,
          deliveryNoteCircle,
          Date.valueOf(creationDate),
          creationTime,
          Date.valueOf(storageDate),
          storageTime,
        )
      } catch (e: DataAccessException) {
        log.warn("Query failed for $tableName, returning null: ${e.message}")
        return null
      }

    if (rows.isEmpty()) {
      log.warn("No pdf row found in $tableName for requested key: $requestedKey")
      logLookupDiagnostics(tableName, factoryNr, date, time, deliveryNoteNr)
      return null
    }

    val bytes = rows.first()
    if (bytes == null) {
      log.warn(
        "Pdf row found in $tableName but pdflieferschein is NULL for requested key: $requestedKey"
      )
      logLookupDiagnostics(tableName, factoryNr, date, time, deliveryNoteNr)
    }
    return bytes
  }

  private fun logLookupDiagnostics(
    tableName: String,
    factoryNr: Long,
    date: LocalDate,
    time: LocalTime,
    deliveryNoteNr: Int,
  ) {
    try {
      val columnTypes =
        jdbcTemplate.query(
          """
          SELECT column_name, data_type, datetime_precision
          FROM information_schema.columns
          WHERE table_name = ?
            AND column_name IN ('datum', 'zeit', 'lsnummer', 'lskreis', 'erstellungdatum',
                                'erstellungzeit', 'ablagedatum', 'ablagezeit', 'pdflieferschein')
          ORDER BY ordinal_position
          """
            .trimIndent(),
          { rs, _ ->
            "${rs.getString("column_name")}:${rs.getString("data_type")}" +
              "(precision=${rs.getObject("datetime_precision")})"
          },
          tableName,
        )

      val candidateRows =
        jdbcTemplate.query(
          """
          SELECT werksnummer::text AS werksnummer, datum::text AS datum, zeit::text AS zeit,
                 lsnummer::text AS lsnummer, lskreis::text AS lskreis,
                 erstellungdatum::text AS erstellungdatum, erstellungzeit::text AS erstellungzeit,
                 ablagedatum::text AS ablagedatum, ablagezeit::text AS ablagezeit,
                 (pdflieferschein IS NULL) AS pdf_is_null, length(pdflieferschein) AS pdf_length,
                 laenge::text AS laenge
          FROM $tableName
          WHERE werksnummer = ? AND datum = ? AND zeit = ? AND lsnummer = ?
          ORDER BY ablagedatum ASC, ablagezeit ASC
          """
            .trimIndent(),
          { rs, _ ->
            "[werksnummer=${rs.getString("werksnummer")} datum=${rs.getString("datum")} " +
              "zeit=${rs.getString("zeit")} lsnummer=${rs.getString("lsnummer")} " +
              "lskreis=${rs.getString("lskreis")} " +
              "erstellungdatum=${rs.getString("erstellungdatum")} " +
              "erstellungzeit=${rs.getString("erstellungzeit")} " +
              "ablagedatum=${rs.getString("ablagedatum")} ablagezeit=${rs.getString("ablagezeit")} " +
              "pdfIsNull=${rs.getBoolean("pdf_is_null")} pdfLength=${rs.getObject("pdf_length")} " +
              "laenge=${rs.getString("laenge")}]"
          },
          factoryNr,
          Date.valueOf(date),
          Time.valueOf(time),
          deliveryNoteNr,
        )

      log.warn(
        "Pdf lookup diagnostics for $tableName: column types=$columnTypes; " +
          "rows for delivery note (werksnummer=$factoryNr datum=$date zeit=$time lsnummer=$deliveryNoteNr): " +
          "${candidateRows.size} row(s) $candidateRows"
      )
    } catch (e: DataAccessException) {
      log.warn("Pdf lookup diagnostics failed for $tableName: ${e.message}")
    }
  }
}
