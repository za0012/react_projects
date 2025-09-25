import PetManagement from "@/pages/management/PetManagement";
import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/management/pet")({
  component: PetManagement,
});
