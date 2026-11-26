package de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.mapper

import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.DeliveryNotePdf
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.DeliveryNotePdfKind
import de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.datasource.entity.DeliveryNotePdfEntity
import de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.parser.DeliveryNotePdfIdParser
import de.sauter_gmbh.s_plan_api.infra_db.table.DeliveryNotePdfSources

internal fun DeliveryNotePdfEntity.toModel(idParser: DeliveryNotePdfIdParser): DeliveryNotePdf =
  DeliveryNotePdf(
    id =
      idParser.format(
        factoryNr = factoryNr,
        date = date,
        time = time,
        deliveryNoteNr = deliveryNoteNr,
        deliveryNoteCircle = deliveryNoteCircle,
        creationDate = creationDate,
        creationTime = creationTime,
        storageDate = storageDate,
        storageTime = storageTime,
      ),
    factoryNr = factoryNr,
    date = date,
    time = time,
    deliveryNoteNr = deliveryNoteNr,
    deliveryNoteCircle = deliveryNoteCircle,
    creationDate = creationDate,
    creationTime = creationTime,
    storageDate = storageDate,
    storageTime = storageTime,
    kind =
      if (dataSource?.startsWith(DeliveryNotePdfSources.S_PLAN) == true) DeliveryNotePdfKind.FINAL
      else DeliveryNotePdfKind.PRE_PRINT,
    waterCorrectionUnloading = waterCorrectionUnloading?.toFloat(),
  )
