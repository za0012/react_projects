import { create } from "zustand";
import { persist } from "zustand/middleware";

// 사용자 타입 정의
interface User {
  id: number;
  username: string;
  email: string;
}

// 스토어의 상태 타입 정의
interface AuthState {
  accessToken: string | null;
  refreshToken: string | null;
  user: User | null;
  isLoggedIn: boolean;
  setLogin: (accessToken: string, refreshToken: string, user: User) => void;
  setLogout: () => void;
}

// 스토어 생성
// create 함수를 persist 미들웨어로 감싸기
const useAuthStore = create<AuthState>()(
  // create함수의 제네릭으로 상태와 액션 타입을 전달한다.
  persist(
    set => ({
      //set핰수 호출할 때 콜백을 사용하였으므로 get함수를 사용하지 않아도 스토어 객체(state)를 얻을 수 있다.
      // 상태 초깃값
      accessToken: null,
      refreshToken: null,
      user: null,
      isLoggedIn: false,

      // 로그인 상태 업데이트 (액션)
      setLogin: (
        accessToken,
        refreshToken,
        user, //이렇게 상태를 선언하고 생성한 스토어를 컴포넌트에서 사용할 수 있다.
      ) =>
        set({
          accessToken,
          refreshToken,
          user,
          isLoggedIn: true,
        }),

      // 로그아웃 상태 업데이트 (액션)
      setLogout: () =>
        set({
          //set으로 상태 변경
          accessToken: null,
          refreshToken: null,
          user: null,
          isLoggedIn: false,
        }),
    }),
    {
      name: "auth-storage", // 로컬 스토리지에 저장될 키 이름
      // getStorage: () => sessionStorage, // 로컬 스토리지가 아닌 세션 스토리지 사용 시
      partialize: state => ({
        accessToken: state.accessToken,
        refreshToken: state.refreshToken,
        user: state.user,
        isLoggedIn: state.isLoggedIn,
      }),
    },
  ),
);

export default useAuthStore;
