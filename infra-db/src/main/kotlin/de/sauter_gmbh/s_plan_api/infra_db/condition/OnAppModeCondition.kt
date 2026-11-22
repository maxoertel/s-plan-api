package de.sauter_gmbh.s_plan_api.infra_db.condition

import de.sauter_gmbh.s_plan_api.shared_core.mode.AppMode
import org.springframework.context.annotation.Condition
import org.springframework.context.annotation.ConditionContext
import org.springframework.core.type.AnnotatedTypeMetadata

class OnAppModeCondition : Condition {

  override fun matches(context: ConditionContext, metadata: AnnotatedTypeMetadata): Boolean {
    val attributes =
      metadata.getAnnotationAttributes(ConditionalOnAppMode::class.java.name) ?: return false

    val expected = attributes["value"] as AppMode

    val actual = context.environment.getProperty("app.mode")?.trim() ?: return false

    return actual == expected.value
  }
}
