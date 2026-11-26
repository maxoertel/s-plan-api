package de.sauter_gmbh.s_plan_api.location_application.usecase

import de.sauter_gmbh.s_plan_api.application_security.provider.ActorProvider
import de.sauter_gmbh.s_plan_api.location_domain.model.Factory
import de.sauter_gmbh.s_plan_api.location_domain.policy.canListFactories
import de.sauter_gmbh.s_plan_api.location_domain.repository.FactoryRepository
import org.springframework.stereotype.Service

interface GetFactoriesUseCase {
  operator fun invoke(): Result<List<Factory>>
}

@Service
internal class GetFactoriesUseCaseImpl(
  private val repository: FactoryRepository,
  private val actorProvider: ActorProvider,
) : GetFactoriesUseCase {

  override operator fun invoke(): Result<List<Factory>> = runCatching {
    val actor = actorProvider.get()
    if (!actor.canListFactories()) throw SecurityException("Actor not authorized to list factories")

    repository.findAll()
  }
}
