import { Link } from "react-router-dom"

function Navbar() {
  return (
    <nav>
      <Link to={"/"}>Home</Link>
      <Link to={"/profile"}>Profile</Link>
      <Link to={"/logout"}>Log Out</Link>
    </nav>
  );
}

export default Navbar;