import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";

function NavBar() {
  const [searchQuery, setSearchQuery] = useState(""); // State for the search query
  const [games, setGames] = useState([]); // State for all games
  const [filteredGames, setFilteredGames] = useState([]); // State for filtered games

  useEffect(() => {
    // Fetch all games only once when the component mounts
    fetch("http://localhost:8080/game") // Adjust this URL if necessary
      .then((response) => response.json())
      .then((data) => {
        setGames(data);
        console.log(data);
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
    <nav>
      <Link to={"/"}>Home</Link>
      <Link to={"/login"}>Login</Link>
      <Link to={"/register"}>Register</Link>

      {/* Search Bar */}
      <div>
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
          <ul>
            {filteredGames.map((game) => (
              <li key={game.game_id}>
                {/* Link to the game details page using the game's ID */}
                <Link to={`/game/${game.game_id}`}>{game.title}</Link>
              </li>
            ))}
          </ul>
        )}
      </div>
    </nav>
  );
}

export default NavBar;