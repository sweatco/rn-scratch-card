package com.rnscratchcard

import android.view.View
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import com.facebook.react.common.MapBuilder
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp
import com.rnscratchcard.tools.dp
import com.rnscratchcard.tools.px

class RnScratchCardViewManager : SimpleViewManager<View>() {

  override fun getName() = "RnScratchCardView"

  override fun createViewInstance(reactContext: ThemedReactContext): View {
    return RNScratchCard(reactContext)
  }

  override fun getExportedCustomDirectEventTypeConstants(): MutableMap<String, Any> {
    return MapBuilder.of(
      "topOnScratch", MapBuilder.of("registrationName", "onScratch")
    )
  }

  @ReactProp(name = "image")
  fun setImage(view: View, source: ReadableMap) {
    val array = WritableNativeArray().also {
      it.pushMap(WritableNativeMap().also {
        it.putString("uri", source.getString("uri"))
      })
    }
    (view as RNScratchCard).setSource(array)
  }

  @ReactProp(name = "brushWidth")
  fun setBrushWidth(view: View, source: Double) {
    (view as RNScratchCard).setBrushWidth(source.toFloat().px)
  }

  @ReactProp(name = "onScratch", defaultBoolean = false)
  fun setOnScratch(view: View, onScratch: Boolean) {
    (view as RNScratchCard).notifyAboutScratchEnabled = onScratch
  }
}
