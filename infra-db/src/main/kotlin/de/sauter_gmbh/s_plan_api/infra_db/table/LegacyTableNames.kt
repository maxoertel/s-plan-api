package de.sauter_gmbh.s_plan_api.infra_db.table

import de.sauter_gmbh.s_plan_api.infra_db.formatter.DATE_ID_FORMATTER
import de.sauter_gmbh.s_plan_api.infra_db.formatter.MONTH_FORMATTER
import de.sauter_gmbh.s_plan_api.infra_db.validation.requireSafeSqlIdentifier
import java.time.LocalDate

// Legacy table names are interpolated into SQL (not parameterised), so they are built only here
// from strongly-typed inputs and re-validated as a defence-in-depth measure against SQL injection.
object LegacyTableNames {

  const val DELIVERY_NOTE_PDFS_TEMPLATE: String = "vorlage_jjjjmmdd_pdf_lieferscheine"

  fun ordersTable(date: LocalDate): String =
    requireSafeSqlIdentifier("a_${date.format(DATE_ID_FORMATTER)}auftraege")

  fun deliveryNotesTable(date: LocalDate): String =
    requireSafeSqlIdentifier("s_${date.format(MONTH_FORMATTER)}lieferscheine")

  fun deliveryNotePdfsTable(date: LocalDate): String =
    requireSafeSqlIdentifier("s_${date.format(DATE_ID_FORMATTER)}pdf_lieferscheine")
}
