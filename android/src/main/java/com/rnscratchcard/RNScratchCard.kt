package com.rnscratchcard

import android.annotation.SuppressLint
import android.graphics.*
import android.graphics.drawable.Animatable
import android.view.MotionEvent
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.image.CloseableStaticBitmap
import com.facebook.imagepipeline.image.ImageInfo
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.views.image.ReactImageDownloadListener
import com.facebook.react.views.image.ReactImageView
import com.rnscratchcard.tools.ScratchPathManager
import com.rnscratchcard.tools.ScratchPathPoint
import com.rnscratchcard.tools.dp


@SuppressLint("ViewConstructor")
class RNScratchCard constructor(
  context: ThemedReactContext
) : ReactImageView(context, Fresco.newDraweeControllerBuilder(), null, null) {

  var notifyAboutScratchEnabled: Boolean = false

  private val pathManager = ScratchPathManager()
  private val clearPaint: Paint

  private var pathStrippedCanvas: Canvas? = null
  private var pathStrippedImage: Bitmap? = null

  private var srcFrame = Rect(0, 0, width, height)
  private var dstFrame = RectF(0f, 0f, width.toFloat(), height.toFloat())

  private val eventDispatcher = UIManagerHelper.getEventDispatcherForReactTag(context, id)

  init {
    clearPaint = createBaseScratchPaint()

    setControllerListener(object : ReactImageDownloadListener<ImageInfo>() {
      override fun onFinalImageSet(id: String?, imageInfo: ImageInfo?, animatable: Animatable?) {
        if (imageInfo is CloseableStaticBitmap) {
          pathStrippedImage = imageInfo.underlyingBitmap
          pathStrippedCanvas = Canvas(imageInfo.underlyingBitmap)
          srcFrame = Rect(0, 0, imageInfo.width, imageInfo.height)
          dstFrame = RectF(0f, 0f, width.toFloat(), height.toFloat())
          pathManager.setScale(
            srcFrame.width() / dstFrame.width(),
            srcFrame.height() / dstFrame.height()
          )
        }
      }
    })
  }

  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(event: MotionEvent): Boolean {
    val events = ScratchPathPoint.fromMotionEvent(event)
    pathManager.addScratchPathPoints(events)
    pathStrippedCanvas?.let {
      pathManager.drawAndReset(it, clearPaint)
    }
    pathStrippedImage?.prepareToDraw()
    notifyScratch(event)
    postInvalidate()
    return true
  }

  override fun onDraw(canvas: Canvas) {
    pathStrippedImage?.let { canvas.drawBitmap(it, srcFrame, dstFrame, null) }
  }

  fun setBrushWidth(brushWidth: Float) {
    clearPaint.strokeWidth = brushWidth
  }

  private fun notifyScratch(event: MotionEvent) {
    if (notifyAboutScratchEnabled) {
      val surfaceId: Int = (context as ThemedReactContext).surfaceId
      eventDispatcher?.dispatchEvent(
        OnScratchEvent(
          event.x.dp.toInt(),
          event.y.dp.toInt(),
          surfaceId,
          id
        )
      )
    }
  }

  private fun createBaseScratchPaint(): Paint {
    val clearPoint = Paint()
    clearPoint.style = Paint.Style.STROKE
    clearPoint.strokeCap = Paint.Cap.ROUND
    clearPoint.strokeJoin = Paint.Join.ROUND

    clearPoint.alpha = 0xFF
    clearPoint.isAntiAlias = true
    clearPoint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    return clearPoint
  }
}
