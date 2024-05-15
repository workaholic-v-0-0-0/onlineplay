import React from 'react';
import './gameBoard.css';

const GameBoard = ({ colorsGrid, handlePlay, playerId, gameState, firstPlayerId, secondPlayerId }) => {
  const isFirstPlayerTurn = (gameState === 'WAIT_FIRST_PLAYER_MOVE');
  const isSecondPlayerTurn = (gameState === 'WAIT_SECOND_PLAYER_MOVE') || (gameState === 'WAIT_OPPONENT' && playerId === secondPlayerId);

  const isPlayerTurn = (isFirstPlayerTurn && playerId == firstPlayerId) ||
                         (isSecondPlayerTurn && playerId == secondPlayerId);


  console.log("(gameState === 'WAIT_FIRST_PLAYER_MOVE'):", (gameState === 'WAIT_FIRST_PLAYER_MOVE'));
  console.log("playerId === firstPlayerId:", playerId === firstPlayerId);
  console.log("playerId === secondPlayerId:", playerId === secondPlayerId);
  console.log("(isFirstPlayerTurn && playerId === firstPlayerId):", (isFirstPlayerTurn && playerId === firstPlayerId));
  console.log("(isSecondPlayerTurn && playerId === secondPlayerId):", (isSecondPlayerTurn && playerId === secondPlayerId));
  console.log("(isFirstPlayerTurn && playerId === firstPlayerId) || (isSecondPlayerTurn && playerId === secondPlayerId):", (isFirstPlayerTurn && playerId === firstPlayerId) || (isSecondPlayerTurn && playerId === secondPlayerId));
  console.log('gameState:', gameState);
  console.log('isFirstPlayerTurn:', isFirstPlayerTurn);
  console.log('isSecondPlayerTurn:', isSecondPlayerTurn);
  console.log('playerId:', playerId);
  console.log('firstPlayerId:', firstPlayerId);
  console.log('secondPlayerId:', secondPlayerId);
  console.log('isPlayerTurn:', isPlayerTurn);

const renderCell = (x, y) => {
    const cellKey = `${y}-${x}`;
    const cellState = colorsGrid[cellKey] || 'empty';

    return (
      <div
        key={cellKey}
        className={`cell ${cellState.toLowerCase()} ${!isPlayerTurn ? 'disabled' : ''}`}
        onClick={() => handlePlay(x)}
      ></div>
    );
  };

  const renderRow = (y) => {
    const columns = [0, 1, 2, 3, 4, 5, 6];
    return (
      <div key={y} className="row">
        {columns.map(x => renderCell(x, 5 - y))}
      </div>
    );
  };

  const renderGrid = () => {
    const rows = [0, 1, 2, 3, 4, 5];
    return (
      <div className="grid">
        {rows.map(y => renderRow(y))}
      </div>
    );
  };

  return (
    <div className="game-board">
      {renderGrid()}
    </div>
  );
};

export default GameBoard;