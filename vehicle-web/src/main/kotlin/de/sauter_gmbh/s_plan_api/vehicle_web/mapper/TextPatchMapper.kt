package de.sauter_gmbh.s_plan_api.vehicle_web.mapper

internal fun String?.patchText(stored: String?): String? =
  if (this == null) stored else trim().ifEmpty { null }
