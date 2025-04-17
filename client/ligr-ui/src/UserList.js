import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

function UserList({ authUser }) {
  const [users, setUsers] = useState([]);
  const url = "http://localhost:8080/user";

// fetch all users on component load
useEffect(() => {
  fetch(`${url}`)
    .then((response) => {
      if (response.status === 200) {
        return response.json();
      } else {
        return Promise.reject(`Unexpected Status Code: ${response.status}`);
      }
    })
    .then((data) => setUsers(data))
    .catch(console.log);
}, []);

// // game review retrieval
// useEffect(() => {
//   if (token) {
//     fetch(`http://localhost:8080/gameReview/user/${userId}`, {
//       headers: { Authorization: `Bearer ${token}` },
//     })
//       .then((response) => {
//         if (response.status === 200) {
//           return response.json();
//         } else {
//           return Promise.reject(`Unexpected Status Code: ${response.status}`);
//         }
//       })
//       .then((data) => setReviews(data))
//       .catch(console.error);
//   }
// }, []);

// {
//   "profile": {
//     "profileId": 4,
//     "favoriteGame": {
//       "gameId": 4,
//       "title": "Star Defender",
//       "developer": "NovaCore Studios",
//       "genre": "Shooter",
//       "yearReleased": 2022,
//       "platform": "Xbox",
//       "region": "EU"
//     },
//     "dateJoined": "2021-11-20",
//     "region": "EU",
//     "profileDescription": "Admin account for moderation purposes.",
//     "preferredGenre": "Strategy"
//   },
//   "username": "admin_one",
//   "email": "admin@example.com",
//   "passwordHash": "adminpass",
//   "password": "adminpass",
//   "id": 4,
//   "enabled": true,
//   "accountNonExpired": true,
//   "credentialsNonExpired": true,
//   "admin": true,
//   "authorities": [
//     {
//       "authority": "ROLE_ADMIN"
//     },
//     {
//       "authority": "ROLE_USER"
//     }
//   ],
//   "accountNonLocked": true
// }

  // handle deleting a user
  const handleDeleteUser = (userId) => {
    const token = localStorage.getItem("jwtToken");
    const user = users.find((user) => user.id === userId);
    if (window.confirm(`Delete Game: ${user.username}?`)) {
      const init = {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      };
      fetch(`${url}/delete/${userId}`, init)
        .then((response) => {
          if (response.status === 204) {
            // create a copy of the array
            // remove the user
            const newUsers = users.filter((user) => user.id !== userId);
            // update the users state
            setUsers(newUsers);
          } else {
            return Promise.reject(`Unexpected Status Code: ${response.status}`);
          }
        })
        .catch(console.log);
    }
  };

  return (
    <section>
      <h3 className="mb-4">All Users</h3>
      <table className="table table-striped table-hover">
        <thead className="thead-dark">
          <tr>
            <th>Username</th>
            <th>Profile</th>
            <th>&nbsp;</th>
          </tr>
        </thead>
        <tbody>
          {users.map((user) => (
            <tr key={user.id}>
              <td>{user.username}</td>
              <td>
                <Link
                  className="btn btn-outline-warning mr-4"
                  to={`/profile/${user.profile.profileId}`}
                >
                  Profile
                </Link>
                {(authUser?.roles.includes("ROLE_ADMIN")) && (
                <button
                  className="btn btn-outline-danger"
                  onClick={() => handleDeleteUser(user.id)}
                >
                  Delete
                </button>)}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </section>
  );
}

export default UserList;
