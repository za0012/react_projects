import { createFileRoute } from "@tanstack/react-router";
import AboutCrystal from "@/pages/newUserGuide/guides/AboutCrystal";

export const Route = createFileRoute("/newUserGuide/guides/aboutCrystal")({
  component: AboutCrystal,
});
