# [2025.09.08 ~ 2025.09.19]

# 라이브러리 관련

    1. 라우팅 구현을 위해 react-router추가
    2. SEO를 위해 BrowserRouter 사용
    3. 서버와 통신하기 위해 ky 추가
        - axios에 비해 작은 번들
        - 깔끔한 코드
    4. 추후 유지보수 시 사용하지 않는 css제거, 직관적으로 클래스 파악를 위해 tailwindcss 추가
        - 추가로 효율성과 전체적인 스타일링 속도
        - 4.x버전이 오류가 일어나 3.4.0버전으로 수정
        - tailwind.config.js 파일에 Tailwind CSS를 사용할 모든 템플릿 파일에 대한 경로를 추가
    5. 코드 일관성을 위해 Prettier추가.
    6. 추후 유지보수를 위해 Eslint 추가
        - 잘못 입력한 문법 및 일관성 유지를 위함
    7. yarn pnp 가 설치 속도가 제일 낮음.
    8. 기존에는 airbnb 규칙을 사용하면 airbnb 조직이 정한 코딩 스타일 가이드를 따라가야한다는 단점이 있어
       보다 나은 rushstack을 사용하기 위해 라이브러리를 설치한 후 규칙을 추가했다.
       허나 rushstack을 설치하고 세팅하는 과정에서
        1. 설치해야하는 라이브러리가 너무 많음
        2. rushstack은 대형 모노레포에 적합함.
        3. 개인 프로젝트(또는 단일 프로젝트)에 넣기에는 과도하게 복잡함.
        4. rushstack을 넣으려던 이유인 빌드 최적화가 무의미해질 가능성 존재
      등 여러 이유로 다시 rushstack을 제거하고 rushstack을 추가하며 개별적으로 더 추가해야하는 라이브러리들로만 하기로 했다.
    9. pnpm dev로 빌드 시 빌드 속도가 너무 느려 vite 라이브러리를 추가했다.
        - 빌드 관련 라이브러리로 유명한 건 webpack과 vite가 대표적이었는데
            1. vite가 webpack에 비해 인터페이스가 단순함
            2. vite가 webpack보다 빠름(허나 webpack을 어떻게 설정하는지에 따라 다름)
            3. 사용하기 쉬움
        위와 같은 이유로 vite를 사용하게 되었고, 실제 실행 시 약 3,4배 더 빨라진 것을 체감할 수 있었다. 짱!
    10. react-router에서 tanstack router로 마이그레이션
        - 많은 사정이 있다.... 우선 최근 react-router v7은 더이상 react-router-dom을 필요로 하지 않게 되었다. 다만 우리가 기존에 알던 구조가 아닌 완전히 다른 구조으로 라우터 파일을 만들어야 했기에 이에 불편함을 느껴 다른 라우터를 찾아보게 되었다.
        - 그중 내 눈에 들어온 게 tanstack router이었다. 많은 사람들이 사용하는 라이브러리인데, 타입 안전성이 있어 코드를 안정적으로 유지할 수 있다는 장점이 있다.
        - 해당 tanstack router로 마이그레이션 하기 위해 라이브러리를 설치 한 다음 파일 구조를 전부 갈아엎었다.
            1. App.tsx는 더이상 필요하지 않기 때문에 삭제했다.
            2. router폴더에 루트 파일을 만든 후 안에 페이지와 같은 이름을 가진 파일들을 넣는다. 다만 여기서 write나 list는 board이라는 같은 루트를 사용하기 있기 때문에 index, $articleId, write이런 식으로 파일을 만들었다. 폴더 라우팅 방식과 비슷하다고 생각하면 될 것 같다. 추가로 TanStack Router의 Vite 플러그인이 실시간으로 파일을 감시하고 있어서 파일을 만들면 내용이 자동으로 생성된다.
            3. 페이지를 이동하기 위해서는 const navigate = useNavigate(); 해당 구문을 넣은 뒤
                ```js
                onClick={() => {
                    navigate({ to: "" });
                }}
                ```
            해당 코드 또는 기존대로 Link를 넣으면 된다.
            const params = useParams({ from: '/board/$articleId' }); 이건 파라미터.
                - 부록
                  Link, useNavigate언제 사용하면 좋을까?
                  Link는 단순 클릭으로 이동할 때 사용하면 좋다. 텍스트와 같은 요소 클릭...
                  useNavigate는 로직 처리 후 이동할 때 사용하면 좋다. 로그인 성공 후 이동이라던가... 글쓰기 완료 후 이동같은.
            4.index.tsx는 아래와 같은 코드를 넣어줘야한다.
            ```js
            // 자동 생성된 라우트 트리 import (Vite 플러그인이 생성해줌)
            import { routeTree } from './routeTree.gen'

            // 라우터 생성
            const router = createRouter({ routeTree })

            // TypeScript를 위한 타입 선언
            declare module '@tanstack/react-router' {
                interface Register {
                    router: typeof router
                }
            }
            ```
            5. 제일 중요한 게 __root.tsx파일이다. 이게 App.tsx랑 비슷하다고 보면 된다.
    11. zustand 추가
        로그인은 가능하나, 로그인 상태를 유지할 수 없었기 때문에 백엔드에 JWT 토큰 기반 인증을 추가했다.
        해당 토큰을 저장함으로써 로그인 상태를 유지해야했기 때문에 store를 구현하기 위해 해당 라이브러리를 추가했다.

