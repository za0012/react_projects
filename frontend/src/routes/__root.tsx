import { createRootRoute, Link, Outlet } from "@tanstack/react-router";
import { TanStackRouterDevtools } from "@tanstack/router-devtools";
import logo from "@/assets/image/manujallll.png";
import useAuthStore from "@/types/authStore";
import { logout } from "@/types/authService";

// 기존 Header 컴포넌트를 여기로 통합
const Header = () => {
  const { isLoggedIn, user } = useAuthStore();

  const handleLogout = () => {
    logout();
  };

  return (
    <header className="flex items-center justify-between bg-white px-6 py-4 shadow-sm">
      {/* 로고 */}
      <Link to="/" className="flex items-center">
        <img src={logo} alt="로고" className="h-8 w-8" />
        <h1 className="ml-2 text-xl font-bold text-gray-800">쿠키런 가이드</h1>
      </Link>

      {/* 내비게이션 메뉴 */}
      <nav className="hidden md:block">
        <ul className="flex space-x-8">
          <li>
            <Link
              to="/board"
              className="text-lg font-semibold text-gray-500 transition hover:text-blue-500"
            >
              게시판
            </Link>
          </li>
          <li>
            <Link
              to="/about"
              className="text-lg font-semibold text-gray-500 transition hover:text-blue-500"
            >
              소개
            </Link>
          </li>
          <li>
            <Link
              to="/cookie"
              className="text-lg font-semibold text-gray-500 transition hover:text-blue-500"
            >
              쿠키들
            </Link>
          </li>
          <li>
            <Link
              to="/newUserGuide/list"
              className="text-lg font-semibold text-gray-500 transition hover:text-blue-500"
            >
              뉴비 공략집
            </Link>
          </li>
          {user?.admin ? (
            <li>
              <Link
                to="/management"
                className="text-lg font-semibold text-gray-500 transition hover:text-blue-500"
              >
                사이트 관리
              </Link>
            </li>
          ) : null}
        </ul>
      </nav>

      {/* 로그인/로그아웃 버튼 */}
      <div className="flex items-center space-x-2">
        {isLoggedIn ? (
          <>
            <span className="text-sm font-medium text-gray-600">
              안녕하세요,{" "}
              <span className="font-bold text-blue-600">{user?.username}</span>
              님!
            </span>
            <button
              className="rounded-full bg-blue-500 px-4 py-2 text-sm font-semibold text-white shadow-md transition hover:bg-blue-600"
              onClick={handleLogout}
            >
              로그아웃
            </button>
          </>
        ) : (
          <Link to="/auth/login">
            <button className="rounded-full bg-blue-500 px-4 py-2 text-sm font-semibold text-white shadow-md transition hover:bg-blue-600">
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
