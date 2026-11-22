package de.sauter_gmbh.s_plan_api.infra_db.table

import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import org.springframework.jdbc.core.JdbcTemplate

class LegacyTableInitializerTest {

  private val jdbcTemplate = mockk<JdbcTemplate>(relaxUnitFun = true)

  private val sut by lazy { LegacyTableInitializer(jdbcTemplate) }

  @Test
  fun `should execute CREATE TABLE IF NOT EXISTS with LIKE INCLUDING ALL when names are valid`() {
    // given:
    val capturedSql = slot<String>()

    // when:
    sut.createIfNotExists(
      targetTable = "s_20260417pdf_lieferscheine",
      templateTable = "vorlage_jjjjmmdd_pdf_lieferscheine",
    )

    // then:
    verify { jdbcTemplate.execute(capture(capturedSql)) }
    assertEquals(
      "CREATE TABLE IF NOT EXISTS s_20260417pdf_lieferscheine " +
        "(LIKE vorlage_jjjjmmdd_pdf_lieferscheine INCLUDING ALL)",
      capturedSql.captured,
    )
  }

  @Test
  fun `should reject unsafe target table identifier`() {
    // given:
    val unsafeTarget = "pdf_lieferscheine; DROP TABLE users"

    // when / then:
    assertFailsWith<IllegalArgumentException> {
      sut.createIfNotExists(
        targetTable = unsafeTarget,
        templateTable = "vorlage_jjjjmmdd_pdf_lieferscheine",
      )
    }
  }

  @Test
  fun `should reject unsafe template table identifier`() {
    // given:
    val unsafeTemplate = "vorlage\"; DELETE"

    // when / then:
    assertFailsWith<IllegalArgumentException> {
      sut.createIfNotExists(
        targetTable = "s_20260417pdf_lieferscheine",
        templateTable = unsafeTemplate,
      )
    }
  }
}
