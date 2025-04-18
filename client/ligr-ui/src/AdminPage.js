import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

function AdminPage() {
  const [games, setGames] = useState([]);
  const [users, setUsers] = useState([]);
  const url = "http://localhost:8080";

  // fetch all games on component load
  useEffect(() => {
    fetch(`${url}/game`)
      .then((response) => {
        if (response.status === 200) {
          return response.json();
        } else {
          return Promise.reject(`Unexpected Status Code: ${response.status}`);
        }
      })
      .then((data) => {
        // Once you have the games, get their average ratings
        return Promise.all(
          data.map(async (game) => {
            try {
              const res = await fetch(
                `http://localhost:8080/gameReview/game/${game.gameId}/avg`
              );
              const avg = await res.json();
              return { ...game, rating: avg.toFixed(1) };
            } catch (error) {
              console.error(
                "Failed to fetch rating for game",
                game.title,
                error
              );
              return { ...game, rating: "N/A" }; // fallback if rating fails
            }
          })
        );
      })
      .then((data) => setGames(data))
      .catch(console.log);
  }, []);

  // fetch all users on component load
  useEffect(() => {
    fetch(`${url}/user`)
      .then((response) => {
        if (response.status === 200) {
          return response.json();
        } else {
          return Promise.reject(`Unexpected Status Code: ${response.status}`);
        }
      })
      .then((data) => setUsers(data))
      .catch(console.log);
  }, []);

  // handle deleting a game
  const handleDeleteGame = (gameId) => {
    const token = localStorage.getItem("jwtToken");
    const game = games.find((game) => game.gameId === gameId);
    if (window.confirm(`Delete Game: ${game.title}?`)) {
      const init = {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      };
      fetch(`${url}/game/${gameId}`, init)
        .then((response) => {
          if (response.status === 204) {
            // create a copy of the array
            // remove the game
            const newGames = games.filter((game) => game.gameId !== gameId);
            // update the games state
            setGames(newGames);
          } else {
            return Promise.reject(`Unexpected Status Code: ${response.status}`);
          }
        })
        .catch(console.log);
    }
  };

  return (
    <div>
      <h2>Admin Page</h2>
      <div>
        <section>
          <h3 className="mb-4">All Games</h3>
          <Link className="btn btn-outline-success mb-4" to={"/game/add"}>
            Add Game
          </Link>
          <table className="table table-striped table-hover">
            <thead style={{ backgroundColor: "#003366", color: "white" }}>
              <tr>
                <th>Name</th>
                <th>Developer</th>
                <th>Year Released</th>
                <th>Top Sale Region</th>
                <th>Rating</th>
                <th>&nbsp;</th>
              </tr>
            </thead>
            <tbody>
              {games.map((game, index) => (
                <tr
                  key={game.gameId}
                  style={{
                    backgroundColor: index % 2 === 0 ? "#e3f2fd" : "white", // light blue for striped rows
                  }}
                >
                  <td
                    style={{
                      color: "#0056b3",
                    }}
                  >
                    {game.title}
                  </td>
                  <td
                    style={{
                      color: "#0056b3",
                    }}
                  >
                    {game.developer}
                  </td>
                  <td
                    style={{
                      color: "#0056b3",
                    }}
                  >
                    {game.yearReleased}
                  </td>
                  <td
                    style={{
                      color: "#0056b3",
                    }}
                  >
                    {game.region}
                  </td>
                  <td
                    style={{
                      color: "#0056b3",
                    }}
                  >
                    {game.rating}
                  </td>
                  <td>
                    <Link
                      className="btn btn-outline-info mr-4"
                      to={`/game/edit/${game.gameId}`}
                    >
                      Update
                    </Link>
                    <button
                      className="btn btn-outline-danger"
                      onClick={() => handleDeleteGame(game.gameId)}
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </section>
      </div>
    </div>
  );
}

export default AdminPage;
