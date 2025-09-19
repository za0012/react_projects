import { CookieListResponse } from "@/types/cookie";
import ky from "ky";
import { useEffect, useState } from "react";

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
        console.log(data);
        setData(response);
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
    <div className="mx-auto mt-10 max-w-3xl rounded-2xl bg-gradient-to-br from-pink-100 to-blue-100 p-8 shadow-xl">
      {/* 검색창 */}
      <div className="mb-8 flex items-center justify-center space-x-2">
        <input
          type="text"
          placeholder="🍪 쿠키 이름을 입력하세요"
          className="w-64 rounded-full border border-gray-300 px-4 py-2 text-sm shadow-sm focus:border-pink-400 focus:outline-none focus:ring-2 focus:ring-pink-200"
          onChange={e => {
            setSearchValue(() => e.target.value);
          }}
        />
        <button
          className="rounded-full bg-pink-500 px-5 py-2 text-sm font-semibold text-white shadow-md transition duration-200 hover:bg-pink-600"
          onClick={handleSearch}
        >
          🔍 검색
        </button>
      </div>

      {/* 쿠키 리스트 */}
      {data ? (
        !search ? (
          <div className="space-y-6">
            {data.content.map(item => (
              <div
                className="rounded-2xl bg-white p-6 shadow-md transition duration-300 hover:scale-[1.02] hover:shadow-lg"
                key={item.id}
              >
                <h2 className="mb-3 text-2xl font-bold text-[#2a3fff]">
                  {item.name}
                </h2>
                <div className="grid grid-cols-2 gap-x-6 gap-y-2 text-sm text-gray-700">
                  <p>
                    <span className="font-semibold text-gray-900">
                      🩺 체력:
                    </span>{" "}
                    {item.health}
                  </p>
                  <p>
                    <span className="font-semibold text-gray-900">
                      ✨ 능력:
                    </span>{" "}
                    {item.ability || "없음"}
                  </p>
                  <p>
                    <span className="font-semibold text-gray-900">
                      ⭐ 별사탕:
                    </span>{" "}
                    {item.unlockStarCandies}
                  </p>
                  <p>
                    <span className="font-semibold text-gray-900">
                      👫 파트너:
                    </span>{" "}
                    {item.partner || "없음"}
                  </p>
                  <p className="col-span-2">
                    <span className="font-semibold text-gray-900">
                      📅 출시일:
                    </span>{" "}
                    {item.releaseDate}
                  </p>
                </div>
              </div>
            ))}

            {/* 페이지네이션 */}
            <div className="mt-10 flex items-center justify-center space-x-6 text-sm text-gray-700">
              <button
                className="rounded-full bg-blue-500 px-5 py-2 font-semibold text-white shadow-md transition duration-200 hover:bg-blue-600 disabled:cursor-not-allowed disabled:bg-gray-300"
                onClick={() => setPage(prev => Math.max(prev - 1, 0))}
                disabled={data.number === 0}
              >
                &gt; 이전
              </button>

              <span className="rounded-full bg-white px-4 py-2 shadow-inner">
                📄 페이지{" "}
                <span className="font-bold text-blue-500">
                  {data.number + 1}
                </span>{" "}
                / {data.totalPages}
              </span>

              <button
                className="rounded-full bg-blue-500 px-5 py-2 font-semibold text-white shadow-md transition duration-200 hover:bg-blue-600 disabled:cursor-not-allowed disabled:bg-gray-300"
                onClick={() =>
                  setPage(prev => Math.min(prev + 1, data.totalPages - 1))
                }
                disabled={data.number + 1 >= data.totalPages}
              >
                다음 &gt;
              </button>
            </div>
          </div>
        ) : (
          <div className="space-y-6">
            {search.content.map(item => (
              <div
                className="rounded-2xl bg-white p-6 shadow-md transition duration-300 hover:scale-[1.02] hover:shadow-lg"
                key={item.id}
              >
                <h2 className="mb-3 text-2xl font-bold text-[#2a3fff]">
                  {item.name}
                </h2>
                <div className="grid grid-cols-2 gap-x-6 gap-y-2 text-sm text-gray-700">
                  <p>
                    <span className="font-semibold text-gray-900">
                      🩺 체력:
                    </span>{" "}
                    {item.health}
                  </p>
                  <p>
                    <span className="font-semibold text-gray-900">
                      ✨ 능력:
                    </span>{" "}
                    {item.ability || "없음"}
                  </p>
                  <p>
                    <span className="font-semibold text-gray-900">
                      ⭐ 별사탕:
                    </span>{" "}
                    {item.unlockStarCandies}
                  </p>
                  <p>
                    <span className="font-semibold text-gray-900">
                      👫 파트너:
                    </span>{" "}
                    {item.partner || "없음"}
                  </p>
                  <p className="col-span-2">
                    <span className="font-semibold text-gray-900">
                      📅 출시일:
                    </span>{" "}
                    {item.releaseDate}
                  </p>
                </div>
              </div>
            ))}

            {/* 페이지네이션 */}
            <div className="mt-10 flex items-center justify-center space-x-6 text-sm text-gray-700">
              <button
                className="rounded-full bg-blue-500 px-5 py-2 font-semibold text-white shadow-md transition duration-200 hover:bg-blue-600 disabled:cursor-not-allowed disabled:bg-gray-300"
                onClick={() => setPage(prev => Math.max(prev - 1, 0))}
                disabled={data.number === 0}
              >
                &gt; 이전
              </button>

              <span className="rounded-full bg-white px-4 py-2 shadow-inner">
                📄 페이지{" "}
                <span className="font-bold text-blue-500">
                  {data.number + 1}
                </span>{" "}
                / {data.totalPages}
              </span>

              <button
                className="rounded-full bg-blue-500 px-5 py-2 font-semibold text-white shadow-md transition duration-200 hover:bg-blue-600 disabled:cursor-not-allowed disabled:bg-gray-300"
                onClick={() =>
                  setPage(prev => Math.min(prev + 1, data.totalPages - 1))
                }
                disabled={data.number + 1 >= data.totalPages}
              >
                다음 &gt;
              </button>
            </div>
          </div>
        )
      ) : (
        <div className="py-12 text-center text-lg text-gray-600">
          🍪 쿠키 데이터를 불러오는 중이에요...
        </div>
      )}
    </div>
  );
};

export default CookieList;
