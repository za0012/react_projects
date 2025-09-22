import { CookieDetails } from "@/types/cookie";
import { Link } from "@tanstack/react-router";

interface CookieDetail {
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
}: CookieDetail) => {
  return (
    <div
      className="rounded-2xl bg-white p-6 shadow-sm transition duration-300 hover:shadow-lg"
      key={id}
    >
      <Link to="/cookie/$articleId" params={{ articleId: String(id) }}>
        <h2 className="mb-3 text-2xl font-bold text-gray-800">{name}</h2>
      </Link>
      <div className="grid grid-cols-2 gap-x-6 gap-y-2 text-sm text-gray-600">
        <p>
          <span className="font-semibold text-gray-800">💖 체력:</span> {health}
        </p>
        <p>
          <span className="font-semibold text-gray-800">✨ 능력:</span>{" "}
          {ability || "없음"}
        </p>
        <p>
          <span className="font-semibold text-gray-800">🍬 별사탕:</span>{" "}
          {unlockStarCandies}
        </p>
        <p>
          <span className="font-semibold text-gray-800">🐾 짝꿍:</span>{" "}
          {partner || "없음"}
        </p>
        <p className="col-span-2">
          <span className="font-semibold text-gray-800">📅 출시일:</span>{" "}
          {releaseDate}
        </p>
      </div>
    </div>
  );
};

export default CookieListCard;
