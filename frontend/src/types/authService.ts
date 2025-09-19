import api from "@/types/ky";
import useAuthStore from "@/types/authStore";
import { LoginResponse } from "@/types/apiTypes"; // 새로 만든 응답 타입 임포트

export const login = async (
  username: string,
  password: string,
): Promise<boolean> => {
  try {
    const response = await api
      .post("users/login", {
        json: { username, password },
      })
      .json<LoginResponse>(); // <LoginResponse> 제네릭으로 응답 타입 지정

    const { data } = response;

    useAuthStore
      .getState()
      .setLogin(data.accessToken, data.refreshToken, data.user); // Zustand 스토어의 setLogin 액션 호출

    console.log("로그인 성공!", response);
    return true;
  } catch (error) {
    console.error("로그인 실패:", error);
    return false;
  }
};

export const logout = (): void => {
  useAuthStore.getState().setLogout();
  console.log("로그아웃 성공!");
}; //간단하게 서버에 로그아웃 요청을 보낼 필요 없이(서버에서 JWT 토큰을 관리하지 않는 경우), 클라이언트 측에서 zustand 스토어의 상태 초기화
