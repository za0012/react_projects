import { CookieListResponse } from "@/types/cookie";
import ky from "ky";
import { useEffect, useState } from "react";
import CookieListCard from "@/components/CookieListCard";

const CookieList = () => {
  const [page, setPage] = useState(0);
  const [search, setSearch] = useState<CookieListResponse>();
  const [searchValue, setSearchValue] = useState("");
  const [data, setData] = useState<CookieListResponse | null>(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await ky
          .get(
            `http://localhost:8080/api/cookies?page=${page}&size=10&sortBy=health&sortDir=desc`,
          )
          .json<CookieListResponse>();
        setData(response);
        console.log(data);
      } catch (error) {
        console.log("데이터를 가져오지 못했습니다.", error);
      }
    };
    fetchData();
  }, [page]);

  const handleSearch = async () => {
    try {
      const filterData = await ky
        .get(
          `http://localhost:8080/api/cookies/search?type=all&keyword=${searchValue}`,
        )
        .json<CookieListResponse>();
      console.log(filterData);
      setSearch(filterData);
    } catch (error) {
      console.error("검색 오류", error);
    }
  };
  return (
    <div className="min-h-screen bg-gray-50 p-8">
      <div className="mx-auto max-w-7xl">
        {/* 헤더 또는 제목 영역 */}
        {/* 검색창 */}
        <div className="mb-12 flex items-center justify-center space-x-2">
          <input
            type="text"
            placeholder="🍪 쿠키 이름을 입력하세요"
            className="w-full max-w-md rounded-full border border-gray-200 px-5 py-3 text-base text-gray-700 shadow-inner focus:border-blue-400 focus:outline-none focus:ring-1 focus:ring-blue-200"
            onChange={e => setSearchValue(e.target.value)}
          />
          <button
            className="rounded-full bg-blue-500 px-6 py-3 text-base font-semibold text-white shadow-md transition duration-200 hover:bg-blue-600"
            onClick={handleSearch}
          >
            검색
          </button>
        </div>

        {/* 쿠키 리스트 */}
        {data ? (
          <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-3">
            {(!search ? data.content : search.content).map(item => (
              <CookieListCard
                id={item.id}
                key={item.id}
                name={item.name}
                health={item.health}
                ability={item.ability}
                unlockStarCandies={item.unlockStarCandies}
                partner={item.partner}
                releaseDate={item.releaseDate}
              />
            ))}
          </div>
        ) : (
          <div className="flex h-64 animate-pulse items-center justify-center text-xl text-gray-500">
            🍪 쿠키 데이터를 불러오는 중이에요...
          </div>
        )}

        {/* 페이지네이션 */}
        {data && (
          <div className="mt-12 flex items-center justify-center space-x-4 text-base text-gray-600">
            <button
              className="rounded-full bg-white px-6 py-3 font-semibold text-gray-700 shadow-md transition duration-200 hover:bg-gray-100 disabled:cursor-not-allowed disabled:bg-gray-200 disabled:text-gray-400"
              onClick={() => setPage(prev => Math.max(prev - 1, 0))}
              disabled={data?.number === 0}
            >
              이전
            </button>
            <span className="rounded-full bg-white px-5 py-3 text-base font-medium shadow-sm">
              페이지{" "}
              <span className="font-bold text-blue-500">
                {data?.number + 1}
              </span>
              / {data?.totalPages}
            </span>
            <button
              className="rounded-full bg-white px-6 py-3 font-semibold text-gray-700 shadow-md transition duration-200 hover:bg-gray-100 disabled:cursor-not-allowed disabled:bg-gray-200 disabled:text-gray-400"
              onClick={() =>
                setPage(prev => Math.min(prev + 1, (data?.totalPages || 1) - 1))
              }
              disabled={data?.number + 1 >= (data?.totalPages || 1)}
            >
              다음
            </button>
          </div>
        )}
      </div>
    </div>
  );
};

export default CookieList;
