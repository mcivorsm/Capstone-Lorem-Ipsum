import React, { useState, useEffect, useMemo, useRef } from "react";
import { useNavigate, Link } from "react-router-dom";

const NavBar = ({ setToken, authUser }) => {
  const [searchQuery, setSearchQuery] = useState(""); // State for the search query
  const [inputValue, setInputValue] = useState(""); // for debounced input
  const [games, setGames] = useState([]); // State for all games
  const [showLogoutPopup, setShowLogoutPopup] = useState(false);
  const [showDropdown, setShowDropdown] = useState(false);
  const navigate = useNavigate();
  const dropdownRef = useRef(null);
  const token = localStorage.getItem("jwtToken");

  useEffect(() => {
    // Fetch all games only once when the component mounts
    if (token) {
      fetch("http://localhost:8080/game", {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`, // Attach token to request
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

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setShowDropdown(false);
      }
    };
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  return (
    <nav style={{ position: "relative" }}>
      {authUser?.roles.includes("ROLE_ADMIN") ? (<Link to={"/admin"}>Admin Page</Link>) : null}
      <Link to={"/"}>Home</Link>
      <Link to={"/userlist"}>Users</Link>
      <Link to={"/profile"}>Profile</Link>
      <button className="btn logout" onClick={() => setShowLogoutPopup(true)}>Logout</button>

      {showLogoutPopup && (
        <div
          style={{
            position: "absolute",
            top: "100%",
            right: 0,
            background: "#fff",
            border: "1px solid #ccc",
            padding: "1rem",
            zIndex: 100,
          }}
        >
          <p>Are you sure you want to log out?</p>
          <button
            onClick={() => {
              localStorage.removeItem("jwtToken");
              setToken(null);
              navigate("/");
            }}
          >
            Yes
          </button>
          <button onClick={() => setShowLogoutPopup(false)}>Cancel</button>
        </div>
      )}

      {/* Search Bar */}
      <div style={{ position: "relative" }} ref={dropdownRef}>
      <label htmlFor="gameSearch" style= {{marginRight: "6px", marginTop: "22px"}}>Find a Game: </label>
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
              overflowY: "auto", // To make it scrollable if the list is long
              padding: "0",
              margin: "0",
              listStyle: "none",
            }}
          >
            {filteredGames.length > 0 ? (
              filteredGames.map((game) => (
                <li key={game.gameId} style={{ padding: "8px" }}>
                  {/* Link to the game details page using the game's ID */}
                  <Link
                    to={`/game/${game.gameId}`}
                    style={{ textDecoration: "none", color: "black" }}
                    onClick={() => {
                      setInputValue("");
                      setSearchQuery("");
                      setShowDropdown(false);
                    }}
                  >
                    {game.title}
                  </Link>
                </li>
              ))
            ) : (
              <li style={{ padding: "8px", color: "#888" }}>No matches found.</li>
            )}
          </ul>
        )}
      </div>
    </nav>
  );
};

export default NavBar;
