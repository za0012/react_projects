import React from "react";
import logo from "@/assets/image/mujigaemanull.png";
import "@/assets/css/App.css";
import { Link } from "react-router";

const Home = () => {
  return (
    <div className="App">
      <header className="App-header">
        <p className="cookieRunFont">
          안넝하-십미가-쿠키런-애-오신것을--아래-무곰을-누르면-어봐웃으로-이동
        </p>
        <Link to={"/about"} className="App-link">
          <img src={logo} className="App-logo" alt="logo" />
        </Link>
        <Link to={"/board"} className="App-link">
          <p>게시판 보러가기</p>
        </Link>
      </header>
    </div>
  );
};

export default Home;
