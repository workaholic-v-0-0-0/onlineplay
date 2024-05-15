import React, { useEffect, useState } from 'react';
import { w3cwebsocket as W3CWebSocket } from 'websocket';
import GameBoard from './GameBoard';

function App() {
  const [game, setGame] = useState(null);
  const [isConnected, setIsConnected] = useState(false);
  const [client, setClient] = useState(null);

  const playerId = window.playerId;

  useEffect(() => {
    // ensure gameId is defined
    if (!window.gameId) return;

    //const newClient = new W3CWebSocket(`wss://localhost:8443/webapp/game/${window.gameId}`);
    //const newClient = new W3CWebSocket(`wss://192.168.0.14:8443/webapp/game/${window.gameId}`);
    const newClient = new W3CWebSocket(`wss://caltuli.online/webapp_version_sylvain/game/${window.gameId}`);
    //const newClient = new W3CWebSocket(`wss://192.168.0.20:8443/webapp/game/${window.gameId}`);
    //const newClient = new W3CWebSocket(`wss://192.168.180.246:8443/webapp/game/${window.gameId}`);
    setClient(newClient);

    newClient.onopen = () => {
      console.log('WebSocket Client Connected');
      setIsConnected(true);
      try {
          const parsedState = JSON.parse(window.game);
          console.log('Parsed initialState:', parsedState);
          setGame(parsedState);
      } catch (error) {
        console.error('Error parsing initialState:', error);
      }
    };

    newClient.onmessage = function(event) {
        const data = JSON.parse(event.data);
        console.log("Mise à jour de l'état du jeu reçue", data);
        if (data.update === "secondPlayer") {
            setGame(prevState => ({
                ...prevState,
                secondPlayer: JSON.parse(data.newValue)
            }));
        }
        if (data.update === "gameState") {
            setGame(prevState => ({
                ...prevState,
                gameState: JSON.parse(data.newValue)
            }));
        }
        if (data.update === "colorsGrid") {
            setGame(prevState => ({
                ...prevState,
                colorsGrid: {
                    ...prevState.colorsGrid,
                    [`${data.x}-${data.y}`]: data.color
                }
            }));
        }
    };

    newClient.onclose = () => {
      console.log('WebSocket closed');
      setIsConnected(false);
    };

    newClient.onerror = (error) => {
      console.error('WebSocket error:', error);
    };

    return () => newClient.close();
  }, []);


useEffect(() => {
    console.log("Updated game:", game);
  }, [game]);  // Ce useEffect se déclenchera à chaque mise à jour de game

const handlePlay = (columnIndex) => {
    console.log(`handlePlay is called for column ${columnIndex}.`);

    if (client && isConnected && game) {
          const isFirstPlayerTurn = game.gameState === 'WAIT_FIRST_PLAYER_MOVE';
          const isSecondPlayerTurn = game.gameState === 'WAIT_SECOND_PLAYER_MOVE';
        if ((isFirstPlayerTurn && (playerId == game.firstPlayer.id)) ||
            (isSecondPlayerTurn && (playerId == game.secondPlayer.id))) {
          var move = {
              update: 'colorsGrid',
              column: columnIndex,
              playerId: playerId
          };
        client.send(JSON.stringify(move));
      } else {
            console.log("Not your turn.");
      }
    }

};

const formatColorsGrid = (colorsGrid) => {
    if (!colorsGrid) {
      return {};
    }
    const formattedGrid = {};
    for (const [key, value] of Object.entries(colorsGrid)) {
      formattedGrid[key] = value.toLowerCase();
    }
    return formattedGrid;
  };

  return (
    <div className="App">
      {isConnected ? <p>Connected to server</p> : <p>Disconnected</p>}
      <p>Current Game: {JSON.stringify(game)}</p>
      {game && game.colorsGrid && (
              <GameBoard
                colorsGrid={formatColorsGrid(game.colorsGrid)}
                handlePlay={handlePlay}
                playerId={playerId}
                gameState={game.gameState}
                firstPlayerId={game.firstPlayer.id}
                secondPlayerId={game.secondPlayer ? game.secondPlayer.id : null}
              />
            )}
    </div>
  );
}

export default App;
