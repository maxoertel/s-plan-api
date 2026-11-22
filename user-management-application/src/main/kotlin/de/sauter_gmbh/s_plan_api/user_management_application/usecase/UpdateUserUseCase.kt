package de.sauter_gmbh.s_plan_api.user_management_application.usecase

import de.sauter_gmbh.s_plan_api.application_security.provider.ActorProvider
import de.sauter_gmbh.s_plan_api.domain_identity.Actor
import de.sauter_gmbh.s_plan_api.domain_identity.UserId
import de.sauter_gmbh.s_plan_api.user_management_domain.model.User
import de.sauter_gmbh.s_plan_api.user_management_domain.policy.canUpdateUser
import de.sauter_gmbh.s_plan_api.user_management_domain.repository.UserRepository
import org.springframework.stereotype.Service

interface UpdateUserUseCase {
  operator fun invoke(id: UserId, patch: (User) -> User): Result<User>
}

@Service
internal class UpdateUserUseCaseImpl(
  private val repository: UserRepository,
  private val actorProvider: ActorProvider,
) : UpdateUserUseCase {

  override operator fun invoke(id: UserId, patch: (User) -> User): Result<User> = runCatching {
    val actor = actorProvider.get()
    if (!actor.canUpdateUser()) throw SecurityException("Actor not authorized to update users")

    val updatedBy =
      (actor as? Actor.User)?.userId?.value
        ?: throw SecurityException("Actor not authorized to update users")

    val existing =
      repository.findById(id) ?: throw NoSuchElementException("User not found: ${id.value}")

    // The patch operates on the stored user, so pin the identity and audit fields it must not move.
    val patched =
      patch(existing)
        .copy(
          id = existing.id,
          createdAt = existing.createdAt,
          createdBy = existing.createdBy,
        )

    repository.update(patched, updatedBy = updatedBy)
  }
}
