import React, { useState } from 'react'
import { Image, View } from 'react-native'

import { RnScratchCard, ScratchCardProps, UserInput } from './RnScratchCard'
import { ScratchGrid } from './ScratchGrid'

export const ScratchCard: React.FC<ScratchCardProps> = (props: ScratchCardProps) => {
  const image = Image.resolveAssetSource(props.source)
  const [grid, setGrid] = useState<ScratchGrid | undefined>()

  return (
    <View
      onLayout={(event) => {
        const { width, height } = event.nativeEvent.layout
        if (grid?.size.width !== width || grid?.size.height !== height) {
          const size = { width, height }
          setGrid(new ScratchGrid(size, props.brushWidth, props.showDebugLogs))
        }
      }}>
      <RnScratchCard image={image} brushWidth={props.brushWidth} onScratch={handleOnScratch} style={props.style} />
    </View>
  )

  function handleOnScratch(event: UserInput) {
    if (props.onScratch && grid) {
      const { nativeEvent } = event
      const x = Math.min(grid.size.width - 1, Math.max(0, nativeEvent.x))
      const y = Math.min(grid.size.height - 1, Math.max(0, nativeEvent.y))
      grid.update({ x, y })
      props.onScratch(grid.percentCompleted)
    }
  }
}
