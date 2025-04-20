import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom"; // For accessing URL parameters
import WriteReview from "./WriteReview";

function Game({ authUser }) {
  const { gameId } = useParams(); // Get the gameId from the URL
  const [gameDetails, setGameDetails] = useState(null);
  const [reviews, setReviews] = useState([]);
  const [error, setError] = useState(null); // Store any errors
  const [showReviewForm, setShowReviewForm] = useState(false);
  const [rating, setRating] = useState();
  const url = "http://LoremIpsumBackendServicesEC2-env.eba-9tm8q273.us-east-2.elasticbeanstalk.com";

  useEffect(() => {
    // Fetch game details
    fetch(`${url}/game/${gameId}`)
      .then((response) => {
        if (!response.ok) {
          throw new Error("Game not found");
        }
        return response.json();
      })
      .then((data) => {
        console.log("hello testing build pl");
        setGameDetails(data);
      })
      .catch((error) => {
        setError(error.message);
      });

    // Fetch reviews for the game
    fetch(`${url}/gameReview/game/${gameId}`)
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

      fetch(`${url}/gameReview/game/${gameId}/avg`)
      .then((response) => {
        if (!response.ok) {
          throw new Error("Game not found");
        }
        return response.json();
      })
      .then(avg => setRating(avg.toFixed(1)))
      .catch((error) => {
        setError(error.message);
      });
  }, [gameId]);


  const handleDeleteReview = (gameReviewId) => {
    const token = localStorage.getItem("jwtToken");
    const gameReview = reviews.find(
      (review) => review.gameReviewId === gameReviewId
    );
    if (window.confirm(`Delete Review?`)) {
      const init = {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      };
      fetch(`${url}/gameReview/${gameReviewId}`, init)
        .then((response) => {
          if (response.status === 204) {
            // create a copy of the array
            // remove the game review
            const newReviews = reviews.filter(
              (review) => review.gameReviewId !== gameReviewId
            );
            // update the reviews state
            setReviews(newReviews);
          } else {
            return Promise.reject(`Unexpected Status Code: ${response.status}`);
          }
        })
        .catch(console.log);
    }
  };

  if (error) {
    return (
      <div style={{ textAlign: "center", marginTop: "2rem" }}>{error}</div>
    );
  }

  if (!gameDetails) {
    return (
      <div style={{ textAlign: "center", marginTop: "2rem" }}>Loading...</div>
    );
  }

  return (
    <div style={{ padding: "2rem" }}>
      <h1 style={{ textAlign: "center", marginBottom: "1rem" }}>
        {gameDetails.title}
      </h1>

      {/* Review Button */}
      <div style={{ textAlign: "center", marginBottom: "2rem" }}>
        <button
          onClick={() => setShowReviewForm(!showReviewForm)}
          style={{
            padding: "0.5rem 1rem",
            fontSize: "1rem",
            cursor: "pointer",
          }}
        >
          {showReviewForm ? "Cancel" : "Review this game"}
        </button>
      </div>

      {/* Conditional Form */}
      {showReviewForm && (
        <WriteReview gameId={gameId} onClose={() => setShowReviewForm(false)} />
      )}

      <div style={{ maxWidth: "600px", margin: "0 auto", textAlign: "left" }}>
        <p
          style={{
            color: "#0056b3",
          }}
        >
          <strong>Developer:</strong> {gameDetails.developer}
        </p>
        <p
          style={{
            color: "#0056b3",
          }}
        >
          <strong>Genre:</strong> {gameDetails.genre}
        </p>
        <p
          style={{
            color: "#0056b3",
          }}
        >
          <strong>Year Released:</strong> {gameDetails.yearReleased}
        </p>
        <p
          style={{
            color: "#0056b3",
          }}
        >
          <strong>Platform:</strong> {gameDetails.platform}
        </p>
        <p
          style={{
            color: "#0056b3",
          }}
        >
          <strong>Region:</strong> {gameDetails.region}
        </p>

        <p
          style={{
            color: "#0056b3",
          }}
        >
          <strong>Rating:</strong> {rating} / 5.0
        </p>
      </div>

      

      {/* Reviews */}
      <div
        style={{ maxWidth: "600px", margin: "2rem auto", textAlign: "left" }}
      >
        <h2 style={{ borderBottom: "1px solid #ccc", paddingBottom: "0.5rem" }}>
          Reviews ({reviews.length})
        </h2>
        {reviews.length === 0 ? (
          <p style={{ fontStyle: "italic", marginTop: "1rem" }}>
            No reviews yet for this game.
          </p>
        ) : (
          <ul style={{ listStyleType: "none", padding: 0 }}>
            {reviews.map((review) => (
              <li
                key={review.gameReviewId}
                style={{
                  backgroundColor: "#e3f2fd",
                  padding: "1rem",
                  borderRadius: "8px",
                  marginBottom: "1rem",
                  boxShadow: "0 2px 4px rgba(0,0,0,0.1)",
                }}
              >
                <p
                  style={{
                    color: "#0056b3",
                  }}
                >
                  <strong>User:</strong> {review.user.username}
                </p>
                <p
                  style={{
                    color: "#0056b3",
                  }}
                >
                  <strong>Rating:</strong> {review.rating} / 5
                </p>
                <p
                  style={{
                    color: "#0056b3",
                  }}
                >
                  <strong>Review:</strong> {review.reviewText}
                </p>

                {/* if user is an admin, or is the author of the review, show delete button */}
                {(authUser?.id === review.user.id ||
                  authUser?.roles.includes("ROLE_ADMIN")) && (
                  <button
                    style={{
                      marginTop: "0.5rem",
                      padding: "0.4rem 0.8rem",
                      backgroundColor: "lightcoral",
                      border: "none",
                      borderRadius: "4px",
                      cursor: "pointer",
                    }}
                    onClick={() => handleDeleteReview(review.gameReviewId)}
                  >
                    Delete
                  </button>
                )}
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
}

export default Game;
