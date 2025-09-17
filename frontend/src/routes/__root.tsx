import { createRootRoute, Link, Outlet } from "@tanstack/react-router";
import { TanStackRouterDevtools } from "@tanstack/router-devtools";
import logo from "@/assets/image/manujallll.png";
import useAuthStore from "@/types/authStore";
import { login, logout } from "@/types/authService";

// 기존 Header 컴포넌트를 여기로 통합
const Header = () => {
  const { isLoggedIn, user } = useAuthStore();

  const handleLogout = () => {
    logout();
  };

  return (
    <header className="flex items-center justify-between bg-gradient-to-r from-blue-400 via-pink-300 to-yellow-200 px-8 py-2 shadow-md">
      <nav>
        <ul className="flex space-x-8">
          <li>
            <Link
              to="/board"
              className="text-lg font-semibold text-gray-700 transition hover:text-blue-600"
            >
              게시판
            </Link>
          </li>
          <li>
            <Link
              to="/about"
              className="text-lg font-semibold text-gray-700 transition hover:text-pink-600"
            >
              소개
            </Link>
          </li>
        </ul>
      </nav>
      <Link to="/" className="flex flex-1 justify-center">
        <img src={logo} alt="로고" className="h-12 w-12" />
      </Link>
      <div className="flex items-center">
        {isLoggedIn ? (
          <button
            className="ml-4 rounded-lg bg-white px-5 py-2 font-bold text-blue-600 shadow-sm transition hover:bg-blue-100 hover:text-blue-800"
            onClick={handleLogout}
          >
            로그아웃
          </button>
        ) : (
          <Link to="/auth/login">
            <button className="ml-4 rounded-lg bg-white px-5 py-2 font-bold text-blue-600 shadow-sm transition hover:bg-blue-100 hover:text-blue-800">
              로그인
            </button>
          </Link>
        )}
      </div>
    </header>
  );
};

export const Route = createRootRoute({
  component: () => (
    <div>
      <Header />
      <main>
        <Outlet />
      </main>
      {/* 개발 모드에서만 DevTools 표시 */}
      <TanStackRouterDevtools />
    </div>
  ),
});
