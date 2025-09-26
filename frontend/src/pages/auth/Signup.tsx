import ky from "ky";
import { useState } from "react";

const Signup = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [email, setEmail] = useState("");

  const fetchData = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    try {
      const response = await ky
        .post("http://localhost:8080/api/users/register", {
          json: { username, password, email },
        })
        .json();
      console.log(response);
      window.location.href = "/auth/login";
    } catch (error: any) {
      console.log("회원가입 오류: ", error);
      if (error.response?.status === 400) {
        alert("존재하는 아이디입니다.");
      }
    }
  };
  return (
    <div className="flex min-h-screen flex-col items-center justify-center bg-gradient-to-br from-blue-100 via-pink-100 to-yellow-100">
      <form
        className="w-full max-w-sm space-y-4 rounded-xl bg-white p-8 shadow-lg"
        onSubmit={fetchData}
      >
        <h2 className="mb-4 text-center text-2xl font-bold text-blue-600">
          회원가입
        </h2>
        <input
          type="text"
          placeholder="아이디를 입력하세요"
          className="input-style"
          onChange={e => setUsername(() => e.target.value)}
        />
        <input
          type="password"
          placeholder="비밀번호를 입력하세요"
          className="input-style"
          onChange={e => setPassword(() => e.target.value)}
        />
        <input
          type="email"
          placeholder="이메일을 입력하세요"
          className="input-style"
          onChange={e => setEmail(() => e.target.value)}
        />
        <button
          type="submit"
          className="w-full rounded-lg bg-blue-500 py-2 font-bold text-white transition hover:bg-blue-600"
        >
          회원가입
        </button>
      </form>
    </div>
  );
};

export default Signup;
