import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";

const USER_DEFAULT = {
  username: "Placeholder username",
  password: ""
};

function SelfProfile() {
  const [user, setUser] = useState(USER_DEFAULT);
  const [profile, setProfile] = useState();

  useEffect(() => {

    const token = localStorage.getItem('jwtToken');
    const url = `http://localhost:8080/profile/`;

    console.log("token = " + token);
    if (token) {
      console.log("HITTT**");
      fetch(url, {
        method: 'GET',
        headers: {
          Authorization: `Bearer ${token}` // Attach token to request
        }
      })
        .then((response) => {
          
          if (response.status === 200) {
            console.log("Getting response");
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


  return (
    <div>
      <h2>{user.username}</h2>
      <h3>{profile.dateJoined}</h3>
      <p>This is your profile.</p>
    </div>
  );
}

export default SelfProfile;