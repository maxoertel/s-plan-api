package de.sauter_gmbh.s_plan_api.infra_db

import java.sql.Timestamp
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

internal class DbTimestampConverterTest {

  private val berlin = DbTimestampConverter("Europe/Berlin")
  private val utc = DbTimestampConverter("UTC")

  @Test
  fun `should encode instant as summer wall-clock when zone is ahead of UTC`() {
    // given: an instant during central european summer time (UTC+2)
    val epochMillis = Instant.parse("2026-06-01T10:00:00Z").toEpochMilli()

    // when:
    val result = berlin.toTimestamp(epochMillis)

    // then: stored wall-clock is shifted by +2h
    assertEquals(LocalDateTime.of(2026, 6, 1, 12, 0, 0), result.toLocalDateTime())
  }

  @Test
  fun `should encode instant as winter wall-clock when zone is ahead of UTC`() {
    // given: an instant during central european winter time (UTC+1)
    val epochMillis = Instant.parse("2026-01-01T10:00:00Z").toEpochMilli()

    // when:
    val result = berlin.toTimestamp(epochMillis)

    // then: stored wall-clock is shifted by +1h (DST handled per instant)
    assertEquals(LocalDateTime.of(2026, 1, 1, 11, 0, 0), result.toLocalDateTime())
  }

  @Test
  fun `should return current wall-clock time in the configured zone`() {
    // given:
    val before = LocalDateTime.now(ZoneId.of("Europe/Berlin"))

    // when:
    val result = berlin.nowWallClock()

    // then: within a few seconds of the zone's wall-clock, never the JVM default zone
    val after = LocalDateTime.now(ZoneId.of("Europe/Berlin"))
    assertTrue(!result.isBefore(before) && !result.isAfter(after))
    assertTrue(Duration.between(before, result).abs() < Duration.ofSeconds(5))
  }

  @Test
  fun `should decode wall-clock back into absolute instant using the zone`() {
    // given: a wall-clock value as it would sit in the column (Berlin summer time)
    val stored = Timestamp.valueOf(LocalDateTime.of(2026, 6, 1, 12, 0, 0))

    // when:
    val result = berlin.toEpochMillis(stored)

    // then:
    assertEquals(Instant.parse("2026-06-01T10:00:00Z").toEpochMilli(), result)
  }

  @Test
  fun `should preserve the original instant on a write-then-read round trip`() {
    // given:
    val epochMillis = 1_700_000_000_000L

    // when:
    val result = berlin.toEpochMillis(berlin.toTimestamp(epochMillis))

    // then:
    assertEquals(epochMillis, result)
  }

  @Test
  fun `should encode the same instant differently for different zones`() {
    // given:
    val epochMillis = Instant.parse("2026-06-01T10:00:00Z").toEpochMilli()

    // when:
    val berlinWallClock = berlin.toTimestamp(epochMillis).toLocalDateTime()
    val utcWallClock = utc.toTimestamp(epochMillis).toLocalDateTime()

    // then:
    assertEquals(LocalDateTime.of(2026, 6, 1, 12, 0, 0), berlinWallClock)
    assertEquals(LocalDateTime.of(2026, 6, 1, 10, 0, 0), utcWallClock)
  }

  @Test
  fun `should fail fast when the configured zone is invalid`() {
    assertFailsWith<IllegalStateException> { DbTimestampConverter("Not/AZone") }
  }

  @Test
  fun `should fail fast when the configured zone is blank`() {
    assertFailsWith<IllegalStateException> { DbTimestampConverter("") }
  }

  @Test
  fun `should recover the same instant regardless of the configured zone`() {
    // given:
    val epochMillis = 1_700_000_000_000L

    // when:
    val viaBerlin = berlin.toEpochMillis(berlin.toTimestamp(epochMillis))
    val viaUtc = utc.toEpochMillis(utc.toTimestamp(epochMillis))

    // then:
    assertEquals(epochMillis, viaBerlin)
    assertEquals(epochMillis, viaUtc)
  }
}
