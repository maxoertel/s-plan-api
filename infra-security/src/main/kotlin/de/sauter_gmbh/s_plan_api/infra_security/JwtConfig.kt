package de.sauter_gmbh.s_plan_api.infra_security

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.jwk.source.JWKSourceBuilder
import com.nimbusds.jose.proc.JWSVerificationKeySelector
import com.nimbusds.jose.proc.SecurityContext
import com.nimbusds.jwt.proc.DefaultJWTProcessor
import java.net.URL
import java.time.Duration
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator
import org.springframework.security.oauth2.jwt.JwtClaimValidator
import org.springframework.security.oauth2.jwt.JwtValidators
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder

@Configuration
internal class JwtConfig {

  @Bean
  fun jwtDecoder(
    @Value("\${spring.security.oauth2.resourceserver.jwt.issuer-uri}") issuerUri: String,
    @Value("\${spring.security.oauth2.resourceserver.jwt.audiences}") audience: String,
  ): NimbusJwtDecoder {
    val jwkSetUri = issuerUri.trimEnd('/') + "/.well-known/jwks.json"

    val jwkSource: JWKSource<SecurityContext> =
      JWKSourceBuilder.create<SecurityContext>(URL(jwkSetUri))
        .cache(CACHE_TTL.toMillis(), CACHE_REFRESH_TIMEOUT.toMillis())
        .refreshAheadCache(true)
        .rateLimited(false)
        .retrying(true)
        .build()

    val processor =
      DefaultJWTProcessor<SecurityContext>().apply {
        jwsKeySelector = JWSVerificationKeySelector(JWSAlgorithm.RS256, jwkSource)
      }

    val decoder = NimbusJwtDecoder(processor)
    val audienceValidator =
      JwtClaimValidator<List<String>>("aud") { aud -> aud != null && aud.contains(audience) }
    val validators =
      DelegatingOAuth2TokenValidator(
        JwtValidators.createDefaultWithIssuer(issuerUri),
        audienceValidator,
      )
    decoder.setJwtValidator(validators)
    return decoder
  }

  companion object {
    private val CACHE_TTL: Duration = Duration.ofHours(24)
    private val CACHE_REFRESH_TIMEOUT: Duration = Duration.ofSeconds(30)
  }
}
