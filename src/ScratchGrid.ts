/*
    Fastest way to calculate percentage of rect area covered by unknown shape is downsampling.
    A (cell width = brush stroke diameter) is the best choice.
    Having bigger cells means in each cell more area remains uncleared when cell is marked.
    Having smaller cells can result in unmarked cells on cleared part of the screen.
*/

export type Size = {
  width: number
  height: number
}

export type Point = {
  x: number
  y: number
}

export class ScratchGrid {
  percentCompleted = 0

  readonly size: Size
  private readonly grid: boolean[][]
  private readonly totalCellsCount: number
  private readonly cellWidth: number
  private markedCellsCount = 0
  private showDebugLogs: boolean

  constructor(rectSize: Size, lineWidth: number, showDebugLogs: boolean = false) {
    const numberOfElementsInRow = Math.ceil(rectSize.width / lineWidth) // x
    const rowsCount = Math.ceil(rectSize.height / lineWidth) // y
    this.grid = Array(rowsCount)
      .fill([])
      .map(() => Array<boolean>(numberOfElementsInRow).fill(false))
    this.totalCellsCount = rowsCount * numberOfElementsInRow
    this.cellWidth = lineWidth
    this.size = rectSize
    this.showDebugLogs = showDebugLogs
  }

  update(scratchPoint: Point) {
    if (this.isCellScratched(scratchPoint)) {
      this.markedCellsCount += 1
    }
    this.percentCompleted = (this.markedCellsCount / this.totalCellsCount) * 100

    if (this.showDebugLogs) {
      // For Debug Console autoscroll in VSCode, uncheck Settings > Debug > Console: Word Wrap
      for (let row of this.grid) {
        console.debug(row.map((isMarked: boolean) => (isMarked ? '*' : ' ')))
      }
      console.debug(' ')
    }
  }

  isCellScratched(brush: Point): boolean {
    const rowIndex = Math.floor(brush.y / this.cellWidth)
    const elementInRowIndex = Math.floor(brush.x / this.cellWidth)
    const cell = this.grid[rowIndex][elementInRowIndex]

    if (!cell) {
      this.grid[rowIndex][elementInRowIndex] = true
      return true
    }

    return false
  }
}
