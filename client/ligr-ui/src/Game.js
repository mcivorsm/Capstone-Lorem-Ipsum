import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom"; // For accessing URL parameters

function Game() {
  const { gameId } = useParams(); // Get the gameId from the URL
  const [gameDetails, setGameDetails] = useState(null); 
  const [reviews, setReviews] = useState([]);
  const [error, setError] = useState(null); // Store any errors

  useEffect(() => {
    // Fetch game details
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

    // Fetch reviews for the game
    fetch(`http://localhost:8080/gameReview/game/${gameId}`)
      .then((response) => {
        if (!response.ok) {
          throw new Error("Reviews not found");
        }
        return response.json();
      })
      .then((data) => {
        setReviews(data);
      })
      .catch((error) => {
        console.error("Error fetching reviews:", error);
      });
  }, [gameId]);

  if (error) {
    return <div style={{ textAlign: "center", marginTop: "2rem" }}>{error}</div>;
  }

  if (!gameDetails) {
    return <div style={{ textAlign: "center", marginTop: "2rem" }}>Loading...</div>;
  }

  return (
    <div style={{ padding: "2rem" }}>
      <h1 style={{ textAlign: "center", marginBottom: "1rem" }}>{gameDetails.title}</h1>
      
      {/* Review button */}
      <div style={{ textAlign: "center", marginBottom: "2rem" }}>
        <button style={{ padding: "0.5rem 1rem", fontSize: "1rem", cursor: "pointer" }}>
          Review this game
        </button>
      </div>

      <div style={{ maxWidth: "600px", margin: "0 auto", textAlign: "left" }}>
        <p><strong>Developer:</strong> {gameDetails.developer}</p>
        <p><strong>Genre:</strong> {gameDetails.genre}</p>
        <p><strong>Year Released:</strong> {gameDetails.yearReleased}</p>
        <p><strong>Platform:</strong> {gameDetails.platform}</p>
        <p><strong>Region:</strong> {gameDetails.region}</p>
      </div>

      {/* Reviews */}
      <div style={{ maxWidth: "600px", margin: "2rem auto", textAlign: "left" }}>
        <h2 style={{ borderBottom: "1px solid #ccc", paddingBottom: "0.5rem" }}>Reviews ({reviews.length})</h2>
        {reviews.length === 0 ? (
          <p style={{ fontStyle: "italic", marginTop: "1rem" }}>No reviews yet for this game.</p>
        ) : (
          <ul style={{ listStyleType: "none", padding: 0 }}>
            {reviews.map((review) => (
              <li
                key={review.gameReviewId}
                style={{
                  backgroundColor: "#f9f9f9",
                  padding: "1rem",
                  borderRadius: "8px",
                  marginBottom: "1rem",
                  boxShadow: "0 2px 4px rgba(0,0,0,0.1)",
                }}
              >
                <p><strong>User:</strong> {review.user.username}</p>
                <p><strong>Rating:</strong> {review.rating} / 5</p>
                <p><strong>Review:</strong> {review.reviewText}</p>
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
}

export default Game;
