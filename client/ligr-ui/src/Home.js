import { useEffect, useState } from "react";
import MasterChiefImg from "./Master_Chief_in_Halo_5.webp"; // adjust path if needed

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
              return { ...game, rating: "N/A" };
            }
          })
        );
      })
      .then((data) =>
        setGames(data.filter((game) => game.gameId !== 1 && game.gameId !== 2))
      )
      .catch(console.log);
  }, []);

  const imageStyle = {
    width: "250px", // Fixed width for the image
    height: "auto", // Maintain aspect ratio
    float: "left", // Align image to the left
    marginRight: "20px", // Space between the image and the content
    marginTop: "50px", // Adjust the space at the top if needed
  };

  return (
    <div>
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
                <th>&nbsp;</th>
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
                    <td></td>
                  </tr>
                ))}
            </tbody>
          </table>
        </section>
      </div>
    </div>
  );
}

export default Home;
