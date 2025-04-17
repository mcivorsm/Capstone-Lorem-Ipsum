import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";

const USER_DEFAULT = {
  username: "Placeholder username",
  password: ""
};

function Profile() {
  console.log("asdadas");
  const [user, setUser] = useState(USER_DEFAULT);
  const { profileId } = useParams();

  useEffect(() => {

    const token = localStorage.getItem('jwtToken'); 

    const url = profileId ? `http://localhost:8080/profile/${profileId}` : `http://localhost:8080/profile`;

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
        })
        .catch(console.log);
    } 
  }, []);
  
  const setProfile = () => {};

  return (
    <div>
      <h2>{user.username}</h2>
      <p>This is your profile.</p>
    </div>
  );
}

export default Profile;