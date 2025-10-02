import PetManagement from "@/pages/management/pet/PetManagement";
import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/management/petMan")({
  component: PetManagement,
});