# 타입 정의 관련

    1. 게시글 타입 파일을 추가. sort는 2번 사용되므로 분리.
    2. article은 향후 재사용 가능성이 있어 미리 분리.
    3. 쿠키 데이터 테이블을 추가했으므로 관련 타입 파일을 추가. 쿠키는 추후 재사용 가능성이 있어 분리

# 코드 관련

    1. Board.tsx에 기존에 데이터를 가져와 console로 호출하는 코드를 사용하였으나, 콘솔에 몇 백번씩 찍히는 상황 발생, useEffect를 통해 한 번만 호출하도록 수정
    2. 게시글 디테일 tsx추가
    3. 기존에는 props로 id를 가져와서 articleDetail에 넘겼으나, 새로고침하면 사라진다는 점과 데이터를 id 1개만 전달하기 때문에 파라미터를 가져와 넣는 것으로 변경
    4. 게시글 리스트에서 게시글 클릭 시 이동하도록 link 추가
    5. 보드 페이지 및 상세 페이지의 UI 개선 및 데이터 표시 방식 수정
    6. tailwind를 사용하게 되며 사용하지 않는 css호출 삭제
    7. about문구 및 스타일 추가
    8. board에 주석 추가
    9. 마이그레이션 하면서 사용하지 않게 된 코드 및 파일 정리
    10. 쿠키 관련 DB를 추가하게 되면서 관련 파일 추가
    11. 조회는 토큰이 필요 없으므로 백엔드 로직 수정

# 패키지 관련

    1. 기존에는 yarn을 사용하였으나, pnpm으로 변경
        - yarn을 쓰다 보니 생긴 문제점이 node_modules가 차지하는 공간이 너무 크다는 것이었다. 이미 많은 프로젝트를 많든 내 집컴은 저장공간이 많이 없었기 때문에 다른 모듈을 써야겠다는 생각이 들었다.
        - 모노레포에 관련하여 찾아보다가 yarn berry에 알게 됨.
          초기에는 PnP라는 yarn berry의 강력한 장점이 있어 yarn berry를 사용하여 마이그레이션 하려 했으나
          PnP를 지원하지 않는 패키지가 존재한다는 점과, Yarn PnP가 Git에 지속적으로 주는 부하, zero-install로 인해 추후 발생하게 될 문제들 때문에 yarn berry가 아닌 pnpm으로 바꿨다. (위의 이유와 더불어서)
          참조: https://velog.io/@imeureka/Yarn-berry-Pnpm-%EC%96%B4%EB%96%A8-%EB%95%8C-%EC%8D%A8%EC%95%BC%ED%95%98%EB%8A%94%EA%B0%80
                https://hackle.io/ko/post/frontend-pnpm/

{
"src": "logo192.png",
"type": "image/png",
"sizes": "192x192"
},
{
"src": "logo512.png",
"type": "image/png",
"sizes": "512x512"
}
