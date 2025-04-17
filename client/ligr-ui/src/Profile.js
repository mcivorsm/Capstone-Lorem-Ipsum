import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";

function Profile() {
  const [profile, setProfile] = useState(null);
  const [userName, setUsername] = useState(null);

  const { profileId } = useParams();
  const token = localStorage.getItem('jwtToken'); 

  //PROFILE RETRIEVAL
  useEffect(() => {

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

  const userId = (() => {
    console.log(profileId + " was passed in");
    if (profileId) {
      return profileId;
    }
    const decodedToken = JSON.parse(atob(token.split('.')[1])); 
    const userId = decodedToken.userId; 
    return userId;
  })();

  console.log(userId);

  useEffect(() => {
    if (token) {
      fetch(`http://localhost:8080/user/id/${userId}`, {
        headers: { Authorization: `Bearer ${token}` }
      })
        .then(res => {
          if (!res.ok) throw new Error('Failed to fetch user');
          return res.json();
        })
        .then(data => setUsername(data.username))
        .catch(console.error);
    }
  }, [token, userId]);


  return (
    <div>
      <h2>{userName}</h2>
      <p>This is your profile.</p>
    </div>
  );
}

export default Profile;