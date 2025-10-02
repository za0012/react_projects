import { useState } from "react";
import useAuthStore from "@/types/authStore";
import { logout } from "@/types/authService";
import logo from "@/assets/image/manujallll.png";
import menu1 from "@/assets/image/cookie.svg";
import menu2 from "@/assets/image/gem.svg";
import menu3 from "@/assets/image/paw-print.svg";
import menu4 from "@/assets/image/megaphone.svg";
import menu5 from "@/assets/image/clipboard-list.svg";
import menu6 from "@/assets/image/message-circle-question-mark.svg";
import menu7 from "@/assets/image/utensils.svg";
import menu8 from "@/assets/image/user.svg";
import { Link } from "@tanstack/react-router";

const Sidebar = () => {
  const [open, setOpen] = useState(false);
  const { isLoggedIn, user } = useAuthStore();
  const Menus = [
    { title: "쿠키 관리", src: menu1, link: "/management/cookieMan" },
    { title: "보물 관리", src: menu2, link: "/management/treasure" },
    { title: "펫 관리", src: menu3, gap: true, link: "/management/petMan" },
    { title: "배너 관리", src: menu4, link: "/management/banner" },
    { title: "게시판 관리", src: menu5, link: "" },
    { title: "1:1 문의들", src: menu6, link: "/management/inquiry" },
    { title: "배고프다", src: menu7, gap: true, link: "" }, //gap으로 속성 그룹화
    { title: "나도", src: menu8, link: "" },
  ];

  const handleLogout = () => {
    logout();
    window.location.href = "/";
  };

  return (
    <div className="flex">
      <div
        className={`${open ? "w-52" : "w-20"} sticky top-0 flex h-screen flex-col rounded-r-3xl bg-blue-500 p-5 pt-8 shadow-2xl duration-300`}
      >
        <img
          src={logo}
          className={`border-dark-purple absolute -right-3 top-9 w-7 cursor-pointer rounded-full border-2 ${!open && "rotate-180"}`}
          onClick={() => setOpen(!open)}
        />
        <Link to="/">
          <div className="flex items-center gap-x-4">
            <img
              src={logo}
              className={`w-10 cursor-pointer duration-500 ${open && "rotate-[360deg]"}`}
            />
            <h1
              className={`origin-left text-xl font-medium text-white duration-200 ${!open && "scale-0"}`}
            >
              admin
            </h1>
          </div>
        </Link>
        <ul className="flex-grow overflow-y-auto pt-6">
          {Menus.map((Menu, index) => (
            <Link to={Menu.link} key={index}>
              <li
                key={index}
                className={`hover:bg-light-white flex cursor-pointer items-center gap-x-4 rounded-md p-2 text-sm text-white ${Menu.gap ? "mt-9" : "mt-2"} ${index === 0 && "bg-light-white"}`}
              >
                <img src={Menu.src} />
                <span
                  className={`origin-left whitespace-nowrap duration-200 ${!open ? "scale-x-0 opacity-0" : "scale-x-100 opacity-100"} `}
                >
                  {Menu.title}
                </span>
              </li>
            </Link>
          ))}
        </ul>
        {isLoggedIn && (
          <div
            className={`border-t border-white pt-6 transition duration-300 ${!open && "hidden"} mt-auto`}
          >
            <span className="mb-3 block text-sm font-medium text-white">
              안녕하세요,{" "}
              <span className="font-bold text-blue-600">{user?.username}</span>
              님!
            </span>
            <button
              className="w-full rounded-full bg-red-500 py-2 text-sm font-semibold text-white shadow-md transition hover:bg-red-600"
              onClick={handleLogout}
            >
              로그아웃
            </button>
          </div>
        )}
      </div>
    </div>
  );
};

export default Sidebar;
