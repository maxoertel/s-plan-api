package de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.datasource.database.disposition

import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.DeliveryNoteId
import de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.datasource.database.DeliveryNoteDataSource
import de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.datasource.entity.DeliveryNoteEntity
import de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.parser.DeliveryNoteIdParser
import de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.parser.ParsedDeliveryNoteId
import de.sauter_gmbh.s_plan_api.infra_db.DbTimestampConverter
import de.sauter_gmbh.s_plan_api.infra_db.condition.ConditionalOnAppMode
import de.sauter_gmbh.s_plan_api.infra_db.formatter.DATE_ID_FORMATTER
import de.sauter_gmbh.s_plan_api.infra_db.formatter.TIME_ID_FORMATTER
import de.sauter_gmbh.s_plan_api.infra_db.table.DeliveryNotePdfSources
import de.sauter_gmbh.s_plan_api.infra_db.table.LegacyTableInitializer
import de.sauter_gmbh.s_plan_api.infra_db.table.LegacyTableNames
import de.sauter_gmbh.s_plan_api.infra_db.validation.requireSafeSqlIdentifier
import de.sauter_gmbh.s_plan_api.shared_core.logger.logger
import de.sauter_gmbh.s_plan_api.shared_core.mode.AppMode
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.DataAccessException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component

