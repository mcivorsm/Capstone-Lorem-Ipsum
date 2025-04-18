import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";

function Settings({ authUser }) {
  const [user, setUser] = useState(null);
  const token = localStorage.getItem("jwtToken");
  const [errors, setErrors] = useState([]);
  const navigate = useNavigate();
  const { idFromURL } = useParams();

  const userId = (() => {
    if (idFromURL) {
      return idFromURL;
    }
    const decodedToken = JSON.parse(atob(token.split(".")[1]));
    const userId = decodedToken.userId;
    return userId;
  })();

  //USER RETRIEVAL FOR USERNAME
  useEffect(() => {
    // if on someone else's profile settings, and not an admin
    console.log(authUser.roles);
    if (idFromURL && !authUser?.roles.includes("ROLE_ADMIN") && authUser?.id != user?.id) {
      navigate("/");
    }
    if (token) {
      fetch(`http://localhost:8080/user/id/${userId}`, {
        headers: { Authorization: `Bearer ${token}` },
      })
        .then((response) => {
          if (response.status === 200) {
            return response.json();
          } else if (response.status === 401 || response.status === 403) {
            localStorage.removeItem("jwtToken");
            navigate("/login");
          } else {
            return Promise.reject(`Unexpected Status Code: ${response.status}`);
          }
        })
        .then((data) => setUser(data))
        .catch(console.error);
    }
  }, [token, userId]);

  const handleChange = (event) => {
    const { name, value } = event.target;
    setUser((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmitUser = () => {
    const init = {
      method: "PUT",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify(user),
    };
    fetch(`http://localhost:8080/user/edit`, init)
      .then((response) => {
        if (response.status === 204) {
          return null;
        } else if (response.status === 400) {
          return response.json();
        } else {
          return Promise.reject(`Unexpected status code: ${response.status}`);
        }
      })
      .then((data) => {
        if (!data) {
          setUser(user);
          window.alert("Account updated.");
          if (idFromURL) {
            navigate(`/profile/${idFromURL}`);
          } else {
            navigate("/profile");
          }
        } else {
          setErrors(data);
        }
      })
      .catch(console.log);
  };

  const handleDeleteUser = () => {
    const confirmed = window.confirm(
      "Are you sure you want to delete this account? This action cannot be undone."
    );
    if (!confirmed) return;

    if (token) {
      const url = idFromURL
        ? `http://localhost:8080/user/delete/${idFromURL}`
        : `http://localhost:8080/user/delete/`;
      fetch(url, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${token}`, // Attach token to request
        },
      })
        .then((response) => {
          if (response.status === 204) {
            return null;
          } else {
            return Promise.reject(`Unexpected Status Code: ${response.status}`);
          }
        })
        .then((data) => {
          if (!data) {
            window.alert("Account deleted.");

            if (idFromURL) {
              navigate("/userlist"); //admin
            } else {
              navigate("/");
            }
          } else {
            setErrors(data);
          }
        })
        .catch(console.log);
    }
  };

  return (
    <>
      <section
        className="container mt-4"
        style={{ border: "2px solid #0056b3", backgroundColor: "#e3f2fd" }}
      >
        {errors.length > 0 && (
          <div className="alert alert-danger d-flex flex-column align-items-center w-25 mx-auto">
            <p className="mb-3">The following errors were found:</p>
            <ul>
              {errors.map((error) => (
                <li key={error}>- {error}.</li>
              ))}
            </ul>
          </div>
        )}

        <h2 className="text-center mb-4 mt-4">Account Settings</h2>

        <div>
          <form className="w-50 mx-auto " style={{ marginTop: "2rem" }}>
            <div className="form-group row mb-3">
              <label
                htmlFor="username"
                className="col-sm-4 col-form-label text-info"
              >
                Update Username:
              </label>
              <div className="col-sm-8">
                <input
                  type="text"
                  className="form-control"
                  id="username"
                  name="username"
                  value={user?.username ?? ""}
                  onChange={handleChange}
                />
              </div>
            </div>

            <div className="form-group row mb-3">
              <label
                htmlFor="passwordHash"
                className="col-sm-4 col-form-label text-info"
              >
                Update Password:
              </label>
              <div className="col-sm-8">
                <input
                  type="text"
                  className="form-control"
                  id="passwordHash"
                  name="passwordHash"
                  onChange={handleChange}
                />
              </div>
            </div>

            <div className="form-group row mb-3">
              <label
                htmlFor="email"
                className="col-sm-4 col-form-label text-info"
              >
                Update Email:
              </label>
              <div className="col-sm-8">
                <input
                  type="text"
                  className="form-control"
                  id="email"
                  name="email"
                  value={user?.email ?? ""}
                  onChange={handleChange}
                />
              </div>
            </div>

            <div className="text-center mt-4">
              <button
                type="button"
                className="btn btn-outline-success"
                onClick={handleSubmitUser}
              >
                Save Changes
              </button>
            </div>
          </form>

          <div
            style={{ marginTop: "350px", marginBottom: "10px" }}
            className="text-center"
          >
            <button
              className="btn btn-outline-danger"
              onClick={handleDeleteUser}
            >
              Delete Account
            </button>
          </div>
          <div style={{ marginBottom: "10px" }}
          className="text-center">
            
            <button
              className="btn btn-outline-info"
              onClick={() => {
                if (idFromURL) {
                  navigate(`/profile/${idFromURL}`);
                } else {
                  navigate("/profile");
                }
              }}
            >
              Back
            </button>
          </div>
        </div>
      </section>
    </>
  );
}

export default Settings;
