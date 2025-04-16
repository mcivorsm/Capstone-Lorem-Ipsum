import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom"; // For accessing URL parameters

function Game() {
  const { gameId } = useParams(); // Get the gameId from the URL
  const [gameDetails, setGameDetails] = useState(null); // Store game details
  const [error, setError] = useState(null); // Store any errors

  useEffect(() => {
    // Fetch game details when the component mounts
    fetch(`http://localhost:8080/game/${gameId}`) // Assuming your backend has this endpoint
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
    return <div>{error}</div>;
  }

  if (!gameDetails) {
    return <div>Loading...</div>;
  }

  return (
    <div>
      <h1>{gameDetails.title}</h1>
      <p><strong>Developer:</strong> {gameDetails.developer}</p>
      <p><strong>Genre:</strong> {gameDetails.genre}</p>
      <p><strong>Year Released:</strong> {gameDetails.yearReleased}</p>
      <p><strong>Platform:</strong> {gameDetails.platform}</p>
      <p><strong>Description:</strong> {gameDetails.description}</p>

      {/* You can add more fields here based on what your backend returns */}
    </div>
  );
}

export default Game;