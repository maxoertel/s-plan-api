package de.sauter_gmbh.s_plan_api.config

import de.sauter_gmbh.s_plan_api.shared_core.mode.AppMode
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
class AppModeConfiguration {

  @Bean
  fun appMode(@Value("\${app.mode}") value: String): AppMode =
    AppMode.entries.firstOrNull { it.value == value.trim() }
      ?: throw IllegalStateException(
        "Invalid app.mode '$value'. Set APP_MODE to one of: " +
          AppMode.entries.joinToString { it.value }
      )
}
