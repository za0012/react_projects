import React, { useEffect, useState } from "react";
import ky from "ky";
import { Article, ArticleListResponse } from "@/types/board";

const Board = () => {
  const [data, setData] = useState<ArticleListResponse | null>(null);

  useEffect(() => { // useEffect를 추가해서 첫 로딩 시에만 데이터가 불러와지도록 함,
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
  }, data);
  return (
    <div>
      {data && data?.content?.map((item) => (
        <div key={item.id}>
          <h2>{item.title}</h2>
          <p>{item.content}</p>
        </div>
      ))}
      {data?.map((item) => {
        <div>{item.}</div>
      })}
    </div>
  );
};

export default Board;
