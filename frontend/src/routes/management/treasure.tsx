import { createFileRoute } from "@tanstack/react-router";
import TreasureManagement from "@/pages/management/TreasureManagement";

export const Route = createFileRoute("/management/treasure")({
  component: TreasureManagement,
});
