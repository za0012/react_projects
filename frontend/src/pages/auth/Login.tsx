import ky from "ky";
import React from "react";

const Login = () => {
  const fetchData = async () => {
    try {
      const data = await ky.post("http://localhost:8080/api/users/login");
    } catch (error) {}
  };
  return (
    <div>
      <form>
        <input type="text"></input>
        <input type="password"></input>
        <button type="submit"></button>
      </form>
    </div>
  );
};

export default Login;
