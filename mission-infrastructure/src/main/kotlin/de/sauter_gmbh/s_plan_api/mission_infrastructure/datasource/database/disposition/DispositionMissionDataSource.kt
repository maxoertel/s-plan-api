package de.sauter_gmbh.s_plan_api.mission_infrastructure.datasource.database.disposition

import de.sauter_gmbh.s_plan_api.infra_db.DbTimestampConverter
import de.sauter_gmbh.s_plan_api.infra_db.condition.ConditionalOnAppMode
import de.sauter_gmbh.s_plan_api.infra_db.formatter.DATE_ID_FORMATTER
import de.sauter_gmbh.s_plan_api.infra_db.table.LegacyTableInitializer
import de.sauter_gmbh.s_plan_api.infra_db.table.LegacyTableNames
import de.sauter_gmbh.s_plan_api.mission_infrastructure.datasource.database.MissionDataSource
import de.sauter_gmbh.s_plan_api.mission_infrastructure.datasource.entity.MissionEntity
import de.sauter_gmbh.s_plan_api.mission_infrastructure.datasource.entity.SiteKeyEntity
import de.sauter_gmbh.s_plan_api.shared_core.logger.logger
import de.sauter_gmbh.s_plan_api.shared_core.mode.AppMode
import java.time.LocalDate
import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component

