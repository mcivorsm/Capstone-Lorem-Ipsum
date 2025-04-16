import { Link } from "react-router-dom";

function RegisterLoginBar() {
  return (
    <nav style={{ position: "relative" }}>
      <Link to={"/login"}>Login</Link>
      <Link to={"/register"}>Register</Link>
    </nav>
  );
}

export default RegisterLoginBar;