import React from "react";
import ky from "ky";

const Board = () => {
  const fetchData = async () => {
    const data = await ky
      .get(
        "http://localhost:8080/api/articles?page=0&size=10&sortBy=createdAt&sortDir=desc"
      )
      .json();
    console.log(data);
  };
  return <div>Board</div>;
};

export default Board;
