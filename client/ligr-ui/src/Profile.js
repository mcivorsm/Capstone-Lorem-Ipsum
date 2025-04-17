import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

const USER_DEFAULT = {
  username: "Placeholder username",
  password: ""
};

function Profile() {
  console.log("asdadas");
  const [user, setUser] = useState(USER_DEFAULT);

  useEffect(() => {

    const token = localStorage.getItem('jwtToken'); 
    let userId = null;
    if (token) {
      const decodedToken = JSON.parse(atob(token.split('.')[1])); 
      userId = decodedToken.userId; 
    }
    const url = userId ? `http://localhost:8080/profile/${userId}` : `http://localhost:8080/profile`;

    if (token) {
      fetch(url, {
        method: 'GET',
        headers: {
          Authorization: `Bearer ${token}` // Attach token to request
        }
      })
        .then((response) => {
          if (response.status === 200) {
            return response.json();
          } else {
            return Promise.reject(`Unexpected Status Code: ${response.status}`);
          }
        })
        .then((data) => {
          setProfile(data);
          setLoading(false);
        })
        .catch(console.log);
    } 
  }, []);
  
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