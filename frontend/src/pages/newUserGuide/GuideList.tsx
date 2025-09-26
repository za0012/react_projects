import { Link } from "@tanstack/react-router";

const GuideList = () => {
  return (
    <div className="mx-auto mt-8 max-w-2xl rounded-2xl bg-gray-50 p-6 shadow-lg">
      <div className="mb-8 border-b border-gray-200 pb-4">
        <h1 className="text-3xl font-extrabold text-gray-800">
          <span className="mr-2 text-3xl">🍪</span>
          뉴비 가이드
        </h1>
        <p className="mt-1 text-lg text-gray-500">
          쿠키런 오븐브레이크, 궁금한 점을 해결해 보세요!
        </p>
      </div>

      {/* 가이드 목록 */}
      <div className="space-y-4">
        <div className="rounded-xl bg-white p-4 transition-all duration-200 hover:bg-gray-100">
          <Link to="/newUserGuide/guides/recommendCookie">
            <h2 className="text-lg font-bold text-gray-700">
              우선적으로 키워야 하는 쿠키는?
            </h2>
          </Link>
          <p className="text-sm text-gray-500">
            <span className="mr-1">👉</span>초반에 효율 좋은 쿠키 추천
          </p>
        </div>

        <div className="rounded-xl bg-white p-4 transition-all duration-200 hover:bg-gray-100">
          <Link to="/newUserGuide/guides/aboutLegendary">
            <h2 className="text-lg font-bold text-gray-700">
              레전더리가 키우고 싶어요
            </h2>
          </Link>
          <p className="text-sm text-gray-500">
            <span className="mr-1">👉</span>레전더리 쿠키 성장법
          </p>
        </div>

        <div className="rounded-xl bg-white p-4 transition-all duration-200 hover:bg-gray-100">
          <Link to="/newUserGuide/guides/aboutBox">
            <h2 className="text-lg font-bold text-gray-700">
              골상작 어디까지가 좋을까요?
            </h2>
          </Link>
          <p className="text-sm text-gray-500">
            <span className="mr-1">👉</span>최대 효율 골드 상자 작 가이드
          </p>
        </div>

        <div className="rounded-xl bg-white p-4 transition-all duration-200 hover:bg-gray-100">
          <Link to="/newUserGuide/guides/aboutCrystal">
            <h2 className="text-lg font-bold text-gray-700">
              크리스탈, 어디에 먼저 쓰는 게 좋을까요?
            </h2>
          </Link>
          <p className="text-sm text-gray-500">
            <span className="mr-1">👉</span>초보자 크리스탈 사용법
          </p>
        </div>

        <div className="rounded-xl bg-white p-4 transition-all duration-200 hover:bg-gray-100">
          <Link to="/newUserGuide/guides/getCoin">
            <h2 className="text-lg font-bold text-gray-700">
              코인, 어디서 수급해야 하나요?
            </h2>
          </Link>
          <p className="text-sm text-gray-500">
            <span className="mr-1">👉</span>코인 수급을 위한 팁
          </p>
        </div>

        <div className="rounded-xl bg-white p-4 transition-all duration-200 hover:bg-gray-100">
          {/* <Link to="/newUserGuide/guides/aboutLegendary"> */}
          <h2 className="text-lg font-bold text-gray-700">
            레이드런 해야 하나요?
          </h2>
          {/* </Link> */}
          <p className="text-sm text-gray-500">
            <span className="mr-1">👉</span>레이드런 참여 가이드
          </p>
        </div>

        <div className="rounded-xl bg-white p-4 transition-all duration-200 hover:bg-gray-100">
          <Link to="/newUserGuide/guides/aboutGem">
            <h2 className="text-lg font-bold text-gray-700">
              젬 교환소, 뭐 먼저 사야 하나요?
            </h2>
          </Link>
          <p className="text-sm text-gray-500">
            <span className="mr-1">👉</span>교환소 우선순위 추천
          </p>
        </div>

        <div className="rounded-xl bg-white p-4 transition-all duration-200 hover:bg-gray-100">
          <h2 className="text-lg font-bold text-gray-700">
            보물, 뭐 먼저 뽑아야 하나요?
          </h2>
          <p className="text-sm text-gray-500">
            <span className="mr-1">👉</span>추천 보물 목록
          </p>
        </div>
      </div>
    </div>
  );
};

export default GuideList;
