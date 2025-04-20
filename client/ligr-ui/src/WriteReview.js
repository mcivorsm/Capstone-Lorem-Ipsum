import React, { useState } from "react";

function WriteReview({ gameId, onClose }) {
  const [rating, setRating] = useState(5);
  const [reviewText, setReviewText] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();

    const token = localStorage.getItem("jwtToken");
    const decoded = parseJwt(token);

    const reviewData = {
      game: {
        gameId: parseInt(gameId),
      },
      user: {
        userId: decoded?.userId,
      },
      reviewText: reviewText,
      rating: parseFloat(rating),
    };

    fetch("http://LoremIpsumBackendServicesEC2-env.eba-9tm8q273.us-east-2.elasticbeanstalk.com/gameReview", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(reviewData),
    })
      .then((res) => {
        if (!res.ok) throw new Error("Failed to submit review");
        return res.json();
      })
      .then(() => {
        alert("Review submitted!");
        onClose();
        window.location.reload();
      })
      .catch((err) => {
        alert("Error submitting review: " + err.message);
      });
  };

  return (
    <form
      onSubmit={handleSubmit}
      style={{
        maxWidth: "500px",
        margin: "2rem auto",
        padding: "2rem",
        border: "1px solid #ddd",
        borderRadius: "12px",
        backgroundColor: "#fff",
        boxShadow: "0 4px 12px rgba(0, 0, 0, 0.05)",
        fontFamily: "Arial, sans-serif",
      }}
    >
      <h2 style={{ textAlign: "center", marginBottom: "1.5rem" }}>Write a Review</h2>

      <div style={{ marginBottom: "1.5rem" }}>
        <label style={{ fontWeight: "bold", display: "block", marginBottom: "0.5rem" }}>
          Rating (1 to 5)
        </label>
        <input
          type="number"
          min="1"
          max="5"
          step="0.1"
          value={rating}
          onChange={(e) => setRating(e.target.value)}
          required
          style={{
            width: "100%",
            padding: "0.5rem",
            borderRadius: "6px",
            border: "1px solid #ccc",
          }}
        />
      </div>

      <div style={{ marginBottom: "1.5rem" }}>
        <label style={{ fontWeight: "bold", display: "block", marginBottom: "0.5rem" }}>
          Your Review
        </label>
        <textarea
          value={reviewText}
          onChange={(e) => setReviewText(e.target.value)}
          required
          style={{
            width: "100%",
            height: "120px",
            padding: "0.75rem",
            borderRadius: "6px",
            border: "1px solid #ccc",
            resize: "vertical",
          }}
        />
      </div>

      <div style={{ display: "flex", justifyContent: "space-between" }}>
        <button
          type="submit"
          style={{
            backgroundColor: "#4CAF50",
            color: "white",
            padding: "0.6rem 1.2rem",
            border: "none",
            borderRadius: "6px",
            cursor: "pointer",
            transition: "background-color 0.2s",
          }}
          onMouseOver={(e) => (e.target.style.backgroundColor = "#45a049")}
          onMouseOut={(e) => (e.target.style.backgroundColor = "#4CAF50")}
        >
          Submit
        </button>

        <button
          type="button"
          onClick={onClose}
          style={{
            backgroundColor: "#f44336",
            color: "white",
            padding: "0.6rem 1.2rem",
            border: "none",
            borderRadius: "6px",
            cursor: "pointer",
            transition: "background-color 0.2s",
          }}
          onMouseOver={(e) => (e.target.style.backgroundColor = "#d32f2f")}
          onMouseOut={(e) => (e.target.style.backgroundColor = "#f44336")}
        >
          Cancel
        </button>
      </div>
    </form>
  );
}

function parseJwt(token) {
  try {
    const base64Url = token.split(".")[1];
    const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split("")
        .map((c) => "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2))
        .join("")
    );
    return JSON.parse(jsonPayload);
  } catch (e) {
    return null;
  }
}

export default WriteReview;