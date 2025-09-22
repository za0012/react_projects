import AboutGem from "@/pages/newUserGuide/guides/AboutGem";
import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/newUserGuide/guides/aboutGem")({
  component: AboutGem,
});
