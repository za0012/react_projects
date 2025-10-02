import AddPets from "@/pages/management/pet/AddPets";
import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/management/pet/add")({
  component: AddPets,
});
