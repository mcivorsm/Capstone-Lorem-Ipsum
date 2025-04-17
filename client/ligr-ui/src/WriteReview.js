import React, { useState } from "react";

function WriteReview({ gameId, onClose }) {
  const [rating, setRating] = useState(5);
  const [reviewText, setReviewText] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();

    const token = localStorage.getItem("jwtToken");
    const decoded = parseJwt(token);
    console.log(decoded?.userId);
    const reviewData = {
      game: {
        gameId: parseInt(gameId)
      },
      user: {
        userId: decoded?.userId
      },
      reviewText: reviewText,
      rating: parseFloat(rating)
    };
    fetch("http://localhost:8080/gameReview", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${localStorage.getItem("jwtToken")}`, 
      },
      body: JSON.stringify(reviewData),
    })
      .then((res) => {
        if (!res.ok) {
          throw new Error("Failed to submit review");
        }
        return res.json();
      })
      .then(() => {
        alert("Review submitted!");
        onClose(); // close form
        window.location.reload(); // optional: refresh page to show new review
      })
      .catch((err) => {
        alert("Error submitting review: " + err.message);
      });
  };

  return (
    <form
      onSubmit={handleSubmit}
      style={{
        maxWidth: "600px",
        margin: "0 auto 2rem auto",
        padding: "1rem",
        border: "1px solid #ccc",
        borderRadius: "8px",
        backgroundColor: "#f9f9f9",
      }}
    >
      <h3 style={{ marginBottom: "1rem" }}>Write a Review</h3>

      <label>
        Rating (1 to 5):
        <input
          type="number"
          min="1"
          max="5"
          step="0.1"
          value={rating}
          onChange={(e) => setRating(e.target.value)}
          required
          style={{ display: "block", marginBottom: "1rem", width: "100%" }}
        />
      </label>

      <label>
        Your Review:
        <textarea
          value={reviewText}
          onChange={(e) => setReviewText(e.target.value)}
          required
          style={{ display: "block", marginBottom: "1rem", width: "100%", height: "100px" }}
        />
      </label>

      <button type="submit" style={{ padding: "0.5rem 1rem", marginRight: "1rem" }}>Submit</button>
      <button type="button" onClick={onClose} style={{ padding: "0.5rem 1rem" }}>Cancel</button>
    </form>
  );
}
function parseJwt(token) {
    try {
      const base64Url = token.split('.')[1]; // Get payload
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
      }).join(''));
      return JSON.parse(jsonPayload);
    } catch (e) {
      return null;
    }
  }
export default WriteReview;