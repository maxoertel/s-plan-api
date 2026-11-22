package de.sauter_gmbh.s_plan_api.infra_db.validation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SqlIdentifierPatternsTest {

  @Test
  fun `should accept lowercase snake_case identifiers`() {
    assertEquals("users", requireSafeSqlIdentifier("users"))
    assertEquals("a_20260416auftraege", requireSafeSqlIdentifier("a_20260416auftraege"))
    assertEquals("s_202604lieferscheine", requireSafeSqlIdentifier("s_202604lieferscheine"))
    assertEquals("fzstatusbeladung", requireSafeSqlIdentifier("fzstatusbeladung"))
  }

  @Test
  fun `should reject empty identifier`() {
    assertFailsWith<IllegalArgumentException> { requireSafeSqlIdentifier("") }
  }

  @Test
  fun `should reject identifiers starting with digit`() {
    assertFailsWith<IllegalArgumentException> { requireSafeSqlIdentifier("1table") }
  }

  @Test
  fun `should reject identifiers starting with underscore`() {
    assertFailsWith<IllegalArgumentException> { requireSafeSqlIdentifier("_table") }
  }

  @Test
  fun `should reject uppercase characters`() {
    assertFailsWith<IllegalArgumentException> { requireSafeSqlIdentifier("Users") }
    assertFailsWith<IllegalArgumentException> { requireSafeSqlIdentifier("USERS") }
  }

  @Test
  fun `should reject whitespace`() {
    assertFailsWith<IllegalArgumentException> { requireSafeSqlIdentifier("table name") }
    assertFailsWith<IllegalArgumentException> { requireSafeSqlIdentifier(" users") }
    assertFailsWith<IllegalArgumentException> { requireSafeSqlIdentifier("users ") }
    assertFailsWith<IllegalArgumentException> { requireSafeSqlIdentifier("users\t") }
    assertFailsWith<IllegalArgumentException> { requireSafeSqlIdentifier("users\n") }
  }

  @Test
  fun `should reject sql injection attempts`() {
    val injectionAttempts =
      listOf(
        "users; DROP TABLE users",
        "users;--",
        "users' OR '1'='1",
        "users\" OR 1=1",
        "users/*",
        "users--",
        "users`",
        "users(",
        "users)",
        "users,users",
        "users.users",
        "'users'",
        "\"users\"",
      )
    injectionAttempts.forEach { candidate ->
      assertFailsWith<IllegalArgumentException>("Expected rejection of: $candidate") {
        requireSafeSqlIdentifier(candidate)
      }
    }
  }

  @Test
  fun `should reject non-ascii characters`() {
    assertFailsWith<IllegalArgumentException> { requireSafeSqlIdentifier("tabellé") }
    assertFailsWith<IllegalArgumentException> { requireSafeSqlIdentifier("tabelleü") }
  }

  @Test
  fun `should reject null byte injection`() {
    assertFailsWith<IllegalArgumentException> { requireSafeSqlIdentifier("users\u0000") }
  }
}
