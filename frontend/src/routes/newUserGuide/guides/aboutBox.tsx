import { createFileRoute } from "@tanstack/react-router";
import AboutGoldBox from "@/pages/newUserGuide/guides/AboutGoldBox";

export const Route = createFileRoute("/newUserGuide/guides/aboutBox")({
  component: AboutGoldBox,
});
