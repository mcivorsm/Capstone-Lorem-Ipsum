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
      .then(
        (data) =>
          setGames(data.filter((game) => game.gameId != 1 && game.gameId != 2)) // take out deleted and default game
      )
      .catch(console.log);
  }, []);

  return (
    <div>
      <h1>Lorem Ipsum Game Reviews</h1>
      <section>
        <h3 className="mb-4">Top 10 Games</h3>
        <table className="table table-striped table-hover">
          <thead style={{ backgroundColor: "#003366", color: "white" }}>
            <tr>
              <th>Name</th>
              <th>Developer</th>
              <th>Year Released</th>
              <th>Top Sale Region</th>
              <th>Rating</th>
            </tr>
          </thead>
          <tbody>
            {[...games]
              .filter((game) => game.rating !== "N/A")
              .sort((a, b) => parseFloat(b.rating) - parseFloat(a.rating))
              .slice(0, 10)
              .map((game, index) => (
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
                </tr>
              ))}
          </tbody>
        </table>
      </section>
    </div>
  );
}

export default Home;
