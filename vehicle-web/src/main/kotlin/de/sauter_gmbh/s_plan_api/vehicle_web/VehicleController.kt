package de.sauter_gmbh.s_plan_api.vehicle_web

import de.sauter_gmbh.s_plan_api.domain_identity.VehicleId
import de.sauter_gmbh.s_plan_api.infra_web.toResponse
import de.sauter_gmbh.s_plan_api.vehicle_application.usecase.GetVehiclesUseCase
import de.sauter_gmbh.s_plan_api.vehicle_application.usecase.UpdateVehicleUseCase
import de.sauter_gmbh.s_plan_api.vehicle_web.dto.UpdateVehicleRequestDto
import de.sauter_gmbh.s_plan_api.vehicle_web.mapper.toDto
import de.sauter_gmbh.s_plan_api.vehicle_web.mapper.toPatch
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/vehicles")
internal class VehicleController(
  private val getVehicles: GetVehiclesUseCase,
  private val updateVehicle: UpdateVehicleUseCase,
) {

  @GetMapping
  fun get(): ResponseEntity<*> {
    return getVehicles().toResponse { vehicles -> vehicles.map { it.toDto() } }
  }

  @PatchMapping("/{vehicleId}")
  fun patch(
    @PathVariable vehicleId: Long,
    @RequestBody request: UpdateVehicleRequestDto,
  ): ResponseEntity<*> {
    return updateVehicle(VehicleId(vehicleId), request.toPatch()).toResponse { it.toDto() }
  }
}
