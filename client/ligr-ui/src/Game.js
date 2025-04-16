import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom"; // For accessing URL parameters

function Game() {
  const { gameId } = useParams(); // Get the gameId from the URL
  const [gameDetails, setGameDetails] = useState(null); 
  const [error, setError] = useState(null); // Store any errors

  useEffect(() => {
    // Fetch game details when the component mounts
    fetch(`http://localhost:8080/game/${gameId}`) 
      .then((response) => {
        if (!response.ok) {
          throw new Error("Game not found");
        }
        return response.json();
      })
      .then((data) => {
        setGameDetails(data);
      })
      .catch((error) => {
        setError(error.message);
      });
  }, [gameId]); // Only re-run the effect if the gameId changes

  // Render loading or error states
  if (error) {
    return <div style={{ textAlign: "center", marginTop: "2rem" }}>{error}</div>;
  }

  if (!gameDetails) {
    return <div style={{ textAlign: "center", marginTop: "2rem" }}>Loading...</div>;
  }

  return (
    <div style={{ padding: "2rem" }}>
      <h1 style={{ textAlign: "center", marginBottom: "2rem" }}>{gameDetails.title}</h1>
      <div style={{ maxWidth: "600px", margin: "0 auto", textAlign: "left" }}>
        <p><strong>Developer:</strong> {gameDetails.developer}</p>
        <p><strong>Genre:</strong> {gameDetails.genre}</p>
        <p><strong>Year Released:</strong> {gameDetails.yearReleased}</p>
        <p><strong>Platform:</strong> {gameDetails.platform}</p>
        <p><strong>Region:</strong> {gameDetails.region}</p>
      </div>
    </div>
  );
}

export default Game;