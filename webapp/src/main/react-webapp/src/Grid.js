import React, { useState } from 'react';

function Grid() {
  const [grid, setGrid] = useState(createInitialGrid());

  function createInitialGrid() {
    // Initialisez votre grille ici, par exemple avec des couleurs aléatoires
    return new Array(10).fill(null).map(() => new Array(10).fill("white"));
  }

  return (
    <div>
      {grid.map((row, rowIndex) => (
        <div key={rowIndex} style={{ display: "flex" }}>
          {row.map((color, colIndex) => (
            <div key={colIndex} style={{ width: 30, height: 30, backgroundColor: color }} />
          ))}
        </div>
      ))}
    </div>
  );
}

export default Grid;
