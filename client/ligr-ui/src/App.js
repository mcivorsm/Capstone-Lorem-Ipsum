import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { useEffect, useState } from "react";
import Home from "./Home";
import NotFound from "./NotFound";
import Navbar from "./Navbar";
import Login from "./Login";
import Register from "./Register";
import Profile from "./Profile";
import Game from "./Game";
import RegisterLoginBar from "./RegisterLoginBar";

function App() {
  const [token, setToken] = useState(() => localStorage.getItem("jwtToken"))
  
  return (
    <div className="App">
      <Router>
      { token ? (<Navbar setToken={setToken}/>) : (<RegisterLoginBar/>)}
        <Routes>
          <Route path="/" element={<Home/>}/>
          <Route path="/login" element={<Login setToken={setToken}/>}/>
          <Route path="/profile" element={<Profile/>}/>
          <Route path="/register" element={<Register/>}/>
          <Route path="/game/:gameId" element={<Game />} />
          <Route path="*" element={<NotFound/>}/>
        </Routes>
      </Router>
    </div>
  );
}

export default App;
