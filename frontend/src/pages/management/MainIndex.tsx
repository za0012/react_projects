import Sidebar from "@/components/Sidebar";
import useAuthStore from "@/types/authStore";
import { useNavigate } from "@tanstack/react-router";

const MainIndex = () => {
  const { isLoggedIn, user } = useAuthStore();
  const navigate = useNavigate();

  return (
    <div className="flex min-h-screen bg-gray-50">
      <Sidebar />
      <div className="m-auto flex-1 p-8">
        {user?.admin ? (
          <div className="max-w-xl rounded-2xl bg-white p-8 text-center shadow-2xl">
            <h1 className="mb-8 text-3xl font-extrabold text-gray-800">
              <span className="text-blue-500">{user.username}</span>님,
              환영합니다!
            </h1>
            <p className="mb-10 text-gray-500">관리자 기능을 사용해 보세요.</p>

            <div className="grid grid-cols-2 gap-6 md:grid-cols-3">
              <button
                className="flex flex-col items-center justify-center rounded-2xl bg-white p-6 shadow-md transition-all duration-300 hover:scale-105 hover:bg-gray-100"
                onClick={() => {
                  navigate({ to: "/management/cookieMan" });
                }}
              >
                <span className="mb-2 text-4xl">🍪</span>
                <span className="font-semibold text-gray-700">쿠키 관리</span>
              </button>
              <button
                className="flex flex-col items-center justify-center rounded-2xl bg-white p-6 shadow-md transition-all duration-300 hover:scale-105 hover:bg-gray-100"
                onClick={() => {
                  navigate({ to: "/management/treasure" });
                }}
              >
                <span className="mb-2 text-4xl">💎</span>
                <span className="font-semibold text-gray-700">보물 관리</span>
              </button>
              <button
                className="flex flex-col items-center justify-center rounded-2xl bg-white p-6 shadow-md transition-all duration-300 hover:scale-105 hover:bg-gray-100"
                onClick={() => {
                  navigate({ to: "/management/petMan" });
                }}
              >
                <span className="mb-2 text-4xl">🐾</span>
                <span className="font-semibold text-gray-700">펫 관리</span>
              </button>
              <button
                className="flex flex-col items-center justify-center rounded-2xl bg-white p-6 shadow-md transition-all duration-300 hover:scale-105 hover:bg-gray-100"
                onClick={() => {
                  navigate({ to: "/management/banner" });
                }}
              >
                <span className="mb-2 text-4xl">🔔</span>
                <span className="font-semibold text-gray-700">배너 관리</span>
              </button>
              <button
                className="flex flex-col items-center justify-center rounded-2xl bg-white p-6 shadow-md transition-all duration-300 hover:scale-105 hover:bg-gray-100"
                onClick={() => {
                  navigate({ to: "/management/banner" });
                }}
              >
                <span className="mb-2 text-4xl">📜</span>
                <span className="font-semibold text-gray-700">게시판 관리</span>
              </button>
              <button
                className="flex flex-col items-center justify-center rounded-2xl bg-white p-6 shadow-md transition-all duration-300 hover:scale-105 hover:bg-gray-100"
                onClick={() => {
                  navigate({ to: "/management/inquiry" });
                }}
              >
                <span className="mb-2 text-4xl">🎸</span>
                <span className="font-semibold text-gray-700">1:1 문의들</span>
              </button>
            </div>
          </div>
        ) : (
          <div className="rounded-2xl bg-white p-8 text-center shadow-lg">
            <h1 className="mb-4 text-2xl font-bold text-red-500">
              접근 권한이 없습니다.
            </h1>
            <p className="text-gray-600">
              이 페이지는 관리자만 접근할 수 있습니다.
            </p>
          </div>
        )}
      </div>
    </div>
  );
};

export default MainIndex;
