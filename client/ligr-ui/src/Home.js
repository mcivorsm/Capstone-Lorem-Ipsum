import { useEffect, useState } from "react";

function Home() {
  const [games, setGames] = useState([]);
  const url = "http://localhost:8080/game";

  // useEffect fetches data when component mounts
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

  return (
    <div>
      <h2>HomePage for Lorem Ipsum Game Reviews</h2>
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
          {games.map((game) => (
              <tr key={game.gameId}>
                <td>{game.title}</td>
                <td>{game.developer}</td>
                <td>{game.yearReleased}</td>
                <td>{game.region}</td>
             </tr>
            ))}
        </tbody>
      </table>
    </section>
    </div>
  );
}

export default Home;