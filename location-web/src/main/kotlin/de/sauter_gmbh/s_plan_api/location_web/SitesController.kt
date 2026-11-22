package de.sauter_gmbh.s_plan_api.location_web

import de.sauter_gmbh.s_plan_api.domain_identity.SiteKey
import de.sauter_gmbh.s_plan_api.infra_web.toResponse
import de.sauter_gmbh.s_plan_api.location_application.usecase.GetSitesUseCase
import de.sauter_gmbh.s_plan_api.location_application.usecase.UpdateSitePositionUseCase
import de.sauter_gmbh.s_plan_api.location_web.dto.UpdateSitePositionRequestDto
import de.sauter_gmbh.s_plan_api.location_web.mapper.toDto
import de.sauter_gmbh.s_plan_api.location_web.mapper.toModel
import java.time.LocalDate
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sites")
internal class SitesController(
  private val getSites: GetSitesUseCase,
  private val updateSitePosition: UpdateSitePositionUseCase,
) {

  @GetMapping
  fun get(
    @RequestParam("date", required = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    date: LocalDate?
  ): ResponseEntity<*> = getSites(date).toResponse { sites -> sites.map { it.toDto() } }

  @PatchMapping("/{customerNr}/{siteNr}/position")
  fun patchPosition(
    @PathVariable customerNr: Long,
    @PathVariable siteNr: Long,
    @RequestBody request: UpdateSitePositionRequestDto,
  ): ResponseEntity<*> =
    updateSitePosition(SiteKey(customerNr = customerNr, siteNr = siteNr), request.toModel())
      .toResponse { it.toDto() }
}
