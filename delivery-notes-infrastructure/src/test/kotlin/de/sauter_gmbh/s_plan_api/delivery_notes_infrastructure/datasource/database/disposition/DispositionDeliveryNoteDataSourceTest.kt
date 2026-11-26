package de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.datasource.database.disposition

import de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.datasource.entity.DeliveryNoteEntity
import de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.parser.DeliveryNoteIdParser
import de.sauter_gmbh.s_plan_api.infra_db.DbTimestampConverter
import de.sauter_gmbh.s_plan_api.infra_db.table.LegacyTableInitializer
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import java.sql.Date
import java.sql.ResultSet
import java.sql.Time
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalTime
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper

class DispositionDeliveryNoteDataSourceTest {

  private val jdbcTemplate = mockk<JdbcTemplate>()
  private val timestampConverter = mockk<DbTimestampConverter>()
  private val legacyTableInitializer = mockk<LegacyTableInitializer>(relaxUnitFun = true)

  private val sut by lazy { createSut(waitingTimeColumnExists = false) }

  private fun createSut(waitingTimeColumnExists: Boolean) =
    DispositionDeliveryNoteDataSource(
      jdbcTemplate,
      DeliveryNoteIdParser(),
      timestampConverter,
      legacyTableInitializer,
      superPlasticizerColumnExists = false,
      acceptedAtColumnExists = false,
      waitingTimeColumnExists = waitingTimeColumnExists,
    )

  private val deliveryNoteId = "100_55_20260418_083000"
  private val date = LocalDate.of(2026, 4, 18)
  private val time = LocalTime.of(8, 30, 0)

  @Test
  fun `updateStatusTimes should write the corrected times to the delivery note and its order`() {
    // given: the on-site status is stored on the delivery note's own day
    givenStoredRow(orderNr = 200, loadNr = 1, storedDate = date)
    val statements = captureUpdates()

    // when:
    sut.updateStatusTimes(deliveryNoteId, mapOf("fzstatusbaustelle" to LocalTime.of(9, 15)))

    // then: both tables receive the same timestamp
    val expectedTimestamp = Timestamp.valueOf("2026-04-18 09:15:00")
    assertEquals(2, statements.size)

    val deliveryNoteUpdate = statements[0]
    assertContains(deliveryNoteUpdate.sql, "UPDATE s_202604lieferscheine")
    assertContains(deliveryNoteUpdate.sql, "SET fzstatusbaustelle = ?")
    assertContains(
      deliveryNoteUpdate.sql,
      "WHERE werksnummer = ? AND lsnummer = ? AND datum = ? AND zeit = ?",
    )
    assertEquals(
      listOf<Any?>(expectedTimestamp, 100L, 55, Date.valueOf(date), Time.valueOf(time)),
      deliveryNoteUpdate.arguments,
    )

    val orderUpdate = statements[1]
    assertContains(orderUpdate.sql, "UPDATE a_20260418auftraege")
    assertContains(orderUpdate.sql, "SET fzstatusbaustelle = ?")
    assertContains(orderUpdate.sql, "WHERE werksnummer = ? AND auftragnr = ? AND ladenr = ?")
    assertEquals(listOf<Any?>(expectedTimestamp, 100L, 200, 1), orderUpdate.arguments)
  }

  @Test
  fun `updateStatusTimes should keep the day a status column already carries`() {
    // given: the vehicle returned after midnight, so the stored status sits on the next day
    givenStoredRow(orderNr = 200, loadNr = 1, storedDate = date.plusDays(1))
    val statements = captureUpdates()

    // when:
    sut.updateStatusTimes(deliveryNoteId, mapOf("fzstatusaufwerk" to LocalTime.of(0, 20)))

    // then: the correction stays on that day instead of jumping back to the delivery note's day
    val expectedTimestamp = Timestamp.valueOf("2026-04-19 00:20:00")
    assertEquals(expectedTimestamp, statements[0].arguments.first())
    assertEquals(expectedTimestamp, statements[1].arguments.first())
  }

  @Test
  fun `updateStatusTimes should only write the delivery note when the order cannot be addressed`() {
    // given:
    givenStoredRow(orderNr = null, loadNr = null, storedDate = date)
    val statements = captureUpdates()

    // when:
    sut.updateStatusTimes(deliveryNoteId, mapOf("fzstatusbaustelle" to LocalTime.of(9, 15)))

    // then:
    assertEquals(1, statements.size)
    assertContains(statements[0].sql, "UPDATE s_202604lieferscheine")
  }

  @Test
  fun `findByDate should read the site coordinates from the site master`() {
    // given:
    val sql = mutableListOf<String>()
    every { jdbcTemplate.query(capture(sql), any<RowMapper<Any?>>(), *anyVararg()) } returns
      emptyList()

    // when:
    sut.findByDate(date)

    // then:
    assertContains(sql.single(), "site.site_latitude, site.site_longitude")
    assertContains(sql.single(), "FROM baust b")
    assertContains(sql.single(), "WHERE b.kundennr = dn.kundennr AND b.baustnr = dn.baustnr")
  }

