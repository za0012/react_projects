import React, { useEffect, useState } from "react";
import ky from "ky";
import { ArticleListResponse } from "@/types/board";
import { Link } from "@tanstack/react-router";

const Board = () => {
  const [data, setData] = useState<ArticleListResponse | null>(null);
  const [page, setPage] = useState(0);
  //상태가 점점 많이질수록 useReducer를 쓰는게 좋음. 뭐 data, page, loading, error, hasMore 등등... 추후 리팩토링 할 때 고려해볼 것.
  // useReducer는 상태 전환 로직을 컴포넌트 외부로 분리하여 관리할 수 있게 해줌. (Redux와 비슷한 개념)
  // useState는 상태가 단순하고 서로 독립적이고 업데이트가 간단한 경우에 사용
  // useReducer는 상태 로직이 복잡하고 여러 상태가 서로 연괸돼고 액션 타입으로 의도를 명확히 하고 싶을 때 사용한다.

  useEffect(() => {
    // useEffect를 추가해서 첫 로딩 시에만 데이터가 불러와지도록 함,
    const fetchData = async () => {
      try {
        const data = await ky
          .get(
            `http://localhost:8080/api/articles?page=${page}&size=10&sortBy=createdAt&sortDir=desc`,
          )
          .json<ArticleListResponse>();
        console.log(data);
        setData(data);
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    };
    fetchData();
  }, [page]); // 불필요한 렌더링을 줄이기 위해 의존성 배열(deps)을 인자로 받도록 설정 (특정 값이 변경될 때만 다시 실행), 빈 값을 넣어주면 첫 렌더링 시에만 실행됨. 변화가 없기 때문이다.
  return (
    <div className="mx-auto mt-8 max-w-2xl rounded-lg bg-blue-500 p-6 text-white shadow-lg">
      <Link to="/board/write">
        <button className="mb-4 rounded bg-white px-4 py-2 text-blue-600 shadow hover:bg-gray-100">
          글쓰기
        </button>
      </Link>
      {data ? (
        <div className="space-y-4">
          {data.content.map(item => (
            <div
              key={item.id}
              className="rounded-lg bg-white p-4 text-gray-900 shadow transition-shadow hover:shadow-xl"
            >
              <Link
                to="/board/$articleId"
                params={{ articleId: String(item.id) }}
              >
                <h2 className="mb-2 font-sans text-xl font-bold text-blue-600 hover:underline">
                  {item.title}
                </h2>
              </Link>
              <div className="mb-1 flex items-center gap-4 text-sm text-gray-500">
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
                  {item.user_id}
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
                  {item.createdAt}
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
                  조회수: {item.viewCount}
                </span>
              </div>
              {/* <p className="text-gray-700">{item.content}</p> */}
            </div>
          ))}
          <div className="mt-6 text-center text-white">
            페이지: {data.number + 1}
            <div className="mt-2 space-x-2">
              <button
                onClick={() => {
                  setPage(prev => Math.max(prev - 1, 0)); //주어진 숫자 중 가장 큰 수 반환
                }}
                className="rounded bg-blue-600 px-4 py-2 text-white shadow hover:bg-blue-700"
              >
                이전
              </button>
              <button
                onClick={() => {
                  setPage(prev => Math.min(prev + 1, data.totalPages - 1)); //주어진 숫자 중 가장 작은 수 반환, 0부터 시작해서 totalPages-1까지 가능
                }}
                className="rounded bg-blue-600 px-4 py-2 text-white shadow hover:bg-blue-700"
              >
                다음
              </button>
            </div>
          </div>
        </div>
      ) : (
        <div className="py-8 text-center text-lg">Loading...</div>
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
