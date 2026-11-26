package de.sauter_gmbh.s_plan_api.infra_db.validation

val TABLE_AND_COLUMN_NAME_PATTERN: Regex = Regex("^[a-z][a-z0-9_]*$")

fun requireSafeSqlIdentifier(identifier: String): String {
  require(TABLE_AND_COLUMN_NAME_PATTERN.matches(identifier)) {
    "Unsafe SQL identifier: $identifier"
  }
  return identifier
}
