import React from "react";
import { CookieDetail } from "@/types/cookie";

interface CookieDetails {
  id: number;
  name: string;
  //   imageUrl: string;
  health: number;
  ability: string;
  unlockStarCandies: number;
  partner: string | null;
  releaseDate: string; // ISO 형식 날짜
}
const CookieListCard = ({
  id,
  name,
  health,
  ability,
  unlockStarCandies,
  partner,
  releaseDate,
}: CookieDetails) => {
  return (
    <div
      className="rounded-2xl bg-white p-6 shadow-md transition duration-300 hover:scale-[1.02] hover:shadow-lg"
      key={id}
    >
      <h2 className="mb-3 text-2xl font-bold text-[#2a3fff]">{name}</h2>
      <div className="grid grid-cols-2 gap-x-6 gap-y-2 text-sm text-gray-700">
        <p>
          <span className="font-semibold text-gray-900">🩺 체력:</span> {health}
        </p>
        <p>
          <span className="font-semibold text-gray-900">✨ 능력:</span>{" "}
          {ability || "없음"}
        </p>
        <p>
          <span className="font-semibold text-gray-900">⭐ 별사탕:</span>{" "}
          {unlockStarCandies}
        </p>
        <p>
          <span className="font-semibold text-gray-900">👫 파트너:</span>{" "}
          {partner || "없음"}
        </p>
        <p className="col-span-2">
          <span className="font-semibold text-gray-900">📅 출시일:</span>{" "}
          {releaseDate}
        </p>
      </div>
    </div>
  );
};

export default CookieListCard;
