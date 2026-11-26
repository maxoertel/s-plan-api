package de.sauter_gmbh.s_plan_api.user_management_application.usecase

import de.sauter_gmbh.s_plan_api.application_security.provider.ActorProvider
import de.sauter_gmbh.s_plan_api.domain_identity.Actor
import de.sauter_gmbh.s_plan_api.domain_identity.UserId
import de.sauter_gmbh.s_plan_api.user_management_domain.policy.canDeleteUser
import de.sauter_gmbh.s_plan_api.user_management_domain.repository.UserRepository
import org.springframework.stereotype.Service

interface DeleteUserUseCase {
  operator fun invoke(id: UserId): Result<Unit>
}

@Service
internal class DeleteUserUseCaseImpl(
  private val repository: UserRepository,
  private val actorProvider: ActorProvider,
) : DeleteUserUseCase {

  override operator fun invoke(id: UserId): Result<Unit> = runCatching {
    val actor = actorProvider.get()
    if (!actor.canDeleteUser()) throw SecurityException("Actor not authorized to delete users")

    // Deleting yourself would revoke your own access with no way back in.
    if (actor is Actor.User && actor.userId == id) {
      throw IllegalArgumentException("Users cannot delete themselves: ${id.value}")
    }

    repository.delete(id)
  }
}
