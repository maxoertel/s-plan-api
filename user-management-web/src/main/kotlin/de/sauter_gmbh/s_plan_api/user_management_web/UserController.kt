package de.sauter_gmbh.s_plan_api.user_management_web

import de.sauter_gmbh.s_plan_api.domain_identity.UserId
import de.sauter_gmbh.s_plan_api.infra_web.toResponse
import de.sauter_gmbh.s_plan_api.user_management_application.usecase.CreateUserUseCase
import de.sauter_gmbh.s_plan_api.user_management_application.usecase.DeleteUserUseCase
import de.sauter_gmbh.s_plan_api.user_management_application.usecase.GetUsersUseCase
import de.sauter_gmbh.s_plan_api.user_management_application.usecase.UpdateUserUseCase
import de.sauter_gmbh.s_plan_api.user_management_web.dto.CreateUserRequestDto
import de.sauter_gmbh.s_plan_api.user_management_web.dto.UpdateUserRequestDto
import de.sauter_gmbh.s_plan_api.user_management_web.mapper.toDto
import de.sauter_gmbh.s_plan_api.user_management_web.mapper.toModel
import de.sauter_gmbh.s_plan_api.user_management_web.mapper.toPatch
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
internal class UserController(
  private val getUsers: GetUsersUseCase,
  private val createUser: CreateUserUseCase,
  private val updateUser: UpdateUserUseCase,
  private val deleteUser: DeleteUserUseCase,
) {

  @GetMapping
  fun get(): ResponseEntity<*> {
    return getUsers().toResponse { users -> users.map { it.toDto() } }
  }

  @PostMapping
  fun post(@RequestBody request: CreateUserRequestDto): ResponseEntity<*> {
    return createUser(request.toModel()).toResponse { it.toDto() }
  }

  @PatchMapping("/{id}")
  fun patch(
    @PathVariable id: String,
    @RequestBody request: UpdateUserRequestDto,
  ): ResponseEntity<*> {
    return updateUser(UserId(id), request.toPatch()).toResponse { it.toDto() }
  }

  @DeleteMapping("/{id}")
  fun delete(@PathVariable id: String): ResponseEntity<*> {
    return deleteUser(UserId(id)).toResponse {}
  }
}
