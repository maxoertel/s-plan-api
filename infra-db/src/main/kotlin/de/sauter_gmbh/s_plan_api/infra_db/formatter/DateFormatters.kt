package de.sauter_gmbh.s_plan_api.infra_db.formatter

import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

val MONTH_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMM")
val DATE_ID_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
val TIME_ID_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("HHmmss")

/** `HHmmss` followed by exactly six fraction digits (microseconds), e.g. `163044962000`. */
val TIME_MICROS_ID_FORMATTER: DateTimeFormatter =
  DateTimeFormatterBuilder()
    .appendPattern("HHmmss")
    .appendValue(ChronoField.MICRO_OF_SECOND, 6)
    .toFormatter()
