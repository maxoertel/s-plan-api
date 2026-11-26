package de.sauter_gmbh.s_plan_api.infra_web

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity

object ErrorMapper {

  fun toResponseEntity(throwable: Throwable): ResponseEntity<ProblemDetail> =
    when (throwable) {
      is NoSuchElementException ->
        ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(
            ProblemDetail.forStatusAndDetail(
              HttpStatus.NOT_FOUND,
              "Resource not found",
            )
          )
      is IllegalArgumentException ->
        ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(
            ProblemDetail.forStatusAndDetail(
              HttpStatus.BAD_REQUEST,
              "Bad request",
            )
          )
      is org.springframework.security.core.AuthenticationException ->
        ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Unauthorized"))
      is org.springframework.security.access.AccessDeniedException,
      is SecurityException ->
        ResponseEntity.status(HttpStatus.FORBIDDEN)
          .body(
            ProblemDetail.forStatusAndDetail(
              HttpStatus.FORBIDDEN,
              "Access denied",
            )
          )
      else ->
        ResponseEntity.internalServerError()
          .body(
            ProblemDetail.forStatusAndDetail(
              HttpStatus.INTERNAL_SERVER_ERROR,
              "An unexpected error occurred",
            )
          )
    }
}
