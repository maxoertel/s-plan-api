package de.sauter_gmbh.s_plan_api.infra_db.table

import de.sauter_gmbh.s_plan_api.infra_db.validation.requireSafeSqlIdentifier
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

// Daily/monthly legacy tables are created on demand from a pre-populated template table so that
// indexes, defaults, and constraints stay aligned with the template — `INCLUDING ALL` copies them.
@Component
class LegacyTableInitializer(private val jdbcTemplate: JdbcTemplate) {

  fun createIfNotExists(targetTable: String, templateTable: String) {
    val safeTarget = requireSafeSqlIdentifier(targetTable)
    val safeTemplate = requireSafeSqlIdentifier(templateTable)
    jdbcTemplate.execute(
      "CREATE TABLE IF NOT EXISTS $safeTarget (LIKE $safeTemplate INCLUDING ALL)"
    )
  }
}
