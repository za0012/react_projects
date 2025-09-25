import { createFileRoute } from "@tanstack/react-router";
import MainIndex from "@/pages/management/MainIndex";

export const Route = createFileRoute("/management/")({
  component: MainIndex,
});
