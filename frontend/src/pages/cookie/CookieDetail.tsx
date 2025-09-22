import { CookieDetails } from "@/types/cookie";
import { useParams } from "@tanstack/react-router";
import ky from "ky";
import { useEffect, useState } from "react";

const CookieDetail = () => {
  const [data, setData] = useState<CookieDetails>();

  const { articleId } = useParams({ from: "/cookie/$articleId" });

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await ky
          .get(`http://localhost:8080/api/cookies/${articleId}`)
          .json<CookieDetails>();
        setData(response);
      } catch (error) {
        console.error("쿠키 상세 조회 실패", error);
      }
    };
    fetchData();
  }, [articleId]);

  return (
    <div>
      {data ? (
        <div className="mx-auto mt-12 max-w-xl rounded-2xl bg-white p-8 shadow-xl">
          <div key={data.id}>
            {/* 헤더: 쿠키 이름, 등급 */}
            <div className="mb-6 flex items-center justify-between">
              <h2 className="text-3xl font-extrabold text-gray-800">
                {data.name}
              </h2>
              {data.rarity === "LEGENDARY" ? (
                <span className="rounded-full border-2 border-slate-400 bg-gradient-to-r from-cyan-400 via-fuchsia-400 to-amber-300 px-4 py-1 text-sm font-semibold text-white">
                  {data.rarity}
                </span>
              ) : data.rarity === "EPIC" ? (
                <span className="rounded-full border-2 border-slate-500 bg-purple-600 px-4 py-1 text-sm font-semibold text-white">
                  {data.rarity}
                </span>
              ) : data.rarity === "RARE" ? (
                <span className="rounded-full border-2 border-slate-500 bg-blue-500 px-4 py-1 text-sm font-semibold text-white">
                  {data.rarity}
                </span>
              ) : data.rarity === "COMMON" ? (
                <span className="rounded-full border-2 border-slate-500 bg-amber-600 px-4 py-1 text-sm font-semibold text-white">
                  {data.rarity}
                </span>
              ) : (
                <span className="rounded-full border-2 border-slate-500 bg-gradient-to-r from-yellow-400 to-orange-500 px-4 py-1 text-sm font-semibold text-white">
                  {data.rarity}
                </span>
              )}
            </div>

            {/* 이미지 (만약 있다면) */}
            {data.imageUrl && (
              <div className="mb-6 flex justify-center">
                <img
                  src={data.imageUrl}
                  alt={data.name}
                  className="h-48 w-48 rounded-full object-cover shadow-md"
                />
              </div>
            )}

            {/* 상세 정보 섹션 */}
            <div className="space-y-4">
              <div className="flex items-start">
                <span className="mr-3 text-2xl text-yellow-500">🍪</span>
                <div>
                  <p className="font-bold text-gray-700">능력</p>
                  <p className="text-gray-600">{data.ability}</p>
                </div>
              </div>

              <div className="flex items-start">
                <span className="mr-3 text-2xl text-blue-500">💖</span>
                <div>
                  <p className="font-bold text-gray-700">체력</p>
                  <p className="text-gray-600">{data.health}</p>
                </div>
              </div>

              <div className="flex items-start">
                <span className="mr-3 text-2xl text-green-500">🐾</span>
                <div>
                  <p className="font-bold text-gray-700">짝꿍</p>
                  <p className="text-gray-600">{data.petName || "없음"}</p>
                </div>
              </div>

              <div className="flex items-start">
                <span className="mr-3 text-2xl text-purple-500">✨</span>
                <div>
                  <p className="font-bold text-gray-700">설명</p>
                  <p className="text-gray-600">{data.description}</p>
                </div>
              </div>

              <div className="flex items-start">
                <span className="mr-3 text-2xl text-red-500">📅</span>
                <div>
                  <p className="font-bold text-gray-700">출시일</p>
                  <p className="text-gray-600">{data.releaseDate}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      ) : (
        <div className="py-12 text-center text-lg text-gray-600">
          🍪 쿠키 데이터를 불러오는 중이에요...
        </div>
      )}
    </div>
  );
};

export default CookieDetail;
