import React, { useEffect, useState } from 'react';
import { w3cwebsocket as W3CWebSocket } from 'websocket';

function App() {
  const [gameState, setGameState] = useState(null);
  const [isConnected, setIsConnected] = useState(false);

  useEffect(() => {
    // Assurer que gameId est défini
    if (!window.gameId) return;

    const client = new W3CWebSocket(`wss://localhost:8443/webapp/game/${window.gameId}`);

    client.onopen = () => {
      console.log('WebSocket Client Connected');
      setIsConnected(true);
      setGameState(JSON.parse(window.initialState));  // Utilisez l'état initial si disponible
    };

    client.onmessage = function(event) {
        const data = JSON.parse(event.data);
        console.log("Mise à jour de l'état du jeu reçue", data);
        setGameState(data);
    };

    client.onclose = () => {
      console.log('WebSocket closed');
      setIsConnected(false);
    };

    client.onerror = (error) => {
      console.error('WebSocket error:', error);
    };

    return () => client.close();  // Nettoyage en fermant la WebSocket lors du démontage du composant
  }, []);

  return (
    <div className="App">
      {isConnected ? <p>Connected to server</p> : <p>Disconnected</p>}
      <p>Current Game State: {JSON.stringify(gameState)}</p>
    </div>
  );
}

export default App;
