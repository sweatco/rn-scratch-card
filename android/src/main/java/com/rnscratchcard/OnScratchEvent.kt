package com.rnscratchcard

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.events.Event

class OnScratchEvent @JvmOverloads constructor(
  private val x: Int,
  private val y: Int,
  surfaceId: Int = -1,
  viewTag: Int
) : Event<OnScratchEvent>(surfaceId, viewTag) {

  companion object {
    const val EVENT_NAME = "topOnScratch"
  }

  override fun getEventName() = EVENT_NAME

  override fun getEventData(): WritableMap {
    val eventData: WritableMap = Arguments.createMap()
    eventData.putInt("x", x)
    eventData.putInt("y", y)
    return eventData
  }

  override fun canCoalesce() = false
}
