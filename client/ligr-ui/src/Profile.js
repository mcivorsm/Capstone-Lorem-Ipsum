import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

const USER_DEFAULT = {
  username: "Placeholder username",
  password: ""
};

function Profile() {
  const [user, setUser] = useState(USER_DEFAULT);
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