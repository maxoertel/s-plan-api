package de.sauter_gmbh.s_plan_api.location_web

import de.sauter_gmbh.s_plan_api.infra_web.toResponse
import de.sauter_gmbh.s_plan_api.location_application.usecase.GetFactoriesUseCase
import de.sauter_gmbh.s_plan_api.location_web.mapper.toDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/factories")
internal class FactoriesController(
  private val getFactories: GetFactoriesUseCase,
) {

  @GetMapping
  fun get(): ResponseEntity<*> =
    getFactories().toResponse { factories -> factories.map { it.toDto() } }
}
