import React, { useEffect, useState } from "react";
import ky from "ky";
import { Article, ArticleListResponse } from "@/types/board";
import { Link } from "react-router";

const Board = () => {
  const [data, setData] = useState<ArticleListResponse | null>(null);

  useEffect(() => {
    // useEffect를 추가해서 첫 로딩 시에만 데이터가 불러와지도록 함,
    const fetchData = async () => {
      try {
        const data = await ky
          .get(
            "http://localhost:8080/api/articles?page=0&size=10&sortBy=createdAt&sortDir=desc"
          )
          .json<ArticleListResponse>();
        console.log(data);
        setData(data);
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    };
    fetchData();
  }, [setData]);
  return (
    <div className="bg-blue-500 text-white p-4 rounded-lg">
      {data ? (
        data?.content?.map((item) => (
          <div key={item.id}>
            <Link to={`/board/${item.id}`}>
              <h2>{item.title}</h2>
            </Link>
            <p>{item.user_id}</p>
            {/* <p>{item.content}</p> */}
          </div>
        ))
      ) : (
        <div>Loading...</div>
      )}
      {data ? <div>{data.number}</div> : <div>Loading...</div>}
    </div>
  );
};

export default Board;
