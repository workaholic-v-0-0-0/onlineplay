import React, { useEffect } from 'react';
import logo from './leo.png';
import './App.css';
import Grid from './Grid'; // Supposons que vous mettiez le code Grid dans Grid.js

function App() {

useEffect(() => {
      console.log("App component is mounting");
      console.log("logo is ", logo);
    }, []);
  return (
    <div className="App">
      <header className="App-header">
        <img src={`./${logo}`} className="App-logo" alt="loggo" />
        <p>
          Edit <code>src/App.js</code> and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header>
      <Grid /> {/* Utilisation du composant Grid ici */}
    </div>
  );
}

export default App;