  @Test
  fun `findByDate should derive the collective flag from the newest own pdf`() {
    // given:
    val sql = mutableListOf<String>()
    every { jdbcTemplate.query(capture(sql), any<RowMapper<Any?>>(), *anyVararg()) } returns
      emptyList()

    // when:
    sut.findByDate(date)

    // then:
    val pdfTable = "s_20260418pdf_lieferscheine"
    assertContains(sql.single(), "FROM $pdfTable p")
    assertContains(sql.single(), "AND p.datenquelle LIKE 's-track%'")
    assertContains(sql.single(), "COALESCE(own_pdf.datenquelle = 's-track-sammel', FALSE)")
    verify(exactly = 1) {
      legacyTableInitializer.createIfNotExists(pdfTable, "vorlage_jjjjmmdd_pdf_lieferscheine")
    }
  }

  @Test
  fun `findByDate should derive the driver placeholder from a customer placeholder with the same checksum`() {
    // given:
    val sql = mutableListOf<String>()
    every { jdbcTemplate.query(capture(sql), any<RowMapper<Any?>>(), *anyVararg()) } returns
      emptyList()

    // when:
    sut.findByDate(date)

    // then:
    assertContains(
      sql.single(),
      "COALESCE(lieferscheinfahrerpruefsumme = lieferscheinabnehmerpruefsumme, FALSE)",
    )
    assertContains(sql.single(), "AS driver_signature_placeholder")
  }

  @Test
  fun `findByDate should select a null waiting time when the waiting time column does not exist`() {
    // given:
    val sql = mutableListOf<String>()
    every { jdbcTemplate.query(capture(sql), any<RowMapper<Any?>>(), *anyVararg()) } returns
      emptyList()

    // when:
    sut.findByDate(date)

    // then:
    assertContains(sql.single(), "CAST(NULL AS double precision) AS entladungwartezeit")
  }

  @Test
  fun `findByDate should select the waiting time column when it exists`() {
    // given:
    val sql = mutableListOf<String>()
    every { jdbcTemplate.query(capture(sql), any<RowMapper<Any?>>(), *anyVararg()) } returns
      emptyList()

    // when:
    createSut(waitingTimeColumnExists = true).findByDate(date)

    // then:
    assertContains(sql.single(), "entladungwartezeit")
    assertFalse(sql.single().contains("AS entladungwartezeit"))
  }

  @Test
  fun `findByDate should map the waiting time and the driver placeholder`() {
    // given:
    val rowMapper = slot<RowMapper<Any?>>()
    every { jdbcTemplate.query(any<String>(), capture(rowMapper), *anyVararg()) } returns
      emptyList()
    sut.findByDate(date)
    val resultSet =
      mockk<ResultSet>(relaxed = true) {
        every { getDate("datum") } returns Date.valueOf(date)
        every { getTime("zeit") } returns Time.valueOf(time)
        every { getTimestamp(any<String>()) } returns null
        every { getObject(any<String>()) } returns null
        every { getObject("entladungwartezeit") } returns 15.0
        every { getBoolean("driver_signature_placeholder") } returns true
      }

    // when:
    val entity = rowMapper.captured.mapRow(resultSet, 0) as DeliveryNoteEntity

    // then:
    assertEquals(15.0, entity.waitingTimeMinutes)
    assertTrue(entity.driverSignaturePlaceholder)
  }

  @Test
  fun `updateStatusTimes should touch no table when there is no time to write`() {
    // given + when:
    sut.updateStatusTimes(deliveryNoteId, emptyMap())

    // then:
    verify(exactly = 0) { jdbcTemplate.update(any<String>(), *anyVararg()) }
  }

  private fun givenStoredRow(orderNr: Int?, loadNr: Int?, storedDate: LocalDate) {
    val resultSet =
      mockk<java.sql.ResultSet> {
        every { getObject("auftragnr") } returns orderNr
        every { getObject("ladenr") } returns loadNr
        every { getTimestamp(any<String>()) } returns Timestamp.valueOf(storedDate.atTime(9, 0, 0))
      }
    every { jdbcTemplate.query(any<String>(), any<RowMapper<Any?>>(), *anyVararg()) } answers
      {
        listOf(secondArg<RowMapper<Any?>>().mapRow(resultSet, 1))
      }
  }

  private fun captureUpdates(): List<Statement> {
    val statements = mutableListOf<Statement>()
    every { jdbcTemplate.update(any<String>(), *anyVararg()) } answers
      {
        val arguments =
          args.drop(1).flatMap { argument ->
            if (argument is Array<*>) argument.toList() else listOf(argument)
          }
        statements.add(Statement(firstArg(), arguments))
        1
      }
    return statements
  }

  private data class Statement(val sql: String, val arguments: List<Any?>)
}
