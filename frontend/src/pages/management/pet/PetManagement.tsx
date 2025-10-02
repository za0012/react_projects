import Sidebar from "@/components/Sidebar";
import { Link, useNavigate } from "@tanstack/react-router";
import React from "react";

const PetManagement = () => {
  const navigate = useNavigate();
  return (
    <div className="flex">
      <Sidebar />
      <div className="flex">
        <Link to="/management/pet/add">펫 추가</Link>
      </div>
    </div>
  );
};

export default PetManagement;
