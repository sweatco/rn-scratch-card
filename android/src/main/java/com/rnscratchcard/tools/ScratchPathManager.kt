package com.rnscratchcard.tools

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.view.MotionEvent
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt

class ScratchPathManager {

  companion object {
    private const val POINTER_LIMIT = 10
  }

  private var activePaths = arrayOfNulls<Path>(POINTER_LIMIT)
  private var lastActiveActions = IntArray(POINTER_LIMIT)

  private var scaleX = 1f
  private var scaleY = 1f

  private val paths = ArrayList<Path>()
  private val points = ArrayList<ScratchPathPoint>()

  fun setScale(scaleX: Float, scaleY: Float) {
    this.scaleX = scaleX
    this.scaleY = scaleY
  }

  fun addScratchPathPoints(events: Collection<ScratchPathPoint>) {
    for (event in events) addScratchPathPoint(event)
  }

  private fun addScratchPathPoint(event: ScratchPathPoint) {
    if (POINTER_LIMIT <= event.pointerIndex) return
    when (event.action) {
      MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_CANCEL -> {
      }
      MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> handleTouchDown(
        event.pointerIndex,
        event.x * scaleX,
        event.y * scaleY
      )
      else -> handleTouchMove(
        event.pointerIndex,
        event.x * scaleX,
        event.y * scaleY
      )
    }
    lastActiveActions[event.pointerIndex] = event.action
    points.add(event)
  }

  private fun handleTouchDown(pointerIndex: Int, x: Float, y: Float) {
    createPath(pointerIndex, x, y)
  }

  private fun handleTouchMove(pointerIndex: Int, x: Float, y: Float) {
    if (MotionEvent.ACTION_POINTER_UP == lastActiveActions[pointerIndex]) {
      createPath(pointerIndex, x, y)
    }
    // If the active Path has been drawn, it would have been reset to an empty state
    val activePath = activePaths[pointerIndex] ?: return
    if (activePath.isEmpty) activePath.moveTo(x, y)
    activePath.lineTo(x, y)
  }

  private fun createPath(pointerIndex: Int, x: Float, y: Float) {
    val activePath = Path()
    activePath.moveTo(x, y)
    activePaths[pointerIndex] = activePath
    paths.add(activePath)
  }

  fun drawAndReset(canvas: Canvas, paint: Paint) {
    for (path in paths) {
      canvas.drawPath(path, paint)
      path.reset()
    }
  }
}
