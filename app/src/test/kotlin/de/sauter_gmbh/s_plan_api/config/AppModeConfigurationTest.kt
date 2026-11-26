package de.sauter_gmbh.s_plan_api.config

import de.sauter_gmbh.s_plan_api.shared_core.mode.AppMode
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import org.junit.jupiter.api.Test

class AppModeConfigurationTest {

  private val sut = AppModeConfiguration()

  @Test
  fun `should resolve s-plan mode`() {
    // when:
    val result = sut.appMode("s-plan")

    // then:
    assertEquals(AppMode.S_PLAN, result)
  }

  @Test
  fun `should resolve s-work mode ignoring surrounding whitespace`() {
    // when:
    val result = sut.appMode(" s-work ")

    // then:
    assertEquals(AppMode.S_WORK, result)
  }

  @Test
  fun `should fail when mode is unknown`() {
    // when / then:
    assertFailsWith<IllegalStateException> { sut.appMode("s-track") }
  }
}
