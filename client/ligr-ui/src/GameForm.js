// Admin-only form for creating and updating games
import { useState, useEffect } from "react";
import { useNavigate, useParams, Link } from "react-router-dom";

const GAME_DEFAULT = {
  title: "",
  developer: "",
  genre: "",
  yearReleased: 0,
  platform: "",
  region: "OTHER",
};

function GameForm() {
  const [game, setGame] = useState(GAME_DEFAULT);
  const [errors, setErrors] = useState([]);
  const url = "http://localhost:8080/game";
  const navigate = useNavigate();
  const { id } = useParams();

  // useEffect
  useEffect(() => {
    if (id) {
      fetch(`${url}/${id}`)
        .then((response) => {
          if (response.status === 200) {
            return response.json();
          } else {
            return Promise.reject(`Unexpected StatusCode: ${response.status}`);
          }
        })
        .then((data) => {
          // Set form state with the existing game data
          setGame(data);
        })
        .catch(console.log);
    } else {
      // No id means we're adding a new game
      setGame(GAME_DEFAULT);
    }
  }, [id]);

  // handleChange
  const handleChange = (event) => {
    const newGame = { ...game };
    newGame[event.target.name] = event.target.value;
    setGame(newGame);
  };

  // handleSubmit
  const handleSubmit = (event) => {
    event.preventDefault();

    if (id) {
      updateGame();
    } else {
      addGame();
    }
  };

  // CRUD
  // addGame
  const addGame = () => {
    const token = localStorage.getItem("jwtToken");
    const init = {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(game),
    };
    fetch(url, init)
      .then((response) => {
        if (response.status === 201 || response.status === 400) {
          return response.json();
        } else {
          return Promise.reject(`Unexpected Status Code: ${response.status}`);
        }
      })
      .then((data) => {
        if (data.gameId) {
          // Happy path
          // navigate to home page
          navigate("/admin");
        } else {
          // Unhappy path
          setErrors(data);
        }
      })
      .catch(console.log);
  };

  // updateGame
  const updateGame = () => {
    const token = localStorage.getItem("jwtToken");
    // Assign gameId
    game.gameId = id;
    const init = {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(game),
    };
    fetch(`${url}/${id}`, init)
      .then((response) => {
        if (response.status === 204) {
          return null;
        } else if (response.status === 400) {
          return response.json();
        } else {
          return Promise.reject(`Unexpected status code: ${response.status}`);
        }
      })
      .then((data) => {
        if (!data) {
          // happy
          // navigate to home page
          navigate("/admin");
        } else {
          // Unhappy
          // Get our error messages and display them
          setErrors(data);
        }
      })
      .catch(console.log);
  };

  return (
    <>
      <section>
        <h2 className="mb-4">{id > 0 ? "Update Game" : "Add Game"}</h2>
        {errors.length > 0 && (
          <div className="alert alert-danger">
            <p>The Following Errors were Found:</p>
            <ul>
              {errors.map((error, index) => (
                <li key={index}>{error.defaultMessage}</li>
              ))}
            </ul>
          </div>
        )}
        <form onSubmit={handleSubmit}>
          <fieldset className="form-group">
            <label htmlFor="title">Title</label>
            <input
              id="title"
              name="title"
              type="text"
              className="form-control"
              value={game.title}
              onChange={handleChange}
            ></input>
          </fieldset>
          <fieldset className="form-group">
            <label htmlFor="developer">Developer</label>
            <input
              id="developer"
              type="text"
              name="developer"
              className="form-control"
              value={game.developer}
              onChange={handleChange}
            ></input>
          </fieldset>
          <fieldset className="form-group">
            <label htmlFor="genre">Genre</label>
            <input
              id="genre"
              name="genre"
              type="text"
              className="form-control"
              value={game.genre}
              onChange={handleChange}
            ></input>
          </fieldset>
          <fieldset className="form-group">
            <label htmlFor="yearReleased">Year Released</label>
            <input
              id="yearReleased"
              type="number"
              name="yearReleased"
              className="form-control"
              value={game.yearReleased}
              onChange={handleChange}
            ></input>
          </fieldset>
          <fieldset className="form-group">
            <label htmlFor="platform">Platform</label>
            <input
              id="platform"
              name="platform"
              type="text"
              className="form-control"
              value={game.platform}
              onChange={handleChange}
            ></input>
          </fieldset>
          <fieldset className="form-group">
            <label htmlFor="region">Region</label>
            <select
              id="region"
              name="region"
              className="form-control"
              value={game.region}
              onChange={handleChange}
            >
              <option>EU</option>
              <option>NA</option>
              <option>JP</option>
              <option>OTHER</option>
            </select>
          </fieldset>
          <fieldset className="form-group">
            <button type="submit" className="btn btn-outline-success mr-4 mt-4">
              {id > 0 ? "Update Game" : "Add Game"}
            </button>
            <Link
              type="button"
              className="btn btn-outline-danger mt-4"
              to={"/admin"}
            >
              Cancel
            </Link>
          </fieldset>
        </form>
      </section>
    </>
  );
}

function parseJwt(token) {
  try {
    const base64Url = token.split(".")[1];
    const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split("")
        .map((c) => "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2))
        .join("")
    );
    return JSON.parse(jsonPayload);
  } catch (e) {
    return null;
  }
}

export default GameForm;
