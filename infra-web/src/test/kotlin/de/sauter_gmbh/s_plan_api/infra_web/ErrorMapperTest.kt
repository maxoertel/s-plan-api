package de.sauter_gmbh.s_plan_api.infra_web

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException

class ErrorMapperTest {

  @Test
  fun `should map NoSuchElementException to 404 Not Found`() {
    // given:
    val throwable = NoSuchElementException("not here")

    // when:
    val result = ErrorMapper.toResponseEntity(throwable)

    // then:
    assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
    val body = assertNotNull(result.body)
    assertEquals("Resource not found", body.detail)
    assertEquals(HttpStatus.NOT_FOUND.value(), body.status)
  }

  @Test
  fun `should map IllegalArgumentException to 400 Bad Request`() {
    // given:
    val throwable = IllegalArgumentException("bad input")

    // when:
    val result = ErrorMapper.toResponseEntity(throwable)

    // then:
    assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    val body = assertNotNull(result.body)
    assertEquals("Bad request", body.detail)
    assertEquals(HttpStatus.BAD_REQUEST.value(), body.status)
  }

  @Test
  fun `should map AuthenticationException to 401 Unauthorized`() {
    // given:
    val throwable = object : AuthenticationException("no creds") {}

    // when:
    val result = ErrorMapper.toResponseEntity(throwable)

    // then:
    assertEquals(HttpStatus.UNAUTHORIZED, result.statusCode)
    val body = assertNotNull(result.body)
    assertEquals("Unauthorized", body.detail)
    assertEquals(HttpStatus.UNAUTHORIZED.value(), body.status)
  }

  @Test
  fun `should map AccessDeniedException to 403 Forbidden`() {
    // given:
    val throwable = AccessDeniedException("forbidden")

    // when:
    val result = ErrorMapper.toResponseEntity(throwable)

    // then:
    assertEquals(HttpStatus.FORBIDDEN, result.statusCode)
    val body = assertNotNull(result.body)
    assertEquals("Access denied", body.detail)
    assertEquals(HttpStatus.FORBIDDEN.value(), body.status)
  }

  @Test
  fun `should map unknown throwable to 500 Internal Server Error`() {
    // given:
    val throwable = RuntimeException("boom")

    // when:
    val result = ErrorMapper.toResponseEntity(throwable)

    // then:
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.statusCode)
    val body = assertNotNull(result.body)
    assertEquals("An unexpected error occurred", body.detail)
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), body.status)
  }
}
