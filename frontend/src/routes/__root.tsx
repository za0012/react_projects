import { createRootRoute, Link, Outlet } from "@tanstack/react-router";
import { TanStackRouterDevtools } from "@tanstack/router-devtools";

// 기존 Header 컴포넌트를 여기로 통합
const Header = () => (
  <header className="flex items-center justify-between bg-gradient-to-r from-blue-400 via-pink-300 to-yellow-200 px-8 py-4 shadow-md">
    <Link to="/">
      <img
        src="/assets/image/manujallll.png"
        alt="로고"
        className="h-12 w-12 rounded-full border-2 border-yellow-400 bg-white shadow-lg"
      />
    </Link>
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
  </header>
);

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
