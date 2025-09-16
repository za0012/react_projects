import ky from "ky";
import { useState } from "react";

const Login = () => {
  const [id, setId] = useState("");
  const [password, setPassword] = useState("");

  const fetchData = async () => {
    try {
      const data = await ky.post("http://localhost:8080/api/users/login");
    } catch (error) {
      console.log("로그인 오류: ", error);
    }
  };
  return (
    <div className="flex min-h-screen items-center justify-center bg-gradient-to-br from-blue-100 via-pink-100 to-yellow-100">
      <form className="w-full max-w-sm space-y-4 rounded-xl bg-white p-8 shadow-lg">
        <h2 className="mb-4 text-center text-2xl font-bold text-blue-600">
          로그인
        </h2>
        <input
          type="text"
          placeholder="아이디를 입력하세요"
          className="w-full rounded-lg border px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-300"
        />
        <input
          type="password"
          placeholder="비밀번호를 입력하세요"
          className="w-full rounded-lg border px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-300"
        />
        <button
          type="submit"
          className="w-full rounded-lg bg-blue-500 py-2 font-bold text-white transition hover:bg-blue-600"
        >
          로그인
        </button>
      </form>
    </div>
  );
};

export default Login;
