import React from "react";
import { BrowserRouter, Route, Routes } from "react-router";
import Home from "@/pages/Home";
import About from "@/pages/About";
import Board from "@/pages/Board";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/home" element={<Home />} />
        <Route path="/about" element={<About />} />
        <Route path="/board" element={<Board />} />
        {/* Add more routes as needed */}
      </Routes>
    </BrowserRouter>
  );
}

export default App;
