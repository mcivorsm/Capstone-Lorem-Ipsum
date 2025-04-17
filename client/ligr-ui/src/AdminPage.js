import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

function AdminPage() {
  const [games, setGames] = useState([]);
  const url = "http://localhost:8080/game";

  useEffect(() => {
    fetch(url)
      .then((response) => {
        if (response.status === 200) {
          return response.json();
        } else {
          return Promise.reject(`Unexpected Status Code: ${response.status}`);
        }
      })
      .then((data) => setGames(data))
      .catch(console.log);
  }, []);

  const handleDeleteGame = (gameId) => {
    const game = games.find((game) => game.gameId === gameId);
    if (window.confirm(`Delete Game: ${game.title}?`)) {
      const init = {
        method: "DELETE",
      };
      fetch(`${url}/${gameId}`, init)
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
            <thead className="thead-dark">
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
              {games.map((game) => (
                <tr key={game.gameId}>
                  <td>{game.title}</td>
                  <td>{game.developer}</td>
                  <td>{game.yearReleased}</td>
                  <td>{game.region}</td>
                  <td>{game.rating}</td>
                  <td>
                    <Link
                      className="btn btn-outline-warning mr-4"
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
