import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

const USER_DEFAULT = {
  username: "Placeholder username",
  password: ""
};

function Profile() {
  const [user, setUser] = useState(USER_DEFAULT);
  const url = "http://localhost:8080/user";

  useEffect(() => {
    const token = localStorage.getItem('jwtToken'); // Retrieve token from localStorage
    if (token) {
      fetch(url, {
        method: 'GET',
        headers: {
          Authorization: `Bearer ${token}` // Attach token to request
        }
      })
        .then((response) => {
          if (!response.ok) throw new Error("Failed to load profile");
          return response.json();
        })
        .then((data) => {
          setProfile(data);
          setLoading(false);
        })
        .catch((err) => {
          console.error(err);
          setLoading(false);
        });
    } else {
      setLoading(false); // Handle case where there's no token
    }
  }, [url]);
  
  const setProfile = () => {};

  const setLoading = () => {};

  return (
    <div>
      <h2>{user.username}</h2>
      <p>This is your profile.</p>
    </div>
  );
}

export default Profile;