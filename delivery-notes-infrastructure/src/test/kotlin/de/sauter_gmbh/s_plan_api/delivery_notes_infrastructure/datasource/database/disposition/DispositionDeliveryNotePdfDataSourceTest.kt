package de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.datasource.database.disposition

import de.sauter_gmbh.s_plan_api.infra_db.table.LegacyTableInitializer
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import java.sql.Date
import java.sql.Time
import java.time.LocalDate
import java.time.LocalTime
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper

class DispositionDeliveryNotePdfDataSourceTest {

  private val jdbcTemplate = mockk<JdbcTemplate>()
  private val legacyTableInitializer =
    mockk<LegacyTableInitializer> { every { createIfNotExists(any(), any()) } just runs }

  private val sut by lazy {
    DispositionDeliveryNotePdfDataSource(jdbcTemplate, legacyTableInitializer)
  }

  private val date = LocalDate.of(2026, 9, 8)
  private val time = LocalTime.of(16, 30)
  private val creationTime = LocalTime.of(16, 30, 44, 962_000_000)
  private val storageTime = LocalTime.of(16, 30, 44, 962_000_000)

  @Test
  fun `findBytes should bind creation and storage time as LocalTime to keep microseconds`() {
    // given:
    val sql = slot<String>()
    val args = mutableListOf<Any?>()
    every {
      jdbcTemplate.query(
        capture(sql),
        any<RowMapper<ByteArray?>>(),
        *varargAllNullable {
          args.add(it)
          true
        }
      )
    } returns listOf(byteArrayOf(1, 2, 3))

    // when:
    val result = findBytes()

    // then: the time(6) columns are compared exactly, with LocalTime bound (not java.sql.Time)
    assertContentEquals(byteArrayOf(1, 2, 3), result)
    assertContains(sql.captured, "AND erstellungdatum = ? AND erstellungzeit = ?")
    assertContains(sql.captured, "AND ablagedatum = ? AND ablagezeit = ?")
    assertContains(sql.captured, "FROM s_20260908pdf_lieferscheine")
    assertEquals(
      listOf<Any?>(
        1L,
        Date.valueOf(date),
        Time.valueOf(time),
        526623,
        1,
        Date.valueOf(date),
        creationTime,
        Date.valueOf(date),
        storageTime,
      ),
      args,
    )
  }

  @Test
  fun `findBytes should return null and run diagnostics when no row matches`() {
    // given:
    every { jdbcTemplate.query(any<String>(), any<RowMapper<Any?>>(), *anyVararg()) } returns
      emptyList()

    // when:
    val result = findBytes()

    // then:
    assertNull(result)
  }

  @Test
  fun `findBytes should return null when the row exists but has no pdf bytes`() {
    // given:
    every { jdbcTemplate.query(any<String>(), any<RowMapper<Any?>>(), *anyVararg()) } returns
      listOf(null)

    // when:
    val result = findBytes()

    // then:
    assertNull(result)
  }

  private fun findBytes(): ByteArray? =
    sut.findBytes(
      factoryNr = 1L,
      date = date,
      time = time,
      deliveryNoteNr = 526623,
      deliveryNoteCircle = 1,
      creationDate = date,
      creationTime = creationTime,
      storageDate = date,
      storageTime = storageTime,
    )
}
