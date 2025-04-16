import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";

function Profile() {
  const [user, setUser] = useState();
  const [errors, setErrors] = useState([]);
  const url = "http://localhost:8080";
  const navigate = useNavigate();


  return (
    <div>
      <h2>Profile</h2>
      <p>This is your profile.</p>
    </div>
  );
}

export default Profile;