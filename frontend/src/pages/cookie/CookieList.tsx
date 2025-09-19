import { CookieListResponse } from "@/types/cookie";
import ky from "ky";
import { useEffect, useState } from "react";

const CookieList = () => {
  const [page, setPage] = useState(0);
  const [data, setData] = useState<CookieListResponse | null>(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await ky
          .get(
            `http://localhost:8080/api/cookies?page=${page}&size=10&sortBy=health&sortDir=desc`,
          )
          .json<CookieListResponse>();
        console.log(data);
        setData(response);
      } catch (error) {
        console.log("데이터를 가져오지 못했습니다.", error);
      }
    };
    fetchData();
  }, [page]);
  return (
    <div className="mx-auto mt-8 max-w-4xl p-4">
      {data ? (
        <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-3">
          {data.content.map(item => (
            <div
              key={item.id}
              className="overflow-hidden rounded-xl bg-white shadow-md transition-all duration-300 hover:shadow-xl"
            >
              {/* 이미지 컨테이너 (예시: 이미지가 있다면) */}
              {/* <div className="h-48 w-full bg-gray-200"></div> */}

              <div className="p-6">
                <h3 className="mb-2 text-2xl font-bold text-gray-800">
                  {item.name}
                </h3>
                <p className="mb-1 text-gray-600">
                  <strong className="font-semibold">체력:</strong> {item.health}
                </p>
                <p className="mb-1 text-gray-600">
                  <strong className="font-semibold">능력:</strong>{" "}
                  {item.ability}
                </p>
                <p className="mb-1 text-gray-600">
                  <strong className="font-semibold">별사탕:</strong>{" "}
                  {item.unlockStarCandies}
                </p>
                <p className="text-sm text-gray-500">
                  <strong className="font-semibold">출시일:</strong>{" "}
                  {item.releaseDate}
                </p>
              </div>
            </div>
          ))}
          <div className="mt-6 text-center text-blue-600">
            <button
              className="rounded bg-blue-600 px-4 py-2 text-white shadow hover:bg-blue-700"
              onClick={() => {
                setPage(prev => Math.min(prev - 1, 0));
              }}
            >
              이전
            </button>
            <p>페이지 {data.number + 1}</p>
            <button
              className="rounded bg-blue-600 px-4 py-2 text-white shadow hover:bg-blue-700"
              onClick={() => {
                setPage(prev => Math.min(prev + 1, data.totalPages - 1));
              }}
            >
              다음
            </button>
          </div>
        </div>
      ) : (
        <div className="flex h-64 items-center justify-center rounded-lg bg-white shadow-md">
          <div className="animate-pulse text-xl text-gray-600">Loading...</div>
        </div>
      )}
    </div>
  );
};

export default CookieList;
