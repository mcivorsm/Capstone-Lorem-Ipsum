import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";

// example token after decoding: {
//   "sub": "johndoe",   // Subject: the username
//   "authorities": "ADMIN,USER",  // Roles or authorities of the user
//   "userId": 12345,     // Unique ID of the user
//   "profileId": 67890,  // ID of the user's profile
//   "email": "johndoe@example.com" // Email of the user
// }

const USER_DEFAULT = {
  email: "",
  username: "",
  password: ""
};

function Register() {
  const [user, setUser] = useState(USER_DEFAULT);
  const [errors, setErrors] = useState([]);
  const url = "http://localhost:8080/api/register";
  const navigate = useNavigate();

  const handleSubmit = (event) => {
    event.preventDefault();
    signUp();
  }

  const handleChange = (event) => {
    const newUser = { ...user };
    newUser[event.target.name] = event.target.value;
    setUser(newUser);
  };

  const signUp = () => {
    const init = {
      method: 'POST',
      headers: {
          'Content-Type': 'application/json'
      },
      body: JSON.stringify(user)
    };
    fetch(url, init)
    .then(response => {
        if(response.status === 201 || response.status === 400){
            return response.json();
        } else {
            return Promise.reject(`Unexpected Status Code: ${response.status}`);
        }
    })
    .then(data => {
        if(data && data.jwtToken){ // Happy path
            localStorage.setItem("token", data.jwtToken); // save token
            // navigate to home page
            navigate("/");
        } else { // Unhappy path
            setErrors(data)
        }
    })
    .catch(console.log);
  };

  return (
    <>
      <section>
        <h2 className="mb-4">Register</h2>
        {errors.length > 0 && (
          <div className="alert alert-danger">
            <p>The Following Errors were Found:</p>
            <ul>
              {errors.map((error) => (
                <li key={error}>{error}</li>
              ))}
            </ul>
          </div>
        )}
        <form onSubmit={handleSubmit}>
        <fieldset className="form-group">
            <label htmlFor="email">Email</label>
            <input
              id="email"
              name="email"
              type="text"
              className="form-control"
              value={user.email}
              onChange={handleChange}
            ></input>
          </fieldset>
          <fieldset className="form-group">
            <label htmlFor="username">Username</label>
            <input
              id="username"
              name="username"
              type="text"
              className="form-control"
              value={user.username}
              onChange={handleChange}
            ></input>
          </fieldset>
          <fieldset className="form-group">
            <label htmlFor="password">Password</label>
            <input
              id="password"
              type="password"
              name="password"
              className="form-control"
              value={user.password}
              onChange={handleChange}
            ></input>
          </fieldset>
          <fieldset className="form-group">
            <button type="submit" className="btn btn-outline-success mr-4 mt-4">
              Register
            </button>
            <Link
              type="button"
              className="btn btn-outline-danger mt-4"
              to={"/"}
            >
              Cancel
            </Link>
          </fieldset>
        </form>
      </section>
      <Link
        type="button"
        className="btn btn-outline-warning mt-4"
        to={"/login"}
      >
        Login
      </Link>
    </>
  );
}

export default Register;
