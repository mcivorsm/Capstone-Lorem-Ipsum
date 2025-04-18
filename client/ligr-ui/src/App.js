import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { useState } from "react";
import Home from "./Home";
import NotFound from "./NotFound";
import Navbar from "./Navbar";
import Login from "./Login";
import Register from "./Register";

import Game from "./Game";
import RegisterLoginBar from "./RegisterLoginBar";
import ProtectedRoute from "./ProtectedRoute";
import AdminPage from "./AdminPage";
import GameForm from "./GameForm";
import Profile from "./Profile";
import UserList from "./UserList";
import Settings from "./Settings";
import MasterChiefImg from "./Master_Chief_in_Halo_5.webp";

function App() {
  const [token, setToken] = useState(() => localStorage.getItem("jwtToken"));

  // a user variable taken from decoded jwtToken, used for checking auth roles
  const [authUser, setAuthUser] = useState(() => {
    const t = localStorage.getItem("jwtToken");
    if (!t) return null;
    try {
      const decodedToken = parseJwt(t);
      if (!decodedToken) return null;
      return {
        id: decodedToken.userId,
        username: decodedToken.sub,
        email: decodedToken.email,
        isAdmin: decodedToken.isAdmin,
        roles: decodedToken.authorities?.split(",") || [],
      };
    } catch {
      return null;
    }
  });

  return (
    <div className="App">
      <img
        src={MasterChiefImg}
        alt="Master Chief"
        style={{
          position: "fixed",
          top: 0,
          left: 0,
          height: "100vh",
          objectFit: "cover",
          zIndex: -1,
        }}
      />
      <Router>
    {token ? (
      <Navbar setToken={setToken} authUser={authUser} />
    ) : (
      <RegisterLoginBar />
    )}
      <div
        style={{
          backgroundColor: "rgba(255, 255, 255, 0.95)",
          margin: "2rem auto",
          padding: "2rem",
          borderRadius: "10px",
          maxWidth: "1200px",
          minHeight: "90vh",
          boxShadow: "0 0 15px rgba(0,0,0,0.2)",
        }}
      >
          <Routes>
            <Route path="/" element={<Home />} />

            <Route
              path="/login"
              element={<Login setToken={setToken} setAuthUser={setAuthUser} />}
            />

            <Route
              path="/profile"
              element={
                <ProtectedRoute token={token} user={authUser}>
                  <Profile authUser={authUser} />
                </ProtectedRoute>
              }
            />

            <Route
              path="/profile/:profileId"
              element={
                <ProtectedRoute token={token}>
                  <Profile authUser={authUser} />
                </ProtectedRoute>
              }
            />

            <Route path="/register" element={<Register />} />

            <Route
              path="/game/:gameId"
              element={
                <ProtectedRoute token={token}>
                  <Game authUser={authUser} />
                </ProtectedRoute>
              }
            />

            <Route
              path="/game/add"
              element={
                <ProtectedRoute
                  token={token}
                  user={authUser}
                  requiredRole="ROLE_ADMIN"
                >
                  <GameForm />
                </ProtectedRoute>
              }
            />

            <Route
              path="/game/edit/:id"
              element={
                <ProtectedRoute
                  token={token}
                  user={authUser}
                  requiredRole="ROLE_ADMIN"
                >
                  <GameForm />
                </ProtectedRoute>
              }
            />

            <Route
              path="/admin"
              element={
                <ProtectedRoute
                  token={token}
                  user={authUser}
                  requiredRole="ROLE_ADMIN"
                >
                  <AdminPage />
                </ProtectedRoute>
              }
            />

            <Route
              path="/userlist"
              element={
                <ProtectedRoute token={token}>
                  <UserList authUser={authUser} />
                </ProtectedRoute>
              }
            />

            <Route
              path="/userlist"
              element={<UserList authUser={authUser} />}
            />

            <Route
              path="/settings"
              element={
                <ProtectedRoute token={token}>
                  <Settings authUser={authUser} />
                </ProtectedRoute>
              }
            />

            <Route
              path="/settings/:idFromURL"
              element={
                <ProtectedRoute token={token}>
                  <Settings authUser={authUser} />
                </ProtectedRoute>
              }
            />

            <Route path="*" element={<NotFound />} />
          </Routes>
      </div>
      </Router>
    </div>
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

export default App;
