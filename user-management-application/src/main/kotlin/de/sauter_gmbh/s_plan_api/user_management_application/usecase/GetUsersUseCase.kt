package de.sauter_gmbh.s_plan_api.user_management_application.usecase

import de.sauter_gmbh.s_plan_api.application_security.provider.ActorProvider
import de.sauter_gmbh.s_plan_api.user_management_domain.model.User
import de.sauter_gmbh.s_plan_api.user_management_domain.policy.canListUsers
import de.sauter_gmbh.s_plan_api.user_management_domain.repository.UserRepository
import org.springframework.stereotype.Service

interface GetUsersUseCase {
  operator fun invoke(): Result<List<User>>
}

@Service
internal class GetUsersUseCaseImpl(
  private val repository: UserRepository,
  private val actorProvider: ActorProvider,
) : GetUsersUseCase {

  override operator fun invoke(): Result<List<User>> = runCatching {
    val actor = actorProvider.get()
    if (!actor.canListUsers()) throw SecurityException("Actor not authorized to list users")

    repository.findAll()
  }
}
