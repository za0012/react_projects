import BoardManagement from "@/pages/management/BoardManagement";
import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/management/board")({
  component: BoardManagement,
});
