import { createFileRoute } from "@tanstack/react-router";
import AboutGetCoin from "@/pages/newUserGuide/guides/AboutGetCoin";

export const Route = createFileRoute("/newUserGuide/guides/getCoin")({
  component: AboutGetCoin,
});
