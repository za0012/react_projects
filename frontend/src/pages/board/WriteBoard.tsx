import ky from "ky";
import React, { useEffect, useState } from "react";
import { ArticleWrite } from "@/types/board";

const WriteBoard = () => {
  const [article, setArticle] = useState<ArticleWrite>({
    title: "",
    content: "",
    username: 0,
  });
  const [isLoading, setIsLoading] = useState(true);
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");

  const fetchData = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    try {
      console.log("send함수실행");
      console.log("title", title);
      console.log("content", content);

      const username = "임시";

      const data = await ky
        .post("http://localhost:8080/api/articles", {
          json: {
            title,
            content,
            username: 1,
          },
        })
        .json<ArticleWrite>();
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };

  return (
    <div className="flex min-h-screen items-center justify-center bg-gradient-to-br from-blue-100 via-pink-100 to-yellow-100">
      <form
        className="w-full max-w-md space-y-4 rounded-xl bg-white p-8 shadow-lg"
        onSubmit={fetchData}
      >
        <h2 className="mb-4 text-center text-2xl font-bold text-blue-600">
          게시글 작성
        </h2>
        <input
          type="text"
          placeholder="제목을 입력하세요"
          className="w-full rounded-lg border px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-300"
          onChange={e => setTitle(() => e.target.value)}
        />
        <textarea
          placeholder="내용을 입력하세요"
          className="h-40 w-full resize-none rounded-lg border px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-300"
          onChange={e => setContent(() => e.target.value)}
        />
        <button
          type="submit"
          className="w-full rounded-lg bg-blue-500 py-2 font-bold text-white transition hover:bg-blue-600"
        >
          글쓰기
        </button>
      </form>
    </div>
  );
};

export default WriteBoard;

// POST /api/articles
// Content-Type: application/json
// Authorization: Bearer {token}

// {
//   "title": "게시글 제목",
//   "content": "게시글 내용"
// }
