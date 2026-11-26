package de.sauter_gmbh.s_plan_api.infra_db.condition

import de.sauter_gmbh.s_plan_api.shared_core.mode.AppMode
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.springframework.context.annotation.ConditionContext
import org.springframework.core.type.AnnotatedTypeMetadata

class OnAppModeConditionTest {

  private val sut = OnAppModeCondition()

  @Test
  fun `should match s-plan when app mode is s-plan`() {
    // given:
    val context = contextWithAppMode("s-plan")

    // when:
    val result = sut.matches(context, metadataFor(AppMode.S_PLAN))

    // then:
    assertTrue(result)
  }

  @Test
  fun `should match s-work when app mode is s-work`() {
    // given:
    val context = contextWithAppMode("s-work")

    // when:
    val result = sut.matches(context, metadataFor(AppMode.S_WORK))

    // then:
    assertTrue(result)
  }

  @Test
  fun `should not match when app mode differs`() {
    // given:
    val context = contextWithAppMode("s-plan")

    // when:
    val result = sut.matches(context, metadataFor(AppMode.S_WORK))

    // then:
    assertFalse(result)
  }

  @Test
  fun `should not match when app mode is missing`() {
    // given:
    val context = contextWithAppMode(null)

    // when:
    val result = sut.matches(context, metadataFor(AppMode.S_PLAN))

    // then:
    assertFalse(result)
  }

  @Test
  fun `should not match when annotation attributes are missing`() {
    // given:
    val context = contextWithAppMode("s-plan")
    val metadata = mockk<AnnotatedTypeMetadata>()
    every { metadata.getAnnotationAttributes(ConditionalOnAppMode::class.java.name) } returns null

    // when:
    val result = sut.matches(context, metadata)

    // then:
    assertFalse(result)
  }

  private fun contextWithAppMode(appMode: String?): ConditionContext {
    val context = mockk<ConditionContext>()
    every { context.environment.getProperty("app.mode") } returns appMode
    return context
  }

  private fun metadataFor(appMode: AppMode): AnnotatedTypeMetadata {
    val metadata = mockk<AnnotatedTypeMetadata>()
    every { metadata.getAnnotationAttributes(ConditionalOnAppMode::class.java.name) } returns
      mapOf("value" to appMode)
    return metadata
  }
}
