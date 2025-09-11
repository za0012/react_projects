import React, { useState, useEffect } from "react";
import { Article } from "@/types/board";
import ky from "ky";
import { useParams } from "react-router";

interface Props {
  articleId: number;
}

const BoardDetail = () => {
  const [data, setData] = useState<Article | null>(null);
  const { articleId } = useParams<{ articleId: string }>();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const data = await ky
          .get(`http://localhost:8080/api/articles/${articleId}`)
          .json<Article>();
        console.log(data);
        setData(data);
      } catch (error) {
        console.error("Error fetching Article detail:", error);
      }
    };
    fetchData();
  }, [articleId]);

  return (
    <div>
      {data ? (
        <div>
          <h1>{data.title}</h1>
          <p>{data.user_id}</p>
          <p>{data.createdAt}</p>
          <p>{data.viewCount}</p>
          <p>{data.content}</p>
        </div>
      ) : (
        <div>Loading...</div>
      )}
    </div>
  );
};

export default BoardDetail;
