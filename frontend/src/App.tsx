import React from "react";
import { BrowserRouter, Route, Routes } from "react-router";
import Home from "@/pages/Home";
import About from "@/pages/About";
import Board from "@/pages/board/Board";
import BoardDetail from "./pages/board/BoardDetail";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/home" element={<Home />} />
        <Route path="/about" element={<About />} />
        <Route path="/board" element={<Board />} />
        <Route path="/board/:articleId" element={<BoardDetail />} />
        {/* Add more routes as needed */}
      </Routes>
    </BrowserRouter>
  );
}

export default App;
