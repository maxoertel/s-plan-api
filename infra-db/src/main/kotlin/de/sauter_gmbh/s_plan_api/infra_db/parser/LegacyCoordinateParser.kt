package de.sauter_gmbh.s_plan_api.infra_db.parser

// Legacy varchar columns store coordinates as doubles with the German locale decimal separator
// stripped, so "47.696007" became "47696007". The original integer part is preserved at the
// start of the string: lat is always two digits in Europe; lon is one digit when the value starts
// with 5-9 (5°-9°) and two digits when it starts with 1 (10°-19°).

enum class CoordinateKind {
  LATITUDE,
  LONGITUDE,
}

fun parseLegacyCoordinate(raw: String?, kind: CoordinateKind): Double? {
  val trimmed = raw?.trim()?.takeIf { it.isNotEmpty() } ?: return null
  if ('.' in trimmed) return trimmed.toDoubleOrNull()

  val normalized = trimmed.replace(',', '.')
  if ('.' in normalized) return normalized.toDoubleOrNull()

  val isNegative = trimmed.startsWith("-")
  val digits = if (isNegative) trimmed.drop(1) else trimmed
  if (digits.isEmpty() || !digits.all(Char::isDigit)) return null

  val intDigits =
    when (kind) {
      CoordinateKind.LATITUDE -> 2
      CoordinateKind.LONGITUDE -> if (digits.first() == '1') 2 else 1
    }
  if (digits.length <= intDigits) return null

  val withDecimal = digits.substring(0, intDigits) + "." + digits.substring(intDigits)
  val parsed = withDecimal.toDoubleOrNull() ?: return null

  return if (isNegative) -parsed else parsed
}
