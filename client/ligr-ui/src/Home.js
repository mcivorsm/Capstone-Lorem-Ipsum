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
              console.error("Failed to fetch rating for game", game.title, error);
              return { ...game, rating: "N/A" };
            }
          })
        );
      })
<<<<<<< HEAD
      .then((data) => setGames(data.filter((game) => game.gameId !== 1 && game.gameId !== 2)))
=======
      .then(
        (data) =>
          setGames(data.filter((game) => game.gameId != 1 && game.gameId != 2)) // take out deleted and default game
      )
>>>>>>> 28c273383cc1e1b85e2e440024c6d13cd89b8b90
      .catch(console.log);
  }, []);

  const imageStyle = {
    position: "fixed",
    top: 0,
    left: 0,
    height: "100vh",
    objectFit: "cover",
    zIndex: -1,
  };

  const contentStyle = {
    marginLeft: "300px", // Adjust based on your image width
    padding: "20px",
  };

  return (
    <div>
<<<<<<< HEAD
      <img
        src={MasterChiefImg}
        alt="Master Chief"
        style={imageStyle}
      />
      <div style={contentStyle}>
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
                    <td></td>
                  </tr>
                ))}
            </tbody>
          </table>
        </section>
      </div>
=======
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
                  <td>{game.title}</td>
                  <td>{game.developer}</td>
                  <td>{game.yearReleased}</td>
                  <td>{game.region}</td>
                  <td>{game.rating}</td>
                  <td>&nbsp;</td>
                </tr>
              ))}
          </tbody>
        </table>
      </section>
>>>>>>> 28c273383cc1e1b85e2e440024c6d13cd89b8b90
    </div>
  );
}

export default Home;
