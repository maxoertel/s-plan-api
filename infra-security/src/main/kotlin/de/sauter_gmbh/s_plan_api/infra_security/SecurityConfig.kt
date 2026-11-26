package de.sauter_gmbh.s_plan_api.infra_security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
internal class SecurityConfig {

  @Bean
  fun securityFilterChain(http: HttpSecurity): SecurityFilterChain =
    http
      .csrf { it.disable() }
      .cors {}
      .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
      .authorizeHttpRequests { it.anyRequest().authenticated() }
      .oauth2ResourceServer { it.jwt {} }
      .build()

  @Bean("corsConfigurationSource")
  @Profile("local")
  fun localCorsConfigurationSource(): CorsConfigurationSource {
    val config =
      CorsConfiguration().apply {
        addAllowedOriginPattern("*")
        allowedMethods = listOf("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        addAllowedHeader("*")
        allowCredentials = true
      }
    return UrlBasedCorsConfigurationSource().apply { registerCorsConfiguration("/**", config) }
  }

  @Bean("corsConfigurationSource")
  @Profile("!local")
  fun prodCorsConfigurationSource(): CorsConfigurationSource {
    val config =
      CorsConfiguration().apply {
        allowedOrigins =
          listOf(
            "https://s-plan-web-dev.sauter-gmbh.online",
            "https://s-plan-web-demo.sauter-gmbh.online"
          )
        allowedMethods = listOf("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        addAllowedHeader("*")
        allowCredentials = true
        maxAge = 3600
      }
    return UrlBasedCorsConfigurationSource().apply { registerCorsConfiguration("/**", config) }
  }
}
