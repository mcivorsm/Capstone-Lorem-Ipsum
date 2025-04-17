import { useState, useEffect, useRef, useMemo } from "react";
import { useNavigate, useParams } from "react-router-dom";

const Profile = ({ authUser }) => {
  const [profile, setProfile] = useState(null);
  const [user, setUser] = useState(null);
  const [reviews, setReviews] = useState(null);
  const [editMode, setEditMode] = useState(false);
  const [preferredGenre, setPreferredGenre] = useState(false);
  const [region, setRegion] = useState(false);
  const [editingProfile, setEditingProfile] = useState(false);
  const dropdownRef = useRef(null);
  const [inputValue, setInputValue] = useState("");
  const [showDropdown, setShowDropdown] = useState(false);
  const [searchQuery, setSearchQuery] = useState("");
  const [games, setGames] = useState([]); // State for all games
  const navigate = useNavigate();
  const [errors, setErrors] = useState([]);

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
  }, [profileId]);

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

  //PROFILE EDITING

  useEffect(() => {
    if (editMode && profile) {
      setEditingProfile({ ...profile });
    }
  }, [editMode, profile]);

  const handleChangeProfile = (event) => {
    const { name, value } = event.target;
    setEditingProfile((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmitProfile = () => {
    editingProfile.profileId = userId;
    const init = {
      method: "PUT",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify(editingProfile),
    };
    fetch(`http://localhost:8080/profile/edit`, init)
      .then((response) => {
        if (response.status === 204) {
          return null;
        } else if (response.status === 400) {
          return response.json();
        } else {
          return Promise.reject(`Unexpected status code: ${response.status}`);
        }
      })
      .then((data) => {
        if (!data) {
          setProfile(editingProfile);
          setEditMode(false);
        } else {
          setErrors(data);
        }
      })
      .catch(console.log);
  };

  //find games
  useEffect(() => {
    if (token) {
      fetch("http://localhost:8080/game", {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
        .then((response) => response.json())
        .then((data) => setGames(data))
        .catch((error) => console.error("Error fetching games:", error));
    }
  }, []);

  useEffect(() => {
    const handler = setTimeout(() => {
      setSearchQuery(inputValue);
    }, 300);
    return () => clearTimeout(handler);
  }, [inputValue]);

  const filteredGames = useMemo(() => {
    if (!searchQuery.trim()) return [];
    return games.filter((game) =>
      game.title.toLowerCase().includes(searchQuery.toLowerCase())
    );
  }, [searchQuery, games]);

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
        {errors.length > 0 && (
          <div className="alert alert-danger d-flex flex-column align-items-center w-25 mx-auto">
            <p className="mb-3">The following errors were found:</p>
            <ul>
              {errors.map((error) => (
                <li key={error}>- {error}.</li>
              ))}
            </ul>
          </div>
        )}

        <div>
          <h1>{user?.username}</h1>
          <p>{user?.email}</p>
        </div>

        {/* if user is an admin, or is the owner of the profile, show buttons */}
        {(authUser?.id === user?.id || authUser?.roles.includes("ROLE_ADMIN")) && (
        <div style={{ display: "flex", gap: "1rem" }}>
          <button
            onClick={() => {
              if (editMode) {
                handleSubmitProfile();
              } else {
                navigate("/settings");
              }
            }}
          >
            {" "}
            {editMode ? "Save Changes" : "Account Settings"}
          </button>
          <button onClick={() => setEditMode(!editMode)}>
            {editMode ? "Cancel Edit" : "Edit Profile"}
          </button>
        </div>
        )}
      </section>

      <section>
        <h3 style={{ marginTop: "2rem", display: "flex", gap: "1rem" }}>
          About Me
        </h3>

        <p>
          {editMode ? (
            <input
              type="text"
              name="profileDescription"
              value={
                editingProfile.profileDescription
                  ? editingProfile.profileDescription
                  : ""
              }
              onChange={handleChangeProfile}
            />
          ) : (
            profile?.profileDescription ||
            "This user has not created a description yet."
          )}
        </p>
      </section>

      <section style={{ marginTop: "2rem", display: "flex", gap: "10rem" }}>
        <div>
          <div>
            {editMode ? (
              <div style={{ position: "relative" }} ref={dropdownRef}>
                <label htmlFor="gameSearch" style={{ marginRight: "6px" }}>
                  Favorite Game:
                </label>
                <input
                  type="text"
                  id="gameSearch"
                  placeholder="Search games..."
                  value={inputValue}
                  onChange={(e) => {
                    setInputValue(e.target.value);
                    setShowDropdown(true);
                  }}
                />

                {showDropdown && inputValue.trim() && (
                  <ul
                    style={{
                      position: "absolute",
                      top: "100%",
                      left: 0,
                      width: "100%",
                      backgroundColor: "#fff",
                      border: "1px solid #ccc",
                      zIndex: 1,
                      maxHeight: "200px",
                      overflowY: "auto",
                      padding: "0",
                      margin: "0",
                      listStyle: "none",
                    }}
                  >
                    {filteredGames.length > 0 ? (
                      filteredGames.map((game) => (
                        <li
                          key={game.gameId}
                          style={{ padding: "8px", cursor: "pointer" }}
                          onClick={() => {
                            setEditingProfile((prev) => ({
                              ...prev,
                              favoriteGame: game,
                            }));
                            setInputValue(game.title);
                            setShowDropdown(false);
                          }}
                        >
                          {game.title}
                        </li>
                      ))
                    ) : (
                      <li style={{ padding: "8px", color: "#888" }}>
                        No matches found.
                      </li>
                    )}
                  </ul>
                )}
              </div>
            ) : (
              // Show the favorite game as plain text if not editing
              <p>Favorite Game: {profile?.favoriteGame.title || "None"}</p>
            )}
          </div>
          <p>
            Favorite Genre:{" "}
            {editMode ? (
              <input
                type="text"
                name="preferredGenre"
                value={
                  editingProfile.preferredGenre
                    ? editingProfile.preferredGenre
                    : "None"
                }
                onChange={handleChangeProfile}
              />
            ) : (
              profile?.preferredGenre || "None"
            )}
          </p>
        </div>

        <div>
          <p>
            Region:{" "}
            {editMode ? (
              <select
                name="region"
                value={editingProfile.region}
                onChange={handleChangeProfile}
              >
                <option value="NA">NA</option>
                <option value="EU">EU</option>
                <option value="JP">JP</option>
                <option value="OTHER">OTHER</option>
              </select>
            ) : (
              profile?.region || "None"
            )}
          </p>
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
          {reviews?.length > 0 ? (
            <section>
              {reviews.map((review) => (
                <div
                  key={review.gameReviewId}
                  className="mb-4 p-3 border rounded bg-light"
                >
                  <div
                    style={{
                      display: "flex",
                      justifyContent: "space-between",
                      alignItems: "center",
                    }}
                  >
                    <p>Game: {review.game.title}</p>
                    <p>
                      Rating:{" "}
                      {Number.isInteger(review.rating)
                        ? review.rating.toFixed(1)
                        : review.rating.toFixed(1)}{" "}
                      / 5.0
                    </p>
                  </div>
                  <p className="mb-0">{review.reviewText}</p>
                </div>
              ))}
            </section>
          ) : (
            <p>No reviews from this user.</p>
          )}
        </section>
      </section>
    </>
  );
}

export default Profile;
