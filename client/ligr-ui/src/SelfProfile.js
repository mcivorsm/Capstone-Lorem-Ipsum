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

  const token = localStorage.getItem("jwtToken");
  const decoded = parseJwt(token);
  const usernameForProfile = decoded?.username;
  return (
    <div>
      <h2>{usernameForProfile}</h2>
      <h3>{profile.dateJoined}</h3>
      <p>This is your profile.</p>
    </div>
  );
}
function parseJwt(token) {
  try {
    const base64Url = token.split('.')[1]; // Get payload
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
      return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));
    return JSON.parse(jsonPayload);
  } catch (e) {
    return null;
  }
}
export default SelfProfile;