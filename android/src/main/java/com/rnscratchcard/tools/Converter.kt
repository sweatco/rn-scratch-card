package com.rnscratchcard.tools

import android.content.res.Resources

val Float.dp: Float
  get() = (this / Resources.getSystem().displayMetrics.density)

val Float.px: Float
  get() = (this * Resources.getSystem().displayMetrics.density)
