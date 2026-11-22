package de.sauter_gmbh.s_plan_api.infra_security.provider

import de.sauter_gmbh.s_plan_api.application_security.provider.ActorProvider
import de.sauter_gmbh.s_plan_api.domain_identity.Actor
import de.sauter_gmbh.s_plan_api.infra_security.datasource.database.AuthUserDataSource
import de.sauter_gmbh.s_plan_api.infra_security.mapper.toModel
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component

@Component
internal class ActorProviderImpl(
  private val authUserDataSource: AuthUserDataSource,
  @Value("\${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
  private val auth0IssuerUri: String,
) : ActorProvider {

  override fun get(): Actor {
    return when (val authentication = SecurityContextHolder.getContext().authentication) {
      is JwtAuthenticationToken -> resolveUser(authentication)
      else -> throw AccessDeniedException("Unknown or missing authentication")
    }
  }

  private fun resolveUser(token: JwtAuthenticationToken): Actor.User {
    val issuer = token.token.issuer ?: throw AccessDeniedException("Missing issuer in JWT")
    val subject = token.token.subject ?: throw AccessDeniedException("Missing subject in JWT")

    return when (issuer.toString()) {
      auth0IssuerUri -> findAuthUserByAuth0Id(subject)
      else -> throw AccessDeniedException("Invalid token issuer")
    }
  }

  private fun findAuthUserByAuth0Id(auth0Id: String): Actor.User {
    val entity =
      authUserDataSource.findAuthUserByAuth0Id(auth0Id)
        ?: throw AccessDeniedException("No user found for auth0 id '$auth0Id'")
    return entity.toModel()
  }
}
