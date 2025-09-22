import { createFileRoute } from "@tanstack/react-router";
import CookieDetail from "@/pages/cookie/CookieDetail";

export const Route = createFileRoute("/cookie/$articleId")({
  component: CookieDetail,
});
