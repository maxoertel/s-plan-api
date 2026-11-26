package de.sauter_gmbh.s_plan_api.location_application.usecase

import de.sauter_gmbh.s_plan_api.application_security.provider.ActorProvider
import de.sauter_gmbh.s_plan_api.domain_identity.SiteKey
import de.sauter_gmbh.s_plan_api.location_domain.model.Site
import de.sauter_gmbh.s_plan_api.location_domain.model.SitePosition
import de.sauter_gmbh.s_plan_api.location_domain.policy.canUpdateSitePosition
import de.sauter_gmbh.s_plan_api.location_domain.repository.SiteRepository
import org.springframework.stereotype.Service

interface UpdateSitePositionUseCase {
  operator fun invoke(siteKey: SiteKey, position: SitePosition): Result<Site>
}

@Service
internal class UpdateSitePositionUseCaseImpl(
  private val siteRepository: SiteRepository,
  private val actorProvider: ActorProvider,
) : UpdateSitePositionUseCase {

  override operator fun invoke(siteKey: SiteKey, position: SitePosition): Result<Site> =
    runCatching {
      val actor = actorProvider.get()
      if (!actor.canUpdateSitePosition())
        throw SecurityException("Actor not authorized to update site positions")

      require(position.latitude in -90.0..90.0) { "Latitude out of range" }
      require(position.longitude in -180.0..180.0) { "Longitude out of range" }
      require(position.radiusInMeter > 0) { "Radius must be positive" }

      siteRepository.updatePosition(siteKey, position)
        ?: throw NoSuchElementException("Site not found: ${siteKey.customerNr}/${siteKey.siteNr}")
    }
}