@Component
@ConditionalOnAppMode(AppMode.S_PLAN)
internal class DispositionMissionDataSource(
  private val jdbcTemplate: JdbcTemplate,
  private val legacyTableInitializer: LegacyTableInitializer,
  private val timestampConverter: DbTimestampConverter,
  @Value("\${DELIVERY_NOTE_SUPER_PLASTICIZER_COLUMN_EXISTS:false}")
  private val superPlasticizerColumnExists: Boolean,
  @Value("\${DELIVERY_NOTE_ACCEPTED_AT_COLUMN_EXISTS:false}")
  private val acceptedAtColumnExists: Boolean,
  @Value("\${MISSION_DRIVER_NOTE_COLUMN:}") driverNoteColumn: String,
) : MissionDataSource {

  private val log by logger()

  private val driverNoteColumnReference =
    driverNoteColumn.trim().lowercase().ifEmpty { DEFAULT_DRIVER_NOTE_COLUMN }

  init {
    // remember: the reference is inlined into SQL, so only plain a./ls. column names are accepted
    require(DRIVER_NOTE_COLUMN_PATTERN.matches(driverNoteColumnReference)) {
      "Invalid MISSION_DRIVER_NOTE_COLUMN '$driverNoteColumn'. Expected a.<column> or ls.<column>."
    }
  }

  private val isDriverNoteFromDeliveryNote = driverNoteColumnReference.startsWith("ls.")
  private val driverNoteColumnName = driverNoteColumnReference.substringAfter('.')

  private val rowMapper = RowMapper { rs, _ ->
    val werksnummer = rs.getLong("werksnummer")
    val auftragnr = rs.getInt("auftragnr")
    val ladenr = rs.getInt("ladenr")
    val datum = rs.getTimestamp("datum")?.toLocalDateTime()?.toLocalDate() ?: return@RowMapper null
    MissionEntity(
      id = "${werksnummer}_${auftragnr}_${ladenr}_${datum.format(DATE_ID_FORMATTER)}",
      factoryId = rs.getInt("werksnummer"),
      orderNr = auftragnr,
      loadNr = ladenr,
      deliveryNr = rs.getLong("lsnummer"),
      date = datum,
      orderType = rs.getObject("auftragart") as? Int,
      deliveryType = rs.getObject("lieferart") as? Int,
      factoryName = rs.getString("werkanschrift1"),
      articleNr = rs.getString("artikelnr"),
      recipe = rs.getString("rezeptsorte"),
      plannedQuantity = rs.getObject("sollmenge") as? Double,
      actualQuantity = rs.getObject("istmenge") as? Double,
      loadQuantity = rs.getObject("lademenge") as? Double,
      note1 = rs.getString("auftragbemerkung"),
      note2 = rs.getString("auftragbemerkung2"),
      vehicleId = rs.getObject("fahrzeugnr") as? Long,
      vehicleLicensePlate = rs.getString("kennzeichen"),
      deliveryNrExternal = (rs.getObject("lsnummerfremd") as? Number)?.toLong(),
      statusId = rs.getObject("aktuellerfzstatus") as? Int,
      driverNote = rs.getString(DRIVER_NOTE_ALIAS),
      driverStartedAtMillis =
        rs.getTimestamp("instrackgestartetdatumzeit")?.let(timestampConverter::toEpochMillis),
      driverAcceptedAtMillis =
        rs.getTimestamp(ACCEPTED_AT_COLUMN)?.let(timestampConverter::toEpochMillis),
      time = rs.getTimestamp("zeit")?.toString(),
      dispatchTime = rs.getTimestamp("datumzeit")?.toString(),
      deliveryTime = rs.getTimestamp("lieferdatumzeit")?.toString(),
      nextTime = rs.getTimestamp("datumzeitnaechsterla")?.toString(),
      driver = rs.getString("fahrername"),
      factorySummaryName = rs.getString("werke_werksname"),
      factorySummaryLatitude = rs.getString("werke_werklatitude"),
      factorySummaryLongitude = rs.getString("werke_werklongitude"),
      factorySummaryRadiusInMeter = rs.getObject("werke_radiusumwerkinmeter") as? Int,
      factorySummaryStreet = rs.getString("werke_werkstrasse"),
      factorySummaryHouseNr = rs.getString("werke_werkhausnummer"),
      factorySummaryCity = rs.getString("werke_werkort"),
      factorySummaryZip = rs.getString("werke_werkplz"),
      factorySummaryCountry = rs.getString("werke_werkland"),
      factorySummaryPhone = rs.getString("werke_werkstelefon"),
      returnFactoryId = rs.getObject("folgewerksnummer") as? Int,
      returnFactorySummaryName = rs.getString("folgewerke_werksname"),
      returnFactorySummaryLatitude = rs.getString("folgewerke_werklatitude"),
      returnFactorySummaryLongitude = rs.getString("folgewerke_werklongitude"),
      returnFactorySummaryRadiusInMeter = rs.getObject("folgewerke_radiusumwerkinmeter") as? Int,
      returnFactorySummaryStreet = rs.getString("folgewerke_werkstrasse"),
      returnFactorySummaryHouseNr = rs.getString("folgewerke_werkhausnummer"),
      returnFactorySummaryCity = rs.getString("folgewerke_werkort"),
      returnFactorySummaryZip = rs.getString("folgewerke_werkplz"),
      returnFactorySummaryCountry = rs.getString("folgewerke_werkland"),
      returnFactorySummaryPhone = rs.getString("folgewerke_werkstelefon"),
      siteNr = rs.getObject("baustnr") as? Long,
      siteSummaryBezeichnung1 = rs.getString("baust_bezeichnung1"),
      siteSummaryBezeichnung2 = rs.getString("baust_bezeichnung2"),
      siteSummaryBezeichnung3 = rs.getString("baust_bezeichnung3"),
      siteSummaryBezeichnung4 = rs.getString("baust_bezeichnung4"),
      siteSummaryMatchCode = rs.getString("baust_matchcode"),
      siteSummaryLatitude = rs.getString("baust_baustellenlatitude"),
      siteSummaryLongitude = rs.getString("baust_baustellenlongitude"),
      siteSummaryRadiusInMeter = rs.getObject("baust_radiusumbaustelleinmeter") as? Int,
      siteSummaryStreet = rs.getString("baust_strasse"),
      siteSummaryHouseNr = rs.getString("baust_hausnummer"),
      siteSummaryCity = rs.getString("baust_ort"),
      siteSummaryZip = rs.getString("baust_plz"),
      siteSummaryCountry = rs.getString("baust_land"),
      siteSummaryPhone = rs.getString("baust_telefon"),
      siteSummaryDeliveryNoteInfo = rs.getString("baust_lsinfo"),
      siteDesc1 = rs.getString("baustbezeichnung1"),
      clientNr = rs.getObject("kundennr") as? Long,
      clientMatchCode = rs.getString("kundenmatchcode"),
      clientName1 = rs.getString("kundenname1"),
      clientName2 = rs.getString("kundenname2"),
      clientName3 = rs.getString("kundenname3"),
      clientName4 = rs.getString("kundenname4"),
      clientStreet = rs.getString("kundenstrasse"),
      clientHouseNr = rs.getString("kundenhausnummer"),
      clientCountry = rs.getString("kundenland"),
      clientZip = rs.getString("kundenplz"),
      clientCity = rs.getString("kundenort"),
      clientPhone = rs.getString("kundentelefon"),
      clientFax = rs.getString("kundenfax"),
      clientMobile = rs.getString("kundenmobil"),
      clientEmail1 = rs.getString("kundenemail1"),
      clientEmail2 = rs.getString("kundenemail2"),
      clientNote = rs.getString("kundenbemerkung"),
      statusLoadingInProgressTimestamp = rs.getTimestamp("fzstatusbeladung")?.toString(),
      statusLoadingCompletedTimestamp = rs.getTimestamp("fzstatusbeladungende")?.toString(),
      statusEnRouteToSiteTimestamp = rs.getTimestamp("fzstatushinfahrt")?.toString(),
      statusOnSiteTimestamp = rs.getTimestamp("fzstatusbaustelle")?.toString(),
      statusUnloadingStartedTimestamp = rs.getTimestamp("fzstatusentleerung")?.toString(),
      statusUnloadingCompletedTimestamp = rs.getTimestamp("fzstatusentleerungende")?.toString(),
      statusReturningTimestamp = rs.getTimestamp("fzstatusrueckfahrt")?.toString(),
      statusReturnedTimestamp = rs.getTimestamp("fzstatusaufwerk")?.toString(),
      driverSignatureSet = rs.getBoolean("driver_signature_set"),
      driverSignatureTimestampMillis =
        rs.getTimestamp("lieferscheinfahrerzeitstempel")?.let(timestampConverter::toEpochMillis),
      customerSignatureSet = rs.getBoolean("customer_signature_set"),
      customerSignatureTimestampMillis =
        rs.getTimestamp("lieferscheinabnehmerzeitstempel")?.let(timestampConverter::toEpochMillis),
      waterCorrectionUnloading = rs.getObject("entladungwasserkorrektur") as? Double,
      superPlasticizerUnloading = rs.getObject(SUPER_PLASTICIZER_COLUMN) as? Double,
      deliveryNotePdfExists = rs.getBoolean("delivery_note_pdf_exists"),
    )
  }

  private val siteKeyRowMapper = RowMapper { rs, _ ->
    SiteKeyEntity(customerNr = rs.getLong("kundennr"), siteNr = rs.getLong("baustnr"))
  }

  private fun selectClause(ordersTable: String, deliveryNotesTable: String, pdfTable: String) =
    """
    SELECT a.werksnummer, a.auftragnr, a.ladenr, a.aktuellerfzstatus,
           COALESCE(NULLIF(ls.lsnummer, 0), a.lsnummer) AS lsnummer,
           COALESCE(NULLIF(ls.lsnummerfremd, 0), a.lsnummerfremd) AS lsnummerfremd,
           a.datum, a.zeit, a.datumzeit, a.datumzeitnaechsterla, a.lieferdatumzeit,
           a.sollmenge, a.istmenge, a.lademenge, a.auftragart, a.lieferart, a.werkanschrift1,
           a.artikelnr, a.rezeptsorte, a.auftragbemerkung, a.folgewerksnummer,
           a.auftragbemerkung2, ${driverNoteSelect()}, a.instrackgestartetdatumzeit,
           a.fahrzeugnr, a.kennzeichen, a.fahrername, a.baustnr, a.baustbezeichnung1,
           a.kundennr, a.kundenmatchcode, a.kundenname1, a.kundenname2, a.kundenname3,
           a.kundenname4, a.kundenstrasse, a.kundenhausnummer, a.kundenland, a.kundenplz,
           a.kundenort, a.kundentelefon, a.kundenfax, a.kundenmobil, a.kundenemail1,
           a.kundenemail2, a.kundenbemerkung,
           ls.fzstatusbeladung, ls.fzstatusbeladungende, ls.fzstatushinfahrt,
           ls.fzstatusbaustelle, ls.fzstatusentleerung, ls.fzstatusentleerungende,
           ls.fzstatusrueckfahrt, ls.fzstatusaufwerk,
           ls.driver_signature_set, ls.lieferscheinfahrerzeitstempel,
           ls.customer_signature_set, ls.lieferscheinabnehmerzeitstempel,
           ls.entladungwasserkorrektur,
           ls.$SUPER_PLASTICIZER_COLUMN,
           ls.$ACCEPTED_AT_COLUMN,
           EXISTS (
               SELECT 1
               FROM $deliveryNotesTable dn
               JOIN $pdfTable p
                 ON p.werksnummer = dn.werksnummer AND p.datum = dn.datum
                AND p.zeit = dn.zeit AND p.lsnummer = dn.lsnummer
               WHERE dn.datum = a.datum AND dn.auftragnr = a.auftragnr AND dn.ladenr = a.ladenr
           ) AS delivery_note_pdf_exists,
           w.werksname AS werke_werksname,
           w.werklatitude AS werke_werklatitude,
           w.werklongitude AS werke_werklongitude,
           w.radiusumwerkinmeter AS werke_radiusumwerkinmeter,
           w.werkstrasse AS werke_werkstrasse,
           w.werkhausnummer AS werke_werkhausnummer,
           w.werkort AS werke_werkort,
           w.werkplz AS werke_werkplz,
           w.werkland AS werke_werkland,
           w.werkstelefon AS werke_werkstelefon,
           fw.werksname AS folgewerke_werksname,
           fw.werklatitude AS folgewerke_werklatitude,
           fw.werklongitude AS folgewerke_werklongitude,
           fw.radiusumwerkinmeter AS folgewerke_radiusumwerkinmeter,
           fw.werkstrasse AS folgewerke_werkstrasse,
           fw.werkhausnummer AS folgewerke_werkhausnummer,
           fw.werkort AS folgewerke_werkort,
           fw.werkplz AS folgewerke_werkplz,
           fw.werkland AS folgewerke_werkland,
           fw.werkstelefon AS folgewerke_werkstelefon,
           b.bezeichnung1 AS baust_bezeichnung1,
           b.bezeichnung2 AS baust_bezeichnung2,
           b.bezeichnung3 AS baust_bezeichnung3,
           b.bezeichnung4 AS baust_bezeichnung4,
           b.matchcode AS baust_matchcode,
           b.baustellenlatitude AS baust_baustellenlatitude,
           b.baustellenlongitude AS baust_baustellenlongitude,
           b.radiusumbaustelleinmeter AS baust_radiusumbaustelleinmeter,
           b.strasse AS baust_strasse,
           b.hausnummer AS baust_hausnummer,
           b.ort AS baust_ort,
           b.plz AS baust_plz,
           b.land AS baust_land,
           b.telefon AS baust_telefon,
           b.lsinfo AS baust_lsinfo
    FROM $ordersTable a
    LEFT JOIN werke w ON w.werksnummer = a.werksnummer
    LEFT JOIN werke fw ON fw.werksnummer = a.folgewerksnummer
    LEFT JOIN baust b ON b.kundennr = a.kundennr AND b.baustnr = a.baustnr
    LEFT JOIN LATERAL (
        SELECT lsnummer, lsnummerfremd,
               fzstatusbeladung, fzstatusbeladungende, fzstatushinfahrt,
               fzstatusbaustelle, fzstatusentleerung, fzstatusentleerungende,
               fzstatusrueckfahrt, fzstatusaufwerk,
               lieferscheinfahrerunterschrift IS NOT NULL AS driver_signature_set,
               lieferscheinfahrerzeitstempel,
               lieferscheinabnehmerunterschrift IS NOT NULL AS customer_signature_set,
               lieferscheinabnehmerzeitstempel,
               entladungwasserkorrektur,
               ${superPlasticizerSelect()},
               ${acceptedAtSelect()},
               ${deliveryNoteDriverNoteSelect()}
               unterschriftgeraeteid
        FROM $deliveryNotesTable
        WHERE datum = a.datum AND auftragnr = a.auftragnr AND ladenr = a.ladenr
        ORDER BY lsnummer DESC
        LIMIT 1
    ) ls ON true
    """
      .trimIndent()

  override fun findByDate(date: LocalDate): List<MissionEntity> {
    val ordersTable = LegacyTableNames.ordersTable(date)
    val deliveryNotesTable = LegacyTableNames.deliveryNotesTable(date)
    val pdfTable = ensureDeliveryNotePdfsTable(date)
    return try {
      jdbcTemplate
        .query(
          """
        ${selectClause(ordersTable, deliveryNotesTable, pdfTable)}
        WHERE a.ladenr > 0
        """
            .trimIndent(),
          rowMapper,
        )
        .filterNotNull()
    } catch (e: DataAccessException) {
      log.warn(
        "Query failed for $ordersTable / $deliveryNotesTable, returning empty list: ${e.message}"
      )
      emptyList()
    }
  }

  // Site numbers repeat across customers, so the customer number is part of the key and both
  // columns have to travel together.
  override fun findSiteKeysOnDate(date: LocalDate): Set<SiteKeyEntity> {
    val ordersTable = LegacyTableNames.ordersTable(date)
    return try {
      jdbcTemplate
        .query(
          """
          SELECT DISTINCT kundennr, baustnr
          FROM $ordersTable
          WHERE baustnr IS NOT NULL AND kundennr IS NOT NULL
          """
            .trimIndent(),
          siteKeyRowMapper,
        )
        .filterNotNull()
        .toSet()
    } catch (e: DataAccessException) {
      log.warn("Query failed for $ordersTable, returning empty set: ${e.message}")
      emptySet()
    }
  }

  // The super-plasticizer column only exists in newer legacy schemas. Where it is missing a
  // constant keeps the result-set shape stable, so the row mapper always finds the column.
  private fun superPlasticizerSelect(): String =
    if (superPlasticizerColumnExists) SUPER_PLASTICIZER_COLUMN
    else "CAST(0 AS double precision) AS $SUPER_PLASTICIZER_COLUMN"

  private fun acceptedAtSelect(): String =
    if (acceptedAtColumnExists) ACCEPTED_AT_COLUMN
    else "CAST(NULL AS timestamp) AS $ACCEPTED_AT_COLUMN"

  private fun driverNoteSelect(): String =
    if (isDriverNoteFromDeliveryNote) "ls.$DRIVER_NOTE_ALIAS"
    else "a.$driverNoteColumnName AS $DRIVER_NOTE_ALIAS"

  private fun deliveryNoteDriverNoteSelect(): String =
    if (isDriverNoteFromDeliveryNote) "$driverNoteColumnName AS $DRIVER_NOTE_ALIAS," else ""

  // The daily PDF table is created on demand; ensure it exists before any SELECT references it.
  private fun ensureDeliveryNotePdfsTable(date: LocalDate): String {
    val pdfTable = LegacyTableNames.deliveryNotePdfsTable(date)
    legacyTableInitializer.createIfNotExists(pdfTable, LegacyTableNames.DELIVERY_NOTE_PDFS_TEMPLATE)
    return pdfTable
  }

  companion object {
    private const val SUPER_PLASTICIZER_COLUMN = "entladungfliessmittel"
    private const val ACCEPTED_AT_COLUMN = "instrackangenommendatumzeit"
    private const val DEFAULT_DRIVER_NOTE_COLUMN = "a.auftragsinfo"
    private const val DRIVER_NOTE_ALIAS = "driver_note"
    private val DRIVER_NOTE_COLUMN_PATTERN = Regex("^(a|ls)\\.[a-z_][a-z0-9_]*$")
  }
}
