package de.sauter_gmbh.s_plan_api.infra_web

import org.slf4j.LoggerFactory
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

  private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

  @ExceptionHandler(Exception::class)
  fun handle(exception: Exception): ResponseEntity<ProblemDetail> {
    val response = ErrorMapper.toResponseEntity(exception)
    if (response.statusCode.is5xxServerError) {
      log.error("Unhandled exception: {}", exception.message, exception)
    } else {
      log.warn("Request failed: {} {}", response.statusCode, exception.message)
    }
    return response
  }
}
