package com.rnscratchcard

import com.facebook.react.bridge.ReadableMap
import com.facebook.react.common.MapBuilder
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp
import com.facebook.react.views.imagehelper.ImageSource
import com.rnscratchcard.tools.px


class RnScratchCardViewManager : SimpleViewManager<RNScratchCard>() {

  override fun getName() = "RnScratchCardView"

  override fun createViewInstance(reactContext: ThemedReactContext): RNScratchCard {
    return RNScratchCard(reactContext)
  }

  override fun getExportedCustomDirectEventTypeConstants(): MutableMap<String, Any> {
    return MapBuilder.of(
      "topOnScratch", MapBuilder.of("registrationName", "onScratch")
    )
  }

  @ReactProp(name = "image")
  fun setImage(view: RNScratchCard, source: ReadableMap) {
    view.setSource(ImageSource(view.context, source.getString("uri")))
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
