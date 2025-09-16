import logo from "@/assets/image/mujigaemanull.png";
import "@/assets/css/App.css";
import { useNavigate, useParams } from "@tanstack/react-router";

const Home = () => {
  const navigate = useNavigate();
  return (
    <div className="App">
      <header className="App-header">
        <p className="cookieRunFont">
          안넝하-십미가-쿠키런-애-오신것을--아래-무곰을-누르면-어봐웃으로-이동
        </p>
        <img
          src={logo}
          className="App-logo"
          alt="logo"
          onClick={() => {
            navigate({ to: "/about" });
          }}
        />
        <p
          onClick={() => {
            navigate({ to: "/board" });
          }}
        >
          게시판 보러가기
        </p>
      </header>
    </div>
  );
};

export default Home;
