import { useEffect, useState } from "react";

function Home() {
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
      .then((data) => setGames(data.filter((game) => game.gameId != 1 && game.gameId != 2)) // take out deleted and default game
    )
      .catch(console.log);
  }, []);

  return (
    <div>
      <h1>Lorem Ipsum Game Reviews</h1>
      <section>
        <h3 className="mb-4">Top 10 Games</h3>
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
            {[...games]
              .filter((game) => game.rating !== "N/A")
              .sort((a, b) => parseFloat(b.rating) - parseFloat(a.rating))
              .slice(0, 10)
              .map((game) => (
                <tr key={game.gameId}>
                  <td>{game.title}</td>
                  <td>{game.developer}</td>
                  <td>{game.yearReleased}</td>
                  <td>{game.region}</td>
                  <td>{game.rating}</td>
                </tr>
              ))}
          </tbody>
        </table>
      </section>
    </div>
  );
}

export default Home;
