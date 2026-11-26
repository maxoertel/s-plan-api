package de.sauter_gmbh.s_plan_api.shared_core.logger

import kotlin.reflect.KProperty
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class LoggerDelegate {
  operator fun getValue(thisRef: Any, property: KProperty<*>): Logger {
    return LoggerFactory.getLogger(thisRef::class.java)
  }
}

fun logger() = LoggerDelegate()
