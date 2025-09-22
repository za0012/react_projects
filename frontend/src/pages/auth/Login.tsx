import { Link } from "@tanstack/react-router";
import { useState } from "react";
import { login } from "@/types/authService";

const Login = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const handleLogin = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    await login(username, password);
    window.location.href = "/";
  };

  return (
    <div className="flex min-h-screen flex-col items-center justify-center bg-gradient-to-br from-blue-100 via-pink-100 to-yellow-100">
      <form
        className="w-full max-w-sm space-y-4 rounded-xl bg-white p-8 shadow-lg"
        onSubmit={handleLogin}
      >
        <h2 className="mb-4 text-center text-2xl font-bold text-blue-600">
          로그인
        </h2>
        <input
          name="username"
          type="text"
          placeholder="아이디를 입력하세요"
          className="input-style"
          onChange={e => setUsername(() => e.target.value)}
        />
        <input
          name="password"
          type="password"
          placeholder="비밀번호를 입력하세요"
          className="input-style"
          onChange={e => setPassword(() => e.target.value)}
        />
        <button
          type="submit"
          className="w-full rounded-lg bg-blue-500 py-2 font-bold text-white transition hover:bg-blue-600"
        >
          로그인
        </button>
      </form>
      <div className="mt-4 text-center text-sm text-gray-600">
        <Link to="/auth/signup">
          <p className="text-blue-500 hover:underline">회원가입</p>
        </Link>
        <p className="mt-4 text-center text-sm text-gray-600">
          임시 id, password: setEmail/setEmail
        </p>
      </div>
    </div>
  );
};

export default Login;
