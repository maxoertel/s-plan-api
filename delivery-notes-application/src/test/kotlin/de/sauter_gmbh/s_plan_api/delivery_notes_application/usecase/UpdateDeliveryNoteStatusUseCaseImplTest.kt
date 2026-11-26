package de.sauter_gmbh.s_plan_api.delivery_notes_application.usecase

import de.sauter_gmbh.s_plan_api.application_security.provider.ActorProvider
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.Customer
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.DeliveryNote
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.DeliveryNoteId
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.Signature
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.Site
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.Status
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.StatusTime
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.StatusUpdate
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.repository.DeliveryNoteRepository
import de.sauter_gmbh.s_plan_api.domain_identity.Actor
import de.sauter_gmbh.s_plan_api.domain_identity.Role
import de.sauter_gmbh.s_plan_api.domain_identity.UserId
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import java.time.LocalTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class UpdateDeliveryNoteStatusUseCaseImplTest {

  private val repository = mockk<DeliveryNoteRepository>()
  private val actorProvider = mockk<ActorProvider>()

  private val sut by lazy { UpdateDeliveryNoteStatusUseCaseImpl(repository, actorProvider) }

  private val deliveryNoteId = DeliveryNoteId("100_55_20260418_083000")
  private val update = StatusUpdate(times = mapOf(StatusTime.ON_SITE to LocalTime.of(9, 15)))

  @Test
  fun `should write times and return reloaded delivery note when user is admin`() {
    // given:
    val stored = testDeliveryNote(onSiteTimestamp = "2026-04-18 09:00:00.0")
    val updated = testDeliveryNote(onSiteTimestamp = "2026-04-18 09:15:00.0")
    every { actorProvider.get() } returns Actor.User(UserId("admin1"), Role.ADMIN)
    every { repository.findById(deliveryNoteId) } returnsMany listOf(stored, updated)
    every { repository.updateStatusTimes(deliveryNoteId, update) } just runs

    // when:
    val result = sut(deliveryNoteId, update)

    // then:
    assertTrue(result.isSuccess)
    assertEquals(updated, result.getOrNull())
    verify(exactly = 1) { repository.updateStatusTimes(deliveryNoteId, update) }
  }

  @Test
  fun `should not touch the database and return the stored note when update has no times`() {
    // given:
    val stored = testDeliveryNote()
    every { actorProvider.get() } returns Actor.User(UserId("admin1"), Role.ADMIN)
    every { repository.findById(deliveryNoteId) } returns stored

    // when:
    val result = sut(deliveryNoteId, StatusUpdate(times = emptyMap()))

    // then:
    assertTrue(result.isSuccess)
    assertEquals(stored, result.getOrNull())
    verify(exactly = 0) { repository.updateStatusTimes(any(), any()) }
  }

  @Test
  fun `should return failure with SecurityException when user is not admin`() {
    // given:
    every { actorProvider.get() } returns Actor.User(UserId("user1"), Role.VIEWER)

    // when:
    val result = sut(deliveryNoteId, update)

    // then:
    assertTrue(result.isFailure)
    assertIs<SecurityException>(result.exceptionOrNull())
    verify(exactly = 0) { repository.updateStatusTimes(any(), any()) }
  }

  @Test
  fun `should return failure with NoSuchElementException when delivery note does not exist`() {
    // given:
    every { actorProvider.get() } returns Actor.User(UserId("admin1"), Role.ADMIN)
    every { repository.findById(deliveryNoteId) } returns null

    // when:
    val result = sut(deliveryNoteId, update)

    // then:
    assertTrue(result.isFailure)
    assertIs<NoSuchElementException>(result.exceptionOrNull())
    verify(exactly = 0) { repository.updateStatusTimes(any(), any()) }
  }

  @Test
  fun `should return failure when repository throws exception`() {
    // given:
    every { actorProvider.get() } returns Actor.User(UserId("admin1"), Role.ADMIN)
    every { repository.findById(deliveryNoteId) } returns testDeliveryNote()
    every { repository.updateStatusTimes(deliveryNoteId, update) } throws
      RuntimeException("db error")

    // when:
    val result = sut(deliveryNoteId, update)

    // then:
    assertTrue(result.isFailure)
    assertEquals("db error", result.exceptionOrNull()?.message)
  }

  private fun testDeliveryNote(onSiteTimestamp: String? = null) =
    DeliveryNote(
      id = deliveryNoteId,
      factoryNr = 100L,
      date = "2026-04-18",
      time = "08:30:00",
      deliveryNoteNr = 55,
      deliveryNoteNrExternal = null,
      deliveryNoteCircle = null,
      printType = null,
      orderType = null,
      deliveryDateTime = null,
      orderNr = null,
      loadNr = null,
      batchNr = null,
      plannedQuantity = null,
      actualQuantity = null,
      loadQuantity = null,
      returnQuantity = null,
      customer = emptyCustomer(),
      site = emptySite(),
      vehicleId = null,
      recipe = null,
      articleNr = null,
      description = null,
      status = status(onSiteTimestamp),
      signature = Signature(deviceId = null, status = null),
      createdAt = null,
      updatedAt = null,
    )

  private fun status(onSiteTimestamp: String?) =
    Status(
      loadingInProgressTimestamp = null,
      loadingCompletedTimestamp = null,
      enRouteToSiteTimestamp = null,
      onSiteTimestamp = onSiteTimestamp,
      unloadingStartedTimestamp = null,
      unloadingCompletedTimestamp = null,
      returningTimestamp = null,
      returnedTimestamp = null,
    )

  private fun emptyCustomer() =
    Customer(
      nr = null,
      matchCode = null,
      name1 = null,
      name2 = null,
      name3 = null,
      name4 = null,
      street = null,
      houseNr = null,
      country = null,
      zip = null,
      city = null,
      phone = null,
      mobile = null,
      email1 = null,
      email2 = null,
      note = null,
      blockCode = null,
    )

  private fun emptySite() =
    Site(
      nr = null,
      matchCode = null,
      description1 = null,
      description2 = null,
      description3 = null,
      description4 = null,
      street = null,
      houseNr = null,
      country = null,
      zip = null,
      city = null,
      contact = null,
      note = null,
      phone = null,
      mobile = null,
    )
}