@Component
@ConditionalOnAppMode(AppMode.S_PLAN)
internal class DispositionDeliveryNoteDataSource(
  private val jdbcTemplate: JdbcTemplate,
  private val deliveryNoteIdParser: DeliveryNoteIdParser,
  private val timestampConverter: DbTimestampConverter,
  private val legacyTableInitializer: LegacyTableInitializer,
  @Value("\${DELIVERY_NOTE_SUPER_PLASTICIZER_COLUMN_EXISTS:false}")
  private val superPlasticizerColumnExists: Boolean,
  @Value("\${DELIVERY_NOTE_ACCEPTED_AT_COLUMN_EXISTS:false}")
  private val acceptedAtColumnExists: Boolean,
  @Value("\${DELIVERY_NOTE_WAITING_TIME_COLUMN_EXISTS:false}")
  private val waitingTimeColumnExists: Boolean,
) : DeliveryNoteDataSource {

  private val log by logger()

  private val rowMapper = RowMapper { rs, _ ->
    val factoryNr = rs.getLong("werksnummer")
    val date = rs.getDate("datum")?.toLocalDate() ?: return@RowMapper null
    val time = rs.getTime("zeit")?.toLocalTime() ?: return@RowMapper null
    val deliveryNoteNr = rs.getInt("lsnummer")
    DeliveryNoteEntity(
      id =
        DeliveryNoteId.from(
            factoryNr = factoryNr,
            deliveryNoteNr = deliveryNoteNr,
            date = date,
            time = time,
          )
          .value,
      factoryNr = factoryNr,
      date = date.toString(),
      time = time.toString(),
      deliveryNoteNr = deliveryNoteNr,
      deliveryNoteNrExternal = rs.getObject("lsnummerfremd") as? Int,
      deliveryNoteCircle = rs.getObject("lskreis") as? Int,
      printType = rs.getString("lieferscheindruckart"),
      orderType = rs.getObject("auftragart") as? Int,
      deliveryDateTime = rs.getTimestamp("lieferdatumzeit")?.toString(),
      orderNr = rs.getObject("auftragnr") as? Int,
      loadNr = rs.getObject("ladenr") as? Int,
      batchNr = (rs.getObject("chargennummer") as? Number)?.toInt(),
      plannedQuantity = rs.getObject("sollmenge") as? Double,
      actualQuantity = rs.getObject("istmenge") as? Double,
      loadQuantity = rs.getObject("lademenge") as? Double,
      returnQuantity = rs.getObject("rueckmenge") as? Double,
      customerNr = rs.getObject("kundennr") as? Long,
      customerMatchCode = rs.getString("kundenmatchcode"),
      customerName1 = rs.getString("kundenname1"),
      customerName2 = rs.getString("kundenname2"),
      customerName3 = rs.getString("kundenname3"),
      customerName4 = rs.getString("kundenname4"),
      customerStreet = rs.getString("kundenstrasse"),
      customerHouseNr = rs.getString("kundenhausnummer"),
      customerCountry = rs.getString("kundenland"),
      customerZip = rs.getString("kundenplz"),
      customerCity = rs.getString("kundenort"),
      customerPhone = rs.getString("kundentelefon"),
      customerMobile = rs.getString("kundenmobil"),
      customerEmail1 = rs.getString("kundenemail1"),
      customerEmail2 = rs.getString("kundenemail2"),
      customerNote = rs.getString("kundenbemerkung"),
      customerBlockCode = rs.getObject("kundensperrcode") as? Int,
      siteNr = rs.getObject("baustnr") as? Long,
      siteMatchCode = rs.getString("baustmatchcode"),
      siteDescription1 = rs.getString("baustbezeichnung1"),
      siteDescription2 = rs.getString("baustbezeichnung2"),
      siteDescription3 = rs.getString("baustbezeichnung3"),
      siteDescription4 = rs.getString("baustbezeichnung4"),
      siteStreet = rs.getString("bauststrasse"),
      siteHouseNr = rs.getString("bausthausnummer"),
      siteCountry = rs.getString("baustland"),
      siteZip = rs.getString("baustplz"),
      siteCity = rs.getString("baustort"),
      siteContact = rs.getString("baustansprechpartner"),
      siteNote = rs.getString("baustbemerkung"),
      sitePhone = rs.getString("bausttelefon"),
      siteMobile = rs.getString("baustmobil"),
      siteLatitude = rs.getString("site_latitude"),
      siteLongitude = rs.getString("site_longitude"),
      vehicleId = rs.getObject("fahrzeugnr") as? Long,
      recipe = rs.getString("rezeptsorte"),
      articleNr = rs.getString("artikelnr"),
      description = rs.getString("bezeichnung"),
      statusLoadingInProgressTimestamp = rs.getTimestamp("fzstatusbeladung")?.toString(),
      statusLoadingCompletedTimestamp = rs.getTimestamp("fzstatusbeladungende")?.toString(),
      statusEnRouteToSiteTimestamp = rs.getTimestamp("fzstatushinfahrt")?.toString(),
      statusOnSiteTimestamp = rs.getTimestamp("fzstatusbaustelle")?.toString(),
      statusUnloadingStartedTimestamp = rs.getTimestamp("fzstatusentleerung")?.toString(),
      statusUnloadingCompletedTimestamp = rs.getTimestamp("fzstatusentleerungende")?.toString(),
      statusReturningTimestamp = rs.getTimestamp("fzstatusrueckfahrt")?.toString(),
      statusReturnedTimestamp = rs.getTimestamp("fzstatusaufwerk")?.toString(),
      signatureDeviceId = rs.getString("unterschriftgeraeteid"),
      signatureStatus = rs.getString("unterschriftstatus"),
      driverSignatureSet = rs.getBoolean("driver_signature_set"),
      driverSignaturePlaceholder = rs.getBoolean("driver_signature_placeholder"),
      driverSignatureTimestampMillis =
        rs.getTimestamp("lieferscheinfahrerzeitstempel")?.let(timestampConverter::toEpochMillis),
      customerSignatureSet = rs.getBoolean("customer_signature_set"),
      customerSignatureTimestampMillis =
        rs.getTimestamp("lieferscheinabnehmerzeitstempel")?.let(timestampConverter::toEpochMillis),
      customerSignaturePlaceholder = rs.getBoolean("customer_signature_placeholder"),
      customerSignatureCollective = rs.getBoolean("customer_signature_collective"),
      createdAt = rs.getTimestamp("lieferscheinerstellungdatumzeit")?.toString(),
      updatedAt = rs.getTimestamp("lieferscheinletzteaenderungdatumzeit")?.toString(),
      waterCorrectionUnloading = rs.getObject("entladungwasserkorrektur") as? Double,
      superPlasticizerUnloading = rs.getObject(SUPER_PLASTICIZER_COLUMN) as? Double,
      waitingTimeMinutes = rs.getObject(WAITING_TIME_COLUMN) as? Double,
      driverAcceptedAtMillis =
        rs.getTimestamp(ACCEPTED_AT_COLUMN)?.let(timestampConverter::toEpochMillis),
    )
  }

  override fun findDriverSignatureJpeg(deliveryNoteId: String): ByteArray? =
    findSignatureJpeg(deliveryNoteId, "lieferscheinfahrerunterschrift")

  override fun findCustomerSignatureJpeg(deliveryNoteId: String): ByteArray? =
    findSignatureJpeg(deliveryNoteId, "lieferscheinabnehmerunterschrift")

  private fun findSignatureJpeg(deliveryNoteId: String, columnName: String): ByteArray? {
    val parsed = deliveryNoteIdParser.parse(deliveryNoteId)
    val date = LocalDate.parse(parsed.date, DATE_ID_FORMATTER)
    val time = LocalTime.parse(parsed.time, TIME_ID_FORMATTER)
    val tableName = LegacyTableNames.deliveryNotesTable(date)
    val sql =
      """
      SELECT $columnName FROM $tableName
      WHERE werksnummer = ? AND lsnummer = ? AND datum = ? AND zeit = ?
      """
        .trimIndent()
    return try {
      jdbcTemplate.queryForObject(
        sql,
        ByteArray::class.java,
        parsed.factoryNr,
        parsed.deliveryNoteNr,
        Date.valueOf(date),
        Time.valueOf(time),
      )
    } catch (e: EmptyResultDataAccessException) {
      log.info("No row found in $tableName for delivery note $deliveryNoteId")
      null
    } catch (e: DataAccessException) {
      log.warn("Query failed for $tableName, returning null: ${e.message}")
      null
    }
  }

  override fun findById(deliveryNoteId: String): DeliveryNoteEntity? {
    val parsed = deliveryNoteIdParser.parse(deliveryNoteId)
    val date = LocalDate.parse(parsed.date, DATE_ID_FORMATTER)
    val time = LocalTime.parse(parsed.time, TIME_ID_FORMATTER)
    val tableName = LegacyTableNames.deliveryNotesTable(date)
    return try {
      jdbcTemplate
        .query(
          """
          ${selectClause(tableName, date)}
          WHERE werksnummer = ? AND datum = ? AND zeit = ? AND lsnummer = ?
          LIMIT 1
          """
            .trimIndent(),
          rowMapper,
          parsed.factoryNr,
          Date.valueOf(date),
          Time.valueOf(time),
          parsed.deliveryNoteNr,
        )
        .filterNotNull()
        .firstOrNull()
    } catch (e: DataAccessException) {
      log.warn("Query failed for $tableName, returning null: ${e.message}")
      null
    }
  }

  /**
   * Writes the given times to the vehicle status columns of both the delivery note and order table.
   *
   * A column keeps the day it already carries, so correcting a time never moves a status to another
   * date.
   */
  override fun updateStatusTimes(deliveryNoteId: String, columnTimes: Map<String, LocalTime>) {
    if (columnTimes.isEmpty()) return
    columnTimes.keys.forEach { requireSafeSqlIdentifier(it) }

    val parsed = deliveryNoteIdParser.parse(deliveryNoteId)
    val date = LocalDate.parse(parsed.date, DATE_ID_FORMATTER)
    val time = LocalTime.parse(parsed.time, TIME_ID_FORMATTER)

    val stored = findStatusRow(columnTimes.keys, parsed, date, time)
    val updates =
      columnTimes.entries.map { (column, newTime) ->
        column to Timestamp.valueOf(LocalDateTime.of(stored.statusDates[column] ?: date, newTime))
      }

    updateDeliveryNotesTable(deliveryNoteId, parsed, date, time, updates)
    updateOrdersTable(deliveryNoteId, parsed, date, stored, updates)
  }

  private fun updateDeliveryNotesTable(
    deliveryNoteId: String,
    parsed: ParsedDeliveryNoteId,
    date: LocalDate,
    time: LocalTime,
    updates: List<Pair<String, Timestamp>>,
  ) {
    val tableName = LegacyTableNames.deliveryNotesTable(date)
    val sql =
      """
      UPDATE $tableName
      SET ${updates.toSetClause()}
      WHERE werksnummer = ? AND lsnummer = ? AND datum = ? AND zeit = ?
      """
        .trimIndent()

    val arguments =
      buildList<Any?> {
        updates.forEach { (_, timestamp) -> add(timestamp) }
        add(parsed.factoryNr)
        add(parsed.deliveryNoteNr)
        add(Date.valueOf(date))
        add(Time.valueOf(time))
      }

    logUpdatedRows(
      jdbcTemplate.update(sql, *arguments.toTypedArray()),
      tableName,
      deliveryNoteId,
      updates,
    )
  }

  private fun updateOrdersTable(
    deliveryNoteId: String,
    parsed: ParsedDeliveryNoteId,
    date: LocalDate,
    stored: StatusRow,
    updates: List<Pair<String, Timestamp>>,
  ) {
    val orderNr = stored.orderNr
    val loadNr = stored.loadNr
    if (orderNr == null || loadNr == null) {
      log.warn(
        "Skipped order status update for delivery note $deliveryNoteId: " +
          "row carries no order number ($orderNr) and load number ($loadNr)"
      )
      return
    }

    val tableName = LegacyTableNames.ordersTable(date)
    val sql =
      """
      UPDATE $tableName
      SET ${updates.toSetClause()}
      WHERE werksnummer = ? AND auftragnr = ? AND ladenr = ?
      """
        .trimIndent()

    val arguments =
      buildList<Any?> {
        updates.forEach { (_, timestamp) -> add(timestamp) }
        add(parsed.factoryNr)
        add(orderNr)
        add(loadNr)
      }

    logUpdatedRows(
      jdbcTemplate.update(sql, *arguments.toTypedArray()),
      tableName,
      deliveryNoteId,
      updates,
    )
  }

  private fun List<Pair<String, Timestamp>>.toSetClause(): String =
    joinToString(", ") { (column, _) -> "$column = ?" }

  private fun logUpdatedRows(
    updatedRows: Int,
    tableName: String,
    deliveryNoteId: String,
    updates: List<Pair<String, Timestamp>>,
  ) {
    if (updatedRows == 0) {
      log.warn("No row updated in $tableName for delivery note $deliveryNoteId")
      return
    }
    val columns = updates.joinToString { (column, _) -> column }
    log.info("Updated $updatedRows row(s) in $tableName for columns $columns")
  }

  // The stored row is read first so each corrected time can keep the day it belongs to.
  private fun findStatusRow(
    columns: Set<String>,
    parsed: ParsedDeliveryNoteId,
    date: LocalDate,
    time: LocalTime,
  ): StatusRow {
    val tableName = LegacyTableNames.deliveryNotesTable(date)
    val sql =
      """
      SELECT auftragnr, ladenr, ${columns.joinToString(", ")} FROM $tableName
      WHERE werksnummer = ? AND lsnummer = ? AND datum = ? AND zeit = ?
      LIMIT 1
      """
        .trimIndent()
    val statusRowMapper =
      RowMapper<StatusRow> { rs, _ ->
        StatusRow(
          orderNr = rs.getObject("auftragnr") as? Int,
          loadNr = rs.getObject("ladenr") as? Int,
          statusDates =
            buildMap {
              for (column in columns) {
                val storedDate =
                  rs.getTimestamp(column)?.toLocalDateTime()?.toLocalDate() ?: continue
                put(column, storedDate)
              }
            },
        )
      }
    return jdbcTemplate
      .query(
        sql,
        statusRowMapper,
        parsed.factoryNr,
        parsed.deliveryNoteNr,
        Date.valueOf(date),
        Time.valueOf(time),
      )
      .firstOrNull() ?: StatusRow(orderNr = null, loadNr = null, statusDates = emptyMap())
  }

  private data class StatusRow(
    val orderNr: Int?,
    val loadNr: Int?,
    val statusDates: Map<String, LocalDate>,
  )

  override fun findByDate(date: LocalDate): List<DeliveryNoteEntity> {
    val tableName = LegacyTableNames.deliveryNotesTable(date)
    return try {
      jdbcTemplate
        .query(
          "${selectClause(tableName, date)} WHERE datum = ?",
          rowMapper,
          Date.valueOf(date),
        )
        .filterNotNull()
    } catch (e: DataAccessException) {
      log.warn("Query failed for $tableName, returning empty list: ${e.message}")
      emptyList()
    }
  }

  private fun selectClause(tableName: String, date: LocalDate): String {
    val pdfTable = LegacyTableNames.deliveryNotePdfsTable(date)
    legacyTableInitializer.createIfNotExists(pdfTable, LegacyTableNames.DELIVERY_NOTE_PDFS_TEMPLATE)
    return """
    SELECT werksnummer, datum, zeit, lsnummer, lsnummerfremd, lskreis,
           lieferscheindruckart, auftragart,
           lieferdatumzeit, auftragnr, ladenr, chargennummer, sollmenge, istmenge,
           lademenge, rueckmenge, kundennr, kundenmatchcode, kundenname1, kundenname2,
           kundenname3, kundenname4, kundenstrasse, kundenhausnummer, kundenland,
           kundenplz, kundenort, kundentelefon, kundenmobil, kundenemail1, kundenemail2,
           kundenbemerkung, kundensperrcode, baustnr, baustmatchcode, baustbezeichnung1,
           baustbezeichnung2, baustbezeichnung3, baustbezeichnung4, bauststrasse,
           bausthausnummer, baustland, baustplz, baustort, baustansprechpartner,
           baustbemerkung, bausttelefon, baustmobil, fahrzeugnr, rezeptsorte, artikelnr,
           bezeichnung, fzstatusbeladung, fzstatusbeladungende, fzstatushinfahrt,
           fzstatusbaustelle, fzstatusentleerung, fzstatusentleerungende,
           fzstatusrueckfahrt, fzstatusaufwerk, unterschriftgeraeteid, unterschriftstatus,
           lieferscheinfahrerunterschrift IS NOT NULL AS driver_signature_set,
           lieferscheinfahrerzeitstempel,
           lieferscheinabnehmerunterschrift IS NOT NULL AS customer_signature_set,
           lieferscheinabnehmerzeitstempel,
           COALESCE(lieferscheinabnehmerkontaktloseuebergabe, FALSE)
             AS customer_signature_placeholder,
           /* lieferscheinabnehmerkontaktloseuebergabe = signature automatically set */
           COALESCE(lieferscheinabnehmerkontaktloseuebergabe, FALSE)
             AND COALESCE(lieferscheinfahrerpruefsumme = lieferscheinabnehmerpruefsumme, FALSE)
             AS driver_signature_placeholder,
           COALESCE(own_pdf.datenquelle = '${DeliveryNotePdfSources.S_PLAN_COLLECTIVE}', FALSE)
             AS customer_signature_collective,
           lieferscheinerstellungdatumzeit, lieferscheinletzteaenderungdatumzeit,
           entladungwasserkorrektur,
           ${superPlasticizerSelect()},
           ${waitingTimeSelect()},
           ${acceptedAtSelect()},
           site.site_latitude, site.site_longitude
    FROM $tableName dn
    LEFT JOIN LATERAL (
        SELECT b.baustellenlatitude AS site_latitude, b.baustellenlongitude AS site_longitude
        FROM baust b
        WHERE b.kundennr = dn.kundennr AND b.baustnr = dn.baustnr
        LIMIT 1
    ) site ON TRUE
    LEFT JOIN LATERAL (
        SELECT p.datenquelle
        FROM $pdfTable p
        WHERE p.werksnummer = dn.werksnummer AND p.datum = dn.datum
          AND p.lsnummer = dn.lsnummer
          AND p.datenquelle LIKE '${DeliveryNotePdfSources.S_PLAN_PATTERN}'
        ORDER BY p.ablagedatum DESC, p.ablagezeit DESC
        LIMIT 1
    ) own_pdf ON TRUE
    """
      .trimIndent()
  }

  // The super-plasticizer column only exists in newer legacy schemas. Where it is missing a
  // constant keeps the result-set shape stable, so the row mapper always finds the column.
  private fun superPlasticizerSelect(): String =
    if (superPlasticizerColumnExists) SUPER_PLASTICIZER_COLUMN
    else "CAST(0 AS double precision) AS $SUPER_PLASTICIZER_COLUMN"

  private fun waitingTimeSelect(): String =
    if (waitingTimeColumnExists) WAITING_TIME_COLUMN
    else "CAST(NULL AS double precision) AS $WAITING_TIME_COLUMN"

  private fun acceptedAtSelect(): String =
    if (acceptedAtColumnExists) ACCEPTED_AT_COLUMN
    else "CAST(NULL AS timestamp) AS $ACCEPTED_AT_COLUMN"

  companion object {
    private const val SUPER_PLASTICIZER_COLUMN = "entladungfliessmittel"
    private const val WAITING_TIME_COLUMN = "entladungwartezeit"
    private const val ACCEPTED_AT_COLUMN = "instrackangenommendatumzeit"
  }
}
