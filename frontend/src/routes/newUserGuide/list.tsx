import { createFileRoute } from "@tanstack/react-router";
import GuideList from "@/pages/newUserGuide/guideList";

export const Route = createFileRoute("/newUserGuide/list")({
  component: GuideList,
});
