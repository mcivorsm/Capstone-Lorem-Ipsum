import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

function UserList({ authUser }) {
  const [users, setUsers] = useState([]);
  const url = "http://LoremIpsumBackendServicesEC2-env.eba-9tm8q273.us-east-2.elasticbeanstalk.com/user";

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
        <thead style={{ backgroundColor: "#003366", color: "white" }}>
          <tr>
            <th>Username</th>
            <th>Profile</th>
            {authUser?.roles.includes("ROLE_ADMIN") && <th>Delete User</th>}
            {!authUser?.roles.includes("ROLE_ADMIN") && <th>&nbsp;</th>}
          </tr>
        </thead>
        <tbody>
          {users
            .filter((user) => user.id != 1)
            .map((user, index) => (
              <tr
                key={user.id}
                style={{
                  backgroundColor: index % 2 === 0 ? "#e3f2fd" : "white", // light blue for striped rows
                }}
              >
                <td
                  style={{
                    color: "#0056b3", 
                  }}
                >
                  {user.username}
                </td>
                <td>
                  <Link
                    className="btn btn-outline-info mr-4"
                    to={`/profile/${user.profile.profileId}`}
                  >
                    Profile
                  </Link>
                </td>

                <td>
                  {authUser?.roles.includes("ROLE_ADMIN") && (
                    <button
                      className="btn btn-outline-danger"
                      onClick={() => handleDeleteUser(user.id)}
                    >
                      Delete
                    </button>
                  )}
                </td>
              </tr>
            ))}
        </tbody>
      </table>
    </section>
  );
}

export default UserList;
