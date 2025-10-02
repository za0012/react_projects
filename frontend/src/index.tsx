// src/main.tsx - TanStack Router용으로 변경
import React from "react";
import ReactDOM from "react-dom/client";
import { RouterProvider, createRouter } from "@tanstack/react-router";
import "@/assets/css/index.css";

// 자동 생성된 라우트 트리 import (Vite 플러그인이 생성해줌)
import { routeTree } from "./routeTree.gen";

// 라우터 생성
const router = createRouter({ routeTree });

// TypeScript를 위한 타입 선언
declare module "@tanstack/react-router" {
  interface Register {
    router: typeof router;
  }
}

// React는 CSR방식으로 작동하는데, 서버는 기본적인 HTML만 전달하고,실제 UI는 브라우저에서 jS가 실행되면서 그려진다. 여기서 기본 HTML은 index.html을 가리킴.
// index.html을 보면 React앱의 진입점인 main.tsx가 연결되어있음.

// 아래 코드 해석
// getElementById로 html의 #root div를 가져오기
// ReactDOM.createRoot으로 ReactRoot 객체 생성
// root.render로 컴포넌트를 root에 렌더링 하기.

// 브라우저가 js파일을 읽고 root div에 react root를 생성하면, 그곳을 기준으로 버츄얼 돔이 구성되고 react 컴포넌트들이 실제 dom에 렌더링된다.
// 브라우저는 JSX 문법을 이해하지 못해서 JSX는 컴파일 타임에 자동으로 React.createElement()로 변환됨
// <h1 className="title">Hello</h1> -> React.createElement("h1", { className: "title" }, "Hello");

// 초기 렌더링에서는 <div id="root">만 존재함.
// React는 Virtual DOM을 기반으로 필요한 요소들을 document.createElement()와 appendChild() 등으로 실제 Dom에 추가함.
//

// 그러니까 js로 돔을 만들어서 배포한다 이런 느낌인데...
//

const root = ReactDOM.createRoot(
  document.getElementById("root") as HTMLElement,
);

root.render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>,
);
