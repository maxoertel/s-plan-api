package de.sauter_gmbh.s_plan_api.infra_db

import java.sql.Timestamp
import java.time.DateTimeException
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/**
 * Converts between absolute UTC epoch millis and the timezone-naive `TIMESTAMP` columns of the
 * legacy schema. Shared by every datasource that persists or reads such columns.
 *
 * Those columns store a bare wall-clock value with no zone attached. To make the stored value
 * independent of the server's own timezone, we always encode and decode it through a single
 * configurable zone ([zoneId]). As long as the same zone is used on write and read, the round-trip
 * recovers the exact original instant on any server. The chosen zone must match the convention the
 * database already uses, so that other consumers reading the column directly see the expected
 * wall-clock time.
 *
 * The zone is **required**: every deployment must set `APP_DB_TIMEZONE` explicitly so the storage
 * convention is a conscious, per-environment decision rather than an accidental default. Startup
 * fails fast if it is missing or not a valid IANA zone id.
 *
 * The API itself always exchanges absolute UTC epoch millis; this codec only governs how those
 * instants are laid down as wall-clock digits inside the database.
 */
@Component
class DbTimestampConverter(
  @Value("\${app.db-timezone}") zoneId: String,
) {

  private val zone: ZoneId =
    try {
      ZoneId.of(zoneId)
    } catch (e: DateTimeException) {
      throw IllegalStateException(
        "Invalid app.db-timezone '$zoneId'. Set APP_DB_TIMEZONE to a valid IANA zone id " +
          "(e.g. Europe/Berlin).",
        e,
      )
    }

  /** Encodes an absolute UTC instant into the wall-clock value to store in a `TIMESTAMP` column. */
  fun toTimestamp(epochMillis: Long): Timestamp =
    Timestamp.valueOf(Instant.ofEpochMilli(epochMillis).atZone(zone).toLocalDateTime())

  /**
   * Decodes a wall-clock value read from a `TIMESTAMP` column back into an absolute UTC instant.
   */
  fun toEpochMillis(timestamp: Timestamp): Long =
    timestamp.toLocalDateTime().atZone(zone).toInstant().toEpochMilli()

  /**
   * The current wall-clock time in the configured zone, for `DATE`/`TIME` columns that are filled
   * with "now" and must follow the same convention as the `TIMESTAMP` columns regardless of the
   * server's own timezone.
   */
  fun nowWallClock(): LocalDateTime = LocalDateTime.now(zone)
}
