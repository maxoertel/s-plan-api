package de.sauter_gmbh.s_plan_api.mission_infrastructure.datasource.database.disposition

import de.sauter_gmbh.s_plan_api.infra_db.DbTimestampConverter
import de.sauter_gmbh.s_plan_api.infra_db.table.LegacyTableInitializer
import io.mockk.every
import io.mockk.mockk
import java.sql.ResultSet
import java.sql.Timestamp
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertFailsWith
import org.springframework.jdbc.core.JdbcTemplate

class DispositionMissionDataSourceTest {

  private val jdbcTemplate = mockk<JdbcTemplate>()
  private val legacyTableInitializer = mockk<LegacyTableInitializer>(relaxed = true)
  private val timestampConverter = mockk<DbTimestampConverter>(relaxed = true)

  private var driverNoteColumn = ""

  private val sut by lazy { createSut(driverNoteColumn) }

  private fun createSut(driverNoteColumn: String) =
    DispositionMissionDataSource(
      jdbcTemplate = jdbcTemplate,
      legacyTableInitializer = legacyTableInitializer,
      timestampConverter = timestampConverter,
      superPlasticizerColumnExists = false,
      acceptedAtColumnExists = false,
      driverNoteColumn = driverNoteColumn,
    )

  private val date = LocalDate.of(2026, 10, 11)

  @Test
  fun `should reject a driver note column that is not a plain order or delivery note column`() {
    listOf("b.ort", "auftragsinfo", "a.auftragsinfo; DROP TABLE x", "a.", "ls.x y").forEach {
      // when + then:
      assertFailsWith<IllegalArgumentException> { createSut(it) }
    }
  }

  private fun resultSet(lsnummerfremd: Int?): ResultSet =
    mockk<ResultSet>(relaxed = true) {
      every { getLong("werksnummer") } returns 1L
      every { getInt("werksnummer") } returns 1
      every { getInt("auftragnr") } returns 200
      every { getInt("ladenr") } returns 1
      every { getTimestamp("datum") } returns Timestamp.valueOf(date.atStartOfDay())
      every { getTimestamp(not("datum")) } returns null
      every { getLong("lsnummer") } returns 4711L
      every { getObject("lsnummerfremd") } returns lsnummerfremd
    }
}
