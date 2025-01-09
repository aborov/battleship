# Battleship Game

A two-player Battleship game implemented in Java. Players take turns placing ships and trying to sink their opponent's fleet.

## Features

- Two-player turn-based gameplay
- Interactive ship placement
- Fog of war mechanics
- Ship placement validation
- Console-based user interface

## How to Play

1. Each player places their ships on their field:
   - Aircraft Carrier (5 cells)
   - Battleship (4 cells)
   - Submarine (3 cells)
   - Cruiser (3 cells)
   - Destroyer (2 cells)

2. Players take turns firing shots at their opponent's field:
   - Enter coordinates (e.g., "A1")
   - The game shows if you hit or missed
   - Sink all enemy ships to win

## Game Rules

- Ships cannot overlap or be placed adjacent to each other
- Ships must be placed horizontally or vertically
- Coordinates are in the format "Letter+Number" (e.g., "A1", "B5")
- The game displays 'X' for hits and 'M' for misses
