import { Link } from "react-router-dom";
import { useState } from "react";

function Navbar() {
  const [searchTerm, setSearchTerm] = useState("");

  const handleSearch = (e) => {
    e.preventDefault();
    console.log("Searching for game:", searchTerm);
    // You can redirect or trigger a search function here
  };

  return (
    <nav style={{ padding: "1rem" }}>
      <div style={{ marginBottom: "1rem" }}>
        <Link to={"/"} style={{ marginRight: "1rem" }}>Home</Link>
        <Link to={"/login"} style={{ marginRight: "1rem" }}>Login</Link>
        <Link to={"/register"}>Register</Link>
      </div>
      
      <form onSubmit={handleSearch}>
        <label htmlFor="gameSearch">Find a Game: </label>
        <input
          id="gameSearch"
          type="text"
          placeholder="Search..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          style={{ marginRight: "0.5rem" }}
        />
        <button type="submit">Search</button>
      </form>
    </nav>
  );
}

export default Navbar;