import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

const Profile = ({ user }) => {
  const [errors, setErrors] = useState([]);
  const url = "http://localhost:8080/user";
  const navigate = useNavigate();

  return (
    <div>
      <h2>{user.username}</h2>
      <p>This is your profile.</p>
    </div>
  );
}

export default Profile;