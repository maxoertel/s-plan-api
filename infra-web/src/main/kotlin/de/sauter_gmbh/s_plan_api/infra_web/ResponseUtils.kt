package de.sauter_gmbh.s_plan_api.infra_web

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity

private val log = LoggerFactory.getLogger("de.sauter_gmbh.s_plan_api.infra_web.ResponseUtils")

fun <T, R> Result<T>.toResponse(onSuccess: (T) -> R): ResponseEntity<*> =
  fold(
    onSuccess = { ResponseEntity.ok(mapOf("data" to onSuccess(it))) },
    onFailure = {
      val response = ErrorMapper.toResponseEntity(it)
      if (response.statusCode.is5xxServerError) {
        log.error("Request failed: {}", it.message, it)
      } else {
        log.warn("Request failed: {} {}", response.statusCode, it.message)
      }
      response
    },
  )
