package de.sauter_gmbh.s_plan_api.mission_web

import de.sauter_gmbh.s_plan_api.infra_web.toResponse
import de.sauter_gmbh.s_plan_api.mission_application.usecase.GetMissionsByDateUseCase
import de.sauter_gmbh.s_plan_api.mission_web.mapper.toDto
import java.time.LocalDate
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/missions")
internal class MissionController(
  private val getMissionsByDate: GetMissionsByDateUseCase,
) {

  // Admin-only listing of all missions on a given day.
  @GetMapping("/by-date")
  fun getByDate(
    @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate
  ): ResponseEntity<*> {
    return getMissionsByDate(date).toResponse { missions -> missions.map { it.toDto() } }
  }
}
