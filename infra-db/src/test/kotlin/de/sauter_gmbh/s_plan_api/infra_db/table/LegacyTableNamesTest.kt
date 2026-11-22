package de.sauter_gmbh.s_plan_api.infra_db.table

import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

class LegacyTableNamesTest {

  @Test
  fun `ordersTable should build daily table name with yyyyMMdd date`() {
    val name = LegacyTableNames.ordersTable(LocalDate.of(2026, 4, 16))

    assertEquals("a_20260416auftraege", name)
  }

  @Test
  fun `ordersTable should zero-pad month and day`() {
    val name = LegacyTableNames.ordersTable(LocalDate.of(2026, 1, 3))

    assertEquals("a_20260103auftraege", name)
  }

  @Test
  fun `deliveryNotesTable should build monthly table name with yyyyMM date`() {
    val name = LegacyTableNames.deliveryNotesTable(LocalDate.of(2026, 4, 16))

    assertEquals("s_202604lieferscheine", name)
  }

  @Test
  fun `deliveryNotesTable should zero-pad month`() {
    val name = LegacyTableNames.deliveryNotesTable(LocalDate.of(2026, 1, 3))

    assertEquals("s_202601lieferscheine", name)
  }

  @Test
  fun `deliveryNotePdfsTable should build daily table name with yyyyMMdd date`() {
    val name = LegacyTableNames.deliveryNotePdfsTable(LocalDate.of(2026, 4, 17))

    assertEquals("s_20260417pdf_lieferscheine", name)
  }

  @Test
  fun `deliveryNotePdfsTable should zero-pad month and day`() {
    val name = LegacyTableNames.deliveryNotePdfsTable(LocalDate.of(2026, 1, 3))

    assertEquals("s_20260103pdf_lieferscheine", name)
  }
}
