package com.rnscratchcard

import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.common.MapBuilder
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp
import com.rnscratchcard.tools.px


class RnScratchCardViewManager : SimpleViewManager<RNScratchCard>() {
  private var requestManager: RequestManager? = null;

  override fun getName() = "RnScratchCardView"

  override fun createViewInstance(reactContext: ThemedReactContext): RNScratchCard {
    this.requestManager = Glide.with(reactContext)
    return RNScratchCard(reactContext)
  }

  override fun onAfterUpdateTransaction(view: RNScratchCard) {
    super.onAfterUpdateTransaction(view)
    view.revalidate(requestManager)
  }

  override fun getExportedCustomDirectEventTypeConstants(): MutableMap<String, Any> {
    return MapBuilder.of(
      "topOnScratch", MapBuilder.of("registrationName", "onScratch")
    )
  }

  @ReactProp(name = "image")
  fun setImage(view: RNScratchCard, source: ReadableMap) {
    view.setSource(source)
  }

  @ReactProp(name = "brushWidth")
  fun setBrushWidth(view: RNScratchCard, source: Double) {
    view.setBrushWidth(source.toFloat().px)
  }

  @ReactProp(name = "onScratch", defaultBoolean = false)
  fun setOnScratch(view: RNScratchCard, onScratch: Boolean) {
    view.notifyAboutScratchEnabled = onScratch
  }
}
