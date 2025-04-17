import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { useState } from "react";
import Home from "./Home";
import NotFound from "./NotFound";
import Navbar from "./Navbar";
import Login from "./Login";
import Register from "./Register";
import Profile from "./Profile";
import Game from "./Game";
import RegisterLoginBar from "./RegisterLoginBar";
import ProtectedRoute from "./ProtectedRoute";

function App() {
  const [token, setToken] = useState(() => localStorage.getItem("jwtToken"))
  
  return (
    <div className="App">
      <Router>
      { token ? (<Navbar setToken={setToken}/>) : (<RegisterLoginBar/>)}
        <Routes>
          <Route path="/" element={<Home/>}/>
          <Route path="/login" element={<Login setToken={setToken}/>}/>
          <Route path="/profile" element={<ProtectedRoute token={token}> <Profile/> </ProtectedRoute>}/>
          <Route path="/profile/:profileId" element={<ProtectedRoute token={token}> <Profile/> </ProtectedRoute>}/>
          <Route path="/register" element={<Register/>}/>
          <Route path="/game/:gameId" element={<ProtectedRoute token={token}> <Game/> </ProtectedRoute>} />
          <Route path="*" element={<NotFound/>}/>
        </Routes>
      </Router>
    </div>
  );
}

export default App;
