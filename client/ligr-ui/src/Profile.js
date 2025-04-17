import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";

function Profile() {
  const [profile, setProfile] = useState(null);
  const [user, setUser] = useState(null);
  const [reviews, setReviews] = useState(null);

  const { profileId } = useParams();
  const token = localStorage.getItem("jwtToken");

  //PROFILE RETRIEVAL
  useEffect(() => {
    const url = profileId
      ? `http://localhost:8080/profile/${profileId}`
      : `http://localhost:8080/profile/`;

    console.log("url: " + url);

    if (token) {
      fetch(url, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`, // Attach token to request
        },
      })
        .then((response) => {
          if (response.status === 200) {
            return response.json();
          } else {
            return Promise.reject(`Unexpected Status Code: ${response.status}`);
          }
        })
        .then((data) => {
          setProfile(data);
        })
        .catch(console.log);
    }
  }, []);

  const userId = (() => {
    if (profileId) {
      return profileId;
    }
    const decodedToken = JSON.parse(atob(token.split(".")[1]));
    const userId = decodedToken.userId;
    return userId;
  })();

  //USER RETRIEVAL FOR USERNAME
  useEffect(() => {
    if (token) {
      fetch(`http://localhost:8080/user/id/${userId}`, {
        headers: { Authorization: `Bearer ${token}` },
      })
        .then((response) => {
          if (response.status === 200) {
            return response.json();
          } else {
            return Promise.reject(`Unexpected Status Code: ${response.status}`);
          }
        })
        .then((data) => setUser(data))
        .catch(console.error);
    }
  }, [token, userId]);

  //GAME REVIEW RETRIEVAL
  useEffect(() => {
    if (token) {
      fetch(`http://localhost:8080/gameReview/user/${userId}`, {
        headers: { Authorization: `Bearer ${token}` },
      })
        .then((response) => {
          if (response.status === 200) {
            return response.json();
          } else {
            return Promise.reject(`Unexpected Status Code: ${response.status}`);
          }
        })
        .then((data) => setReviews(data))
        .catch(console.error);
    }
  }, [token, userId]);

  console.log(reviews);

  return (
    <>
      <section
        style={{
          marginTop: "2rem",
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
        }}
      >
        <div>
          <h1>{user?.username}</h1>
          <p>{user?.email}</p>
        </div>

        <div style={{ display: "flex", gap: "1rem" }}>
          <button> Account Settings</button>
          <button> Edit Profile</button>
        </div>
      </section>

      <section style={{ marginTop: "2rem", display: "flex", gap: "10rem" }}>
        <div>
          <p>Favorite Game: {profile?.favoriteGame.title}</p>
          <p>
            Favorite Genre:{" "}
            {profile?.preferredGenre ? profile?.preferredGenre : "None"}
          </p>
        </div>

        <div>
          <p>Region: {profile?.region}</p>
          <p>Date Joined: {profile?.dateJoined}</p>
        </div>
      </section>

      <section
        className="bg-light p-3"
        style={{
          marginTop: "2rem",
          display: "flex",
          flexDirection: "column",
          gap: "1rem",
        }}
      >
        <h4> Reviews({reviews?.length}) </h4>

        <section>
          {reviews?.map((review) => (
            <div
              key={review.gameReviewId}
              className="mb-4 p-3 border rounded bg-light"
            >
              <div
                style={{
                  display: "grid",
                  gridTemplateColumns: "1fr 1fr",
                  alignItems: "center",
                }}
              >
                <p>Game: {review.game.title}</p>
                <p>Rating: {review.rating.toFixed(1)} / 5.0</p>
              </div>

              <p className="mb-0">{review.reviewText}</p>
            </div>
          ))}
        </section>
      </section>
    </>
  );
}

export default Profile;
