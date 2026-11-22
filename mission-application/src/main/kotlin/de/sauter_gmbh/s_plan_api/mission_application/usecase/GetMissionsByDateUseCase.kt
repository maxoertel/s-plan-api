package de.sauter_gmbh.s_plan_api.mission_application.usecase

import de.sauter_gmbh.s_plan_api.application_security.provider.ActorProvider
import de.sauter_gmbh.s_plan_api.mission_domain.model.Mission
import de.sauter_gmbh.s_plan_api.mission_domain.policy.canListAllMissions
import de.sauter_gmbh.s_plan_api.mission_domain.repository.MissionRepository
import java.time.LocalDate
import org.springframework.stereotype.Service

interface GetMissionsByDateUseCase {
  operator fun invoke(date: LocalDate): Result<List<Mission>>
}

@Service
internal class GetMissionsByDateUseCaseImpl(
  private val repository: MissionRepository,
  private val actorProvider: ActorProvider,
) : GetMissionsByDateUseCase {

  override operator fun invoke(date: LocalDate): Result<List<Mission>> = runCatching {
    val actor = actorProvider.get()
    if (!actor.canListAllMissions())
      throw SecurityException("Actor not authorized to list all missions")

    repository.findByDate(date)
  }
}
