import React, { useState, useEffect } from "react";
import { Article } from "@/types/board";
import ky from "ky";
import { useNavigate, useParams } from "@tanstack/react-router";

const BoardDetail = () => {
  const [data, setData] = useState<Article | null>(null);

  const articleId = useParams({ from: "/board/$articleId" });

  useEffect(() => {
    const fetchData = async () => {
      try {
        const data = await ky
          .get(`http://localhost:8080/api/articles/${articleId}`)
          .json<Article>();
        console.log(data);
        setData(data);
      } catch (error) {
        console.error("Error fetching Article detail:", error);
      }
    };
    fetchData();
  }, [articleId]);

  return (
    <div className="mx-auto mt-8 max-w-2xl rounded-lg bg-blue-500 p-6 text-white shadow-lg">
      {data ? (
        <div className="rounded-lg bg-white p-6 text-gray-900 shadow">
          <h1 className="mb-4 text-3xl font-bold text-blue-600">
            {data.title}
          </h1>
          <div className="mb-2 flex items-center gap-4 text-sm text-gray-500">
            <span className="flex items-center gap-1">
              <svg
                className="h-4 w-4 text-blue-400"
                fill="none"
                stroke="currentColor"
                strokeWidth="2"
                viewBox="0 0 24 24"
              >
                <path d="M5.121 17.804A13.937 13.937 0 0112 15c2.5 0 4.847.657 6.879 1.804M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
              </svg>
              작성자: {data.user_id}
            </span>
            <span className="flex items-center gap-1">
              <svg
                className="h-4 w-4 text-blue-400"
                fill="none"
                stroke="currentColor"
                strokeWidth="2"
                viewBox="0 0 24 24"
              >
                <path d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
              </svg>
              {data.createdAt}
            </span>
            <span className="flex items-center gap-1">
              <svg
                className="h-4 w-4 text-blue-400"
                fill="none"
                stroke="currentColor"
                strokeWidth="2"
                viewBox="0 0 24 24"
              >
                <path d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6 6 0 10-12 0v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9" />
              </svg>
              조회수: {data.viewCount}
            </span>
          </div>
          <hr className="my-4" />
          <div className="whitespace-pre-line text-lg leading-relaxed">
            {data.content}
          </div>
        </div>
      ) : (
        <div className="py-8 text-center text-lg">Loading...</div>
      )}
    </div>
    // 제목은 파란색, 크게, 굵게
    // 작성자/날짜/조회수는 아이콘과 함께 한 줄에 표시
    // 본문은 여백과 줄바꿈 유지
    // 전체 카드에 그림자와 여백 추가
  );
};

export default BoardDetail;
