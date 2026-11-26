package de.sauter_gmbh.s_plan_api.delivery_notes_application.usecase

import de.sauter_gmbh.s_plan_api.application_security.provider.ActorProvider
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.Customer
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.DeliveryNote
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.DeliveryNoteId
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.Signature
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.Site
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.Status
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.repository.DeliveryNoteRepository
import de.sauter_gmbh.s_plan_api.domain_identity.Actor
import de.sauter_gmbh.s_plan_api.domain_identity.Role
import de.sauter_gmbh.s_plan_api.domain_identity.UserId
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class GetDeliveryNotesUseCaseImplTest {

  private val repository = mockk<DeliveryNoteRepository>()
  private val actorProvider = mockk<ActorProvider>()

  private val sut by lazy { GetDeliveryNotesUseCaseImpl(repository, actorProvider) }

  private val date = LocalDate.of(2026, 4, 18)

  @Test
  fun `should return success with delivery notes when user is admin`() {
    // given:
    val notes = listOf(testDeliveryNote())
    every { actorProvider.get() } returns Actor.User(UserId("admin1"), Role.ADMIN)
    every { repository.findByDate(date) } returns notes

    // when:
    val result = sut(date)

    // then:
    assertTrue(result.isSuccess)
    assertEquals(notes, result.getOrNull())
    verify(exactly = 1) { repository.findByDate(date) }
  }

  @Test
  fun `should return failure with SecurityException when user is not admin`() {
    // given:
    every { actorProvider.get() } returns Actor.User(UserId("user1"), Role.VIEWER)

    // when:
    val result = sut(date)

    // then:
    assertTrue(result.isFailure)
    assertIs<SecurityException>(result.exceptionOrNull())
    verify(exactly = 0) { repository.findByDate(any()) }
  }

  @Test
  fun `should return failure when repository throws exception`() {
    // given:
    every { actorProvider.get() } returns Actor.User(UserId("admin1"), Role.ADMIN)
    every { repository.findByDate(date) } throws RuntimeException("db error")

    // when:
    val result = sut(date)

    // then:
    assertTrue(result.isFailure)
    assertEquals("db error", result.exceptionOrNull()?.message)
  }

  private fun testDeliveryNote() =
    DeliveryNote(
      id = DeliveryNoteId("100_55_20260418_083000"),
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
      status = emptyStatus(),
      signature = emptySignature(),
      createdAt = null,
      updatedAt = null,
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

  private fun emptyStatus() =
    Status(
      loadingInProgressTimestamp = null,
      loadingCompletedTimestamp = null,
      enRouteToSiteTimestamp = null,
      onSiteTimestamp = null,
      unloadingStartedTimestamp = null,
      unloadingCompletedTimestamp = null,
      returningTimestamp = null,
      returnedTimestamp = null,
    )

  private fun emptySignature() =
    Signature(
      deviceId = null,
      status = null,
    )
}
