import React, { useEffect, useState } from "react";
import ky from "ky";
import { Article, ArticleListResponse } from "@/types/board";
import { Link } from "react-router";

const Board = () => {
  const [data, setData] = useState<ArticleListResponse | null>(null);

  useEffect(() => {
    // useEffect를 추가해서 첫 로딩 시에만 데이터가 불러와지도록 함,
    const fetchData = async () => {
      try {
        const data = await ky
          .get(
            "http://localhost:8080/api/articles?page=0&size=10&sortBy=createdAt&sortDir=desc"
          )
          .json<ArticleListResponse>();
        console.log(data);
        setData(data);
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    };
    fetchData();
  }, [setData]);
  return (
    <div className="bg-blue-500 text-white p-6 rounded-lg shadow-lg max-w-2xl mx-auto mt-8">
      {data ? (
        <div className="space-y-4">
          {data.content.map((item) => (
            <div
              key={item.id}
              className="bg-white text-gray-900 p-4 rounded-lg shadow hover:shadow-xl transition-shadow"
            >
              <Link to={`/board/${item.id}`}>
                <h2 className="text-xl font-bold text-blue-600 hover:underline mb-2">
                  {item.title}
                </h2>
              </Link>
              <div className="flex items-center gap-4 text-sm text-gray-500 mb-1">
                <span className="flex items-center gap-1">
                  <svg
                    className="w-4 h-4 text-blue-400"
                    fill="none"
                    stroke="currentColor"
                    strokeWidth="2"
                    viewBox="0 0 24 24"
                  >
                    <path d="M5.121 17.804A13.937 13.937 0 0112 15c2.5 0 4.847.657 6.879 1.804M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
                  </svg>
                  {item.user_id}
                </span>
                <span className="flex items-center gap-1">
                  <svg
                    className="w-4 h-4 text-blue-400"
                    fill="none"
                    stroke="currentColor"
                    strokeWidth="2"
                    viewBox="0 0 24 24"
                  >
                    <path d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                  </svg>
                  {item.createdAt}
                </span>
                <span className="flex items-center gap-1">
                  <svg
                    className="w-4 h-4 text-blue-400"
                    fill="none"
                    stroke="currentColor"
                    strokeWidth="2"
                    viewBox="0 0 24 24"
                  >
                    <path d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6 6 0 10-12 0v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9" />
                  </svg>
                  조회수: {item.viewCount}
                </span>
              </div>
              {/* <p className="text-gray-700">{item.content}</p> */}
            </div>
          ))}
          <div className="text-center text-white mt-6">
            페이지: {data.number + 1}
          </div>
        </div>
      ) : (
        <div className="text-center py-8 text-lg">Loading...</div>
      )}
    </div>
    // 각 게시글을 카드 형태로, 호버 시 그림자 강조
    // 제목은 파란색, 굵게, 링크에 호버 효과
    // 작성자/날짜/조회수에 아이콘 추가
    // 전체 리스트에 간격(space-y-4) 추가
    // 페이지 번호는 하단에 표시
  );
};

export default Board;
