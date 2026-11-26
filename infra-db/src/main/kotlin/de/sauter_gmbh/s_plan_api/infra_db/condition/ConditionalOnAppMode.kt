package de.sauter_gmbh.s_plan_api.infra_db.condition

import de.sauter_gmbh.s_plan_api.shared_core.mode.AppMode
import org.springframework.context.annotation.Conditional

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Conditional(OnAppModeCondition::class)
annotation class ConditionalOnAppMode(val value: AppMode)
