package de.sauter_gmbh.s_plan_api.location_application.usecase

import de.sauter_gmbh.s_plan_api.application_security.provider.ActorProvider
import de.sauter_gmbh.s_plan_api.location_domain.model.Site
import de.sauter_gmbh.s_plan_api.location_domain.policy.canListSites
import de.sauter_gmbh.s_plan_api.location_domain.repository.SiteRepository
import de.sauter_gmbh.s_plan_api.mission_domain.repository.MissionRepository
import java.time.LocalDate
import org.springframework.stereotype.Service

interface GetSitesUseCase {
  operator fun invoke(date: LocalDate?): Result<List<Site>>
}

@Service
internal class GetSitesUseCaseImpl(
  private val repository: SiteRepository,
  private val missionRepository: MissionRepository,
  private val actorProvider: ActorProvider,
) : GetSitesUseCase {

  override operator fun invoke(date: LocalDate?): Result<List<Site>> = runCatching {
    val actor = actorProvider.get()
    if (!actor.canListSites()) throw SecurityException("Actor not authorized to list sites")

    if (date == null) {
      repository.findAll()
    } else {
      val siteKeys = missionRepository.findSiteKeysWithMissionsOnDate(date)
      if (siteKeys.isEmpty()) emptyList() else repository.findBySiteKeys(siteKeys)
    }
  }
}
