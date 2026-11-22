package de.sauter_gmbh.s_plan_api.infra_security.provider

import de.sauter_gmbh.s_plan_api.domain_identity.Actor
import de.sauter_gmbh.s_plan_api.domain_identity.Role
import de.sauter_gmbh.s_plan_api.domain_identity.UserId
import de.sauter_gmbh.s_plan_api.domain_identity.VehicleId
import de.sauter_gmbh.s_plan_api.infra_security.datasource.database.AuthUserDataSource
import de.sauter_gmbh.s_plan_api.infra_security.datasource.database.entity.AuthUserEntity
import io.mockk.every
import io.mockk.mockk
import java.net.URL
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

class ActorProviderImplTest {

  private val authUserDataSource = mockk<AuthUserDataSource>()

  private val auth0IssuerUri = "https://tenant.eu.auth0.com/"

  private val sut by lazy { ActorProviderImpl(authUserDataSource, auth0IssuerUri) }

  private val auth0Id = "auth0|abc"
  private val validIssuer = URL(auth0IssuerUri)
  private val vehicleId = VehicleId(9013L)

  @AfterTest
  fun tearDown() {
    SecurityContextHolder.clearContext()
  }

  @Test
  fun `should return user actor when jwt authentication is valid`() {
    // given:
    every { authUserDataSource.findAuthUserByAuth0Id(auth0Id) } returns
      AuthUserEntity(userId = auth0Id, role = "admin")
    setAuthentication(jwtAuthenticationToken(issuer = validIssuer, subject = auth0Id))

    // when:
    val result = sut.get()

    // then:
    assertEquals(Actor.User(UserId(auth0Id), Role.ADMIN), result)
  }

  @Test
  fun `should throw AccessDeniedException when jwt issuer is missing`() {
    // given:
    setAuthentication(jwtAuthenticationToken(issuer = null, subject = auth0Id))

    // when / then:
    val exception = assertFailsWith<AccessDeniedException> { sut.get() }
    assertEquals("Missing issuer in JWT", exception.message)
  }

  @Test
  fun `should throw AccessDeniedException when jwt subject is missing`() {
    // given:
    setAuthentication(jwtAuthenticationToken(issuer = validIssuer, subject = null))

    // when / then:
    val exception = assertFailsWith<AccessDeniedException> { sut.get() }
    assertEquals("Missing subject in JWT", exception.message)
  }

  @Test
  fun `should throw AccessDeniedException when jwt issuer does not match configured auth0 issuer`() {
    // given:
    setAuthentication(
      jwtAuthenticationToken(issuer = URL("https://other-tenant.auth0.com/"), subject = auth0Id),
    )

    // when / then:
    val exception = assertFailsWith<AccessDeniedException> { sut.get() }
    assertEquals("Invalid token issuer", exception.message)
  }

  @Test
  fun `should throw AccessDeniedException when no user is found for auth0 id`() {
    // given:
    every { authUserDataSource.findAuthUserByAuth0Id(auth0Id) } returns null
    setAuthentication(jwtAuthenticationToken(issuer = validIssuer, subject = auth0Id))

    // when / then:
    val exception = assertFailsWith<AccessDeniedException> { sut.get() }
    assertEquals("No user found for auth0 id '$auth0Id'", exception.message)
  }

  @Test
  fun `should throw AccessDeniedException when authentication is missing`() {
    // given:
    SecurityContextHolder.clearContext()

    // when / then:
    val exception = assertFailsWith<AccessDeniedException> { sut.get() }
    assertEquals("Unknown or missing authentication", exception.message)
  }

  @Test
  fun `should throw AccessDeniedException when authentication type is unknown`() {
    // given:
    setAuthentication(mockk<Authentication>(relaxed = true))

    // when / then:
    val exception = assertFailsWith<AccessDeniedException> { sut.get() }
    assertEquals("Unknown or missing authentication", exception.message)
  }

  private fun setAuthentication(authentication: Authentication) {
    SecurityContextHolder.getContext().authentication = authentication
  }

  private fun jwtAuthenticationToken(issuer: URL?, subject: String?): JwtAuthenticationToken {
    val jwt = mockk<Jwt>()
    every { jwt.issuer } returns issuer
    every { jwt.subject } returns subject
    val token = mockk<JwtAuthenticationToken>()
    every { token.token } returns jwt
    return token
  }
}
