package de.sauter_gmbh.s_plan_api.user_management_application.usecase

import de.sauter_gmbh.s_plan_api.application_security.provider.ActorProvider
import de.sauter_gmbh.s_plan_api.domain_identity.Actor
import de.sauter_gmbh.s_plan_api.user_management_domain.model.NewUser
import de.sauter_gmbh.s_plan_api.user_management_domain.model.User
import de.sauter_gmbh.s_plan_api.user_management_domain.policy.canCreateUser
import de.sauter_gmbh.s_plan_api.user_management_domain.repository.UserRepository
import org.springframework.stereotype.Service

interface CreateUserUseCase {
  operator fun invoke(user: NewUser): Result<User>
}

@Service
internal class CreateUserUseCaseImpl(
  private val repository: UserRepository,
  private val actorProvider: ActorProvider,
) : CreateUserUseCase {

  override operator fun invoke(user: NewUser): Result<User> = runCatching {
    val actor = actorProvider.get()
    if (!actor.canCreateUser()) throw SecurityException("Actor not authorized to create users")

    val createdBy =
      (actor as? Actor.User)?.userId?.value
        ?: throw SecurityException("Actor not authorized to create users")

    if (repository.findById(user.id) != null) {
      throw IllegalArgumentException("User already exists: ${user.id.value}")
    }

    repository.create(user, createdBy = createdBy)
  }
}
