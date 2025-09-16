import { Link } from "react-router";
import logo from "@/assets/image/manujallll.png";

const Header = () => (
  <header className="flex items-center justify-between rounded-b-2xl bg-white px-8 py-4 shadow-md">
    <Link to="/" className="flex items-center gap-3">
      <img
        src={logo}
        alt="로고"
        className="h-12 w-12 shadow-sm transition hover:scale-105"
      />
      <span className="select-none text-2xl font-extrabold tracking-tight text-blue-600">
        멸치액젓 커뮤니티
      </span>
    </Link>
    <nav>
      <ul className="flex space-x-4">
        <li>
          <Link
            to="/board"
            className="rounded-lg bg-blue-50 px-5 py-2 font-bold text-blue-600 shadow-sm transition hover:bg-blue-100 hover:text-blue-800"
          >
            게시판
          </Link>
        </li>
        <li>
          <Link
            to="/about"
            className="rounded-lg bg-pink-50 px-5 py-2 font-bold text-pink-600 shadow-sm transition hover:bg-pink-100 hover:text-pink-800"
          >
            소개
          </Link>
        </li>
      </ul>
    </nav>
  </header>
);

export default Header;
