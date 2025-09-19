import ky from "ky";
import useAuthStore from "@/types/authStore";

// 기본 URL과 함께 Ky 인스턴스 생성
const api = ky.extend({
  prefixUrl: "http://localhost:8080/api", // 백엔드 API 기본 URL
  hooks: {
    // 요청 보내기 전에 실행되는 훅
    beforeRequest: [
      request => {
        // Zustand 스토어의 상태가 로드되었는지 확인,useAuthStore.getState()를 호출할 때 이미 persist 미들웨어가 로컬 스토리지에서 상태를 동기적으로 불러왔다고 가정
        const { accessToken } = useAuthStore.getState();
        // Zustand 스토어에서 토큰 가져와서 헤더에 추가
        if (accessToken) {
          request.headers.set("Authorization", `Bearer ${accessToken}`);
        }
      },
    ],
  },
});

export default api;
