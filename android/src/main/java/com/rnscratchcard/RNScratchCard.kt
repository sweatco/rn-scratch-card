package com.rnscratchcard

import android.annotation.SuppressLint
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.MotionEvent
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.model.GlideUrl
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.views.imagehelper.ImageSource
import com.rnscratchcard.tools.ScratchPathManager
import com.rnscratchcard.tools.ScratchPathPoint
import com.rnscratchcard.tools.dp

@SuppressLint("ViewConstructor")
class RNScratchCard(
  context: ThemedReactContext
) : androidx.appcompat.widget.AppCompatImageView(context) {

  var notifyAboutScratchEnabled: Boolean = false

  private val pathManager = ScratchPathManager()
  private val clearPaint: Paint

  private var source: ImageSource? = null
  private var pathStrippedCanvas: Canvas? = null
  private var pathStrippedImage: Bitmap? = null

  private var srcFrame = Rect(0, 0, width, height)
  private var dstFrame = RectF(0f, 0f, width.toFloat(), height.toFloat())

  private val eventDispatcher = UIManagerHelper.getEventDispatcherForReactTag(context, id)

  // parsers
  private val DATA_SCHEME: String = "data"
  private val ANDROID_RESOURCE_SCHEME = "android.resource"
  private val ANDROID_CONTENT_SCHEME = "content"
  private val LOCAL_FILE_SCHEME = "file"

  init {
    clearPaint = createBaseScratchPaint()
  }

  fun setSource(source: ReadableMap) {
    this.source = ImageSource(context, source.getString("uri"))
  }

  fun revalidate(requestManager: RequestManager?) {
    if(source == null) {
      Log.w("RNScratchCard", "Image source is not set")
      return
    }

    if(requestManager == null) {
      Log.w("RNScratchCard", "Request manager is not set")
      return
    }

    val sourceUri = this.source?.uri!!
    val source: Any? = when(sourceUri.scheme) {
      ANDROID_CONTENT_SCHEME -> this.source
      DATA_SCHEME -> this.source
      ANDROID_RESOURCE_SCHEME -> sourceUri
      LOCAL_FILE_SCHEME -> sourceUri.toString()
      else -> GlideUrl(sourceUri.toString())
    }

    val builder = requestManager.load(source)

    builder.into(this)
  }

  private fun initCanvas(): Boolean {
    if (this.drawable is BitmapDrawable) {
      this.pathStrippedImage = (this.drawable as BitmapDrawable).bitmap

      pathStrippedCanvas = Canvas(pathStrippedImage!!)
      srcFrame = Rect(0, 0, pathStrippedImage!!.width, pathStrippedImage!!.height)
      dstFrame = RectF(0f, 0f, width.toFloat(), height.toFloat())
      pathManager.setScale(
        srcFrame.width() / dstFrame.width(),
        srcFrame.height() / dstFrame.height()
      )

      // Initialized
      return true
    }

    return false
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
    if(pathStrippedImage == null) {
      Log.e("RNScratchCard", "Path stripped image is null")

      val initialized = initCanvas()
      if (!initialized) return;
    }

    // No shot this is null at this point
    canvas.drawBitmap(pathStrippedImage!!, srcFrame, dstFrame,null)
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
