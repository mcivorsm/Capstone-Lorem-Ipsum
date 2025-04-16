import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";

function NavBar() {
  const [searchQuery, setSearchQuery] = useState(""); // State for the search query
  const [games, setGames] = useState([]); // State for all games
  const [filteredGames, setFilteredGames] = useState([]); // State for filtered games
  const [showLogoutPopup, setShowLogoutPopup] = useState(false);

  useEffect(() => {
    // Fetch all games only once when the component mounts
    fetch("http://localhost:8080/game") // Adjust this URL if necessary
      .then((response) => response.json())
      .then((data) => {
        setGames(data);
        setFilteredGames(data); // Initialize filtered games with all games
      })
      .catch((error) => console.error("Error fetching games:", error));
  }, []);

  useEffect(() => {
    // Filter the games based on search query
    if (searchQuery.trim() === "") {
      setFilteredGames(games); // If the search query is empty, show all games
    } else {
      const filtered = games.filter((game) =>
        game.title.toLowerCase().includes(searchQuery.toLowerCase()) // Case-insensitive search
      );
      setFilteredGames(filtered);
    }
  }, [searchQuery, games]); // Re-run the filter whenever the search query or games change

  return (
    <nav style={{ position: "relative" }}>
      <Link to={"/"}>Home</Link>
      <Link to={"/profile"}>Profile</Link>
      <button onClick={() => setShowLogoutPopup(true)}>Logout</button>

      {showLogoutPopup && (
        <div style={{
          position: "absolute",
          top: "100%",
          right: 0,
          background: "#fff",
          border: "1px solid #ccc",
          padding: "1rem",
          zIndex: 100
        }}>
          <p>Are you sure you want to log out?</p>
          <button onClick={() => localStorage.removeItem("jwtToken")}>Yes</button>
          <button onClick={() => setShowLogoutPopup(false)}>Cancel</button>
        </div>
      )}

      {/* Search Bar */}
      <div style={{ position: "relative" }}>
        <label htmlFor="gameSearch">Find a Game: </label>
        <input
          type="text"
          id="gameSearch"
          placeholder="Search games..."
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)} // Update the search query state
        />

        {/* Conditionally render the list of filtered games */}
        {filteredGames.length > 0 && searchQuery && (
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
            }}
          >
            {filteredGames.map((game) => (
              <li key={game.game_id} style={{ padding: "8px" }}>
                {/* Link to the game details page using the game's ID */}
                <Link to={`/game/${game.gameId}`} style={{ textDecoration: "none", color: "black" }}>
                  {game.title}
                </Link>
              </li>
            ))}
          </ul>
        )}
      </div>
    </nav>
  );
}

export default NavBar;