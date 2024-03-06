package com.rnscratchcard

import android.annotation.SuppressLint
import android.graphics.*
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.MotionEvent
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
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

  private var imageSource: ImageSource? = null
  private var pathStrippedCanvas: Canvas? = null
  private var pathStrippedImage: Bitmap? = null
  private var srcFrame = Rect(0, 0, width, height)
  private var dstFrame = RectF(0f, 0f, width.toFloat(), height.toFloat())

  private val eventDispatcher = UIManagerHelper.getEventDispatcherForReactTag(context, id)

  private val requestManager: RequestManager

  // parsers
  private val DATA_SCHEME: String = "data"
  private val ANDROID_RESOURCE_SCHEME = "android.resource"
  private val ANDROID_CONTENT_SCHEME = "content"
  private val LOCAL_FILE_SCHEME = "file"
  private val LOCAL_RESOURCE_SCHEME = "res"

  init {
    clearPaint = createBaseScratchPaint()
    requestManager = Glide.with(context.reactApplicationContext)
  }

  fun setSource(imageSource: ImageSource) {
    if (this.imageSource != imageSource) {
      this.imageSource = imageSource
    } else {
      return
    }
    val source: Any = when(imageSource.uri.scheme) {
      ANDROID_CONTENT_SCHEME -> imageSource
      DATA_SCHEME -> imageSource
      ANDROID_RESOURCE_SCHEME -> imageSource.uri
      LOCAL_FILE_SCHEME -> imageSource.uri.toString()
      LOCAL_RESOURCE_SCHEME -> Uri.parse(imageSource.uri.toString().replace("res:/", ANDROID_RESOURCE_SCHEME + "://" + context.getPackageName() + "/"));
      else -> GlideUrl(imageSource.uri.toString())
    }
    requestManager.asBitmap()
      .load(source)
      .into(object : CustomTarget<Bitmap>() {
        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
          pathStrippedImage = resource.copy(resource.config, true)
          pathStrippedImage?.let {
            pathStrippedCanvas = Canvas(it)
          }
          srcFrame = Rect(0, 0, resource.width, resource.height)
          pathManager.setScale(
            srcFrame.width() / dstFrame.width(),
            srcFrame.height() / dstFrame.height()
          )
          invalidate()
        }

        override fun onLoadCleared(placeholder: Drawable?) {
          // do nothing
        }
      })
  }

  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(event: MotionEvent): Boolean {
    val events = ScratchPathPoint.fromMotionEvent(event)
    pathManager.addScratchPathPoints(events)
    pathStrippedCanvas?.let {
      pathManager.drawLines(it, clearPaint)
    }
    pathStrippedImage?.prepareToDraw()
    notifyScratch(event)
    postInvalidate()
    return true
  }

  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    super.onSizeChanged(w, h, oldw, oldh)
    dstFrame = RectF(0f, 0f, w.toFloat(), h.toFloat())
    pathStrippedImage?.let {
      srcFrame = Rect(0, 0, it.width, it.height)
      pathManager.setScale(
        srcFrame.width().toFloat() / w,
        srcFrame.height().toFloat() / h
      )
    }
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
