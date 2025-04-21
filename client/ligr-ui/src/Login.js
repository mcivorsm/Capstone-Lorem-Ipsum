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
  username: "",
  password: ""
};

const Login = ({ setToken, setAuthUser }) => {
  const [user, setUser] = useState(USER_DEFAULT);
  const [errors, setErrors] = useState([]);
  const url = "http://LoremIpsumBackendServicesEC2-env.eba-9tm8q273.us-east-2.elasticbeanstalk.com/login/authenticate";
  const navigate = useNavigate();

  const handleSubmit = (event) => {
    event.preventDefault();
    signIn();
  }

  const handleChange = (event) => {
    const newUser = { ...user };
    newUser[event.target.name] = event.target.value;
    setUser(newUser);
  };

  const signIn = () => {
    const init = {
      method: 'POST',
      headers: {
          'Content-Type': 'application/json'
      },
      body: JSON.stringify(user)
    };
    fetch(url, init)
    .then(response => {
      
        if(response.status === 200){
          const testChceck = false;
            return response.json();
        } else {
            if (response.status === 403) { // incorrect credentials
              setErrors(["Username or password is incorrect."]);
            }
            return Promise.reject(`*Unexpected Status Code: ${response.status}`);
        }
    })
    .then(data => {
        if(data.jwt_token){ // Happy path
            localStorage.setItem("jwtToken", data.jwt_token); // save token
            setToken(data.jwt_token);
            const decodedToken = parseJwt(data.jwt_token);
            if (decodedToken) {
              const authUser = {
                id: decodedToken.userId,
                username: decodedToken.sub,
                email: decodedToken.email,
                isAdmin: decodedToken.isAdmin,
                roles: decodedToken.authorities?.split(",") || [],
              };
              setAuthUser(authUser);
            }
            // navigate to home page
            navigate("/");
        } else {
          setErrors(data);
        }
    })
    .catch(console.log);
  };

  return (
    <>
      <section>
        <h2 className="mb-4">Login</h2>
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
              Log In
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
        to={"/register"}
      >
        Register
      </Link>
    </>
  );
}

function parseJwt(token) {
  try {
    const base64Url = token.split(".")[1];
    const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split("")
        .map((c) => "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2))
        .join("")
    );
    return JSON.parse(jsonPayload);
  } catch (e) {
    return null;
  }
}

export default Login;
