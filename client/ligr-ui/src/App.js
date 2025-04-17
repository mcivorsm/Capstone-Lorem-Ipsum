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
import SelfProfile from "./SelfProfile";
import OtherProfile from "./OtherProfile";

function App() {
  const [token, setToken] = useState(() => localStorage.getItem("jwtToken"))

  // a user variable taken from decoded jwtToken, used for checking auth roles
  const [authUser, setAuthUser] = useState(() => {
    const t = localStorage.getItem("jwtToken");
    if (!t) return null;
    try {
      const decodedToken = JSON.parse(atob(token.split('.')[1])); 
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
      <Router>
      { token ? (<Navbar setToken={setToken} user={authUser}/>) : (<RegisterLoginBar/>)}
        <Routes>
          <Route path="/" element={<Home/>}/>

          <Route path="/login" element={<Login setToken={setToken} setAuthUser={setAuthUser}/>}/>

          <Route path="/profile" element={
            <ProtectedRoute token={token} user={authUser}>
              <SelfProfile/>
            </ProtectedRoute>
          } />

          <Route path="/profile/:profileId" element={<ProtectedRoute token={token}> <OtherProfile/> </ProtectedRoute>}/>
          <Route path="/register" element={<Register/>}/>

          <Route path="/game/:gameId" element={
            <ProtectedRoute token={token} user={authUser}>
              <Game/>
            </ProtectedRoute>
          } />

          <Route path="/game/add" element={
            <ProtectedRoute token={token} user={authUser} requiredRole="ROLE_ADMIN">
              <GameForm/>
            </ProtectedRoute>
          } />

          <Route path="/game/edit/:id" element={
            <ProtectedRoute token={token} user={authUser} requiredRole="ROLE_ADMIN">
              <GameForm/>
            </ProtectedRoute>
          } />

          <Route path="/admin" element={
            <ProtectedRoute token={token} user={authUser} requiredRole="ROLE_ADMIN">
              <AdminPage/>
            </ProtectedRoute>
          } />

          <Route path="*" element={<NotFound/>}/>
        </Routes>
      </Router>
    </div>
  );
}

export default App;
