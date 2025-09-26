interface PageProps {
  pageNumber: number;
  totalPages: number;
  onPrevClickBefore: () => void;
  onPrevClickAfter: () => void;
}

const PageNation = ({
  pageNumber,
  totalPages,
  onPrevClickBefore,
  onPrevClickAfter,
}: PageProps) => {
  return (
    <div className="mt-12 flex items-center justify-center space-x-4 text-base text-gray-600">
      <button
        className="rounded-full bg-white px-6 py-2 font-semibold text-gray-700 shadow-md transition duration-200 hover:bg-gray-100 disabled:cursor-not-allowed disabled:bg-gray-200 disabled:text-gray-400"
        onClick={onPrevClickBefore}
        disabled={pageNumber === 0}
      >
        이전
      </button>
      <span className="rounded-full bg-white px-5 py-3 text-base font-medium shadow-sm">
        페이지 <span className="font-bold text-blue-500">{pageNumber + 1}</span>{" "}
        / {totalPages}
      </span>
      <button
        className="rounded-full bg-white px-6 py-2 font-semibold text-gray-700 shadow-md transition duration-200 hover:bg-gray-100 disabled:cursor-not-allowed disabled:bg-gray-200 disabled:text-gray-400"
        onClick={onPrevClickAfter}
        disabled={pageNumber + 1 >= totalPages}
      >
        다음
      </button>
    </div>
  );
};

export default PageNation;
