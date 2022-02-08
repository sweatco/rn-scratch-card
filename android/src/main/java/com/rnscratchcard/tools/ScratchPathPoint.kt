package com.rnscratchcard.tools

import android.view.MotionEvent
import java.util.*

data class ScratchPathPoint(
  val pointerIndex: Int,
  val x: Float,
  val y: Float,
  val action: Int
) {

  companion object {

    fun fromMotionEvent(event: MotionEvent): List<ScratchPathPoint> {
      val historySize = event.historySize
      val pointersCount = event.pointerCount
      val events = ArrayList<ScratchPathPoint>(historySize * pointersCount + pointersCount)
      for (historyIndex in 0 until historySize) {
        for (pointerIndex in 0 until pointersCount) {
          events.add(
            ScratchPathPoint(
              pointerIndex,
              event.getHistoricalX(pointerIndex, historyIndex),
              event.getHistoricalY(pointerIndex, historyIndex),
              MotionEvent.ACTION_MOVE
            )
          )
        }
      }
      for (pointerIndex in 0 until pointersCount) {
        events.add(
          ScratchPathPoint(
            pointerIndex,
            event.getX(pointerIndex),
            event.getY(pointerIndex),
            event.actionMasked
          )
        )
      }
      return events
    }
  }
}
