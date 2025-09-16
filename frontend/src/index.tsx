// src/main.tsx - TanStack Router용으로 변경
import React from "react";
import ReactDOM from "react-dom/client";
import { RouterProvider, createRouter } from '@tanstack/react-router'
import "@/assets/css/index.css";

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

const root = ReactDOM.createRoot(
  document.getElementById("root") as HTMLElement,
);

root.render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>,
);