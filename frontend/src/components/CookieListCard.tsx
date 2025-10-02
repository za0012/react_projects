import { CookieDetails } from "@/types/cookie";
import { Link } from "@tanstack/react-router";
import cookieex from "@/assets/image/escargo.png";

interface CookieDetail {
  id: number;
  name: string;
  //   imageUrl: string;
  health: number;
  ability: string;
  unlockStarCandies: number;
  partner: string | null;
  releaseDate: string; // ISO 형식 날짜
  // deleteFn: () => void;
}
const CookieListCard = ({
  id,
  name,
  health,
  ability,
  unlockStarCandies,
  partner,
  // releaseDate,
  // deleteFn,
}: CookieDetail) => {
  return (
    <div
      className="relative rounded-2xl bg-white p-5 shadow-lg transition duration-300 hover:shadow-xl"
      key={id}
    >
      {/* 삭제 버튼: 우측 상단에 작은 동그란 아이콘 버튼으로 깔끔하게 변경 */}
      <button
        // Link 태그와 겹치지 않도록 이벤트 버블링 방지
        // onClick={e => {
        //   e.preventDefault();
        //   e.stopPropagation();
        //   deleteFn();
        // }}
        className="absolute right-3 top-3 z-10 flex h-8 w-8 items-center justify-center rounded-full bg-red-50 text-red-500 shadow-sm transition duration-200 hover:bg-red-500 hover:text-white"
        aria-label="쿠키 삭제"
      >
        <svg // x 아이콘 사용 (삭제/닫기)
          className="h-4 w-4"
          fill="none"
          stroke="currentColor"
          strokeWidth="3"
          viewBox="0 0 24 24"
        >
          <path d="M6 18L18 6M6 6l12 12" />
        </svg>
      </button>

      <Link to="/cookie/$articleId" params={{ articleId: String(id) }}>
        <h2 className="mb-4 pr-10 text-xl font-extrabold text-gray-800 transition-colors hover:text-blue-600">
          {name}
        </h2>

        <div className="flex gap-x-6">
          {/* 쿠키 이미지 (전신) */}
          <div className="flex h-1/3 w-1/2 flex-shrink-0 items-end justify-center overflow-hidden rounded-lg">
            {/* 'cookieex'는 전신 이미지를 담는 변수라고 가정합니다. */}
            <img
              src={cookieex}
              alt={`${name} 쿠키 전신`}
              className="h-full w-full object-cover"
            />
          </div>

          {/* 쿠키 정보 (2x2 그리드 형태로 간결하게 정리) */}
          <div className="flex-1 space-y-2 text-sm text-gray-700">
            {/* 체력 */}
            <div className="flex items-center">
              <span className="mr-3 text-lg font-bold text-red-500">💖</span>
              <p>
                <span className="font-semibold text-gray-800">체력:</span>{" "}
                {health}
              </p>
            </div>

            {/* 능력 */}
            <div className="flex items-center">
              <span className="mr-3 text-lg font-bold text-yellow-500">✨</span>
              <p>
                <span className="font-semibold text-gray-800">능력:</span>{" "}
                {ability || "없음"}
              </p>
            </div>

            {/* 별사탕 */}
            <div className="flex items-center">
              <span className="mr-3 text-lg font-bold text-pink-500">🍬</span>
              <p>
                <span className="font-semibold text-gray-800">별사탕: </span>
                {unlockStarCandies}
              </p>
            </div>

            {/* 짝꿍 */}
            <div className="flex items-center">
              <span className="mr-3 text-lg font-bold text-green-500">🐾</span>
              <p>
                <span className="font-semibold text-gray-800">짝꿍:</span>{" "}
                {partner || "없음"}
              </p>
            </div>
          </div>
        </div>
      </Link>
    </div>
  );
};

export default CookieListCard;
