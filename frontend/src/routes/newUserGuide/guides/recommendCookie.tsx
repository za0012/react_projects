import CookieRecommend from "@/pages/newUserGuide/guides/CookieRecommend";
import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/newUserGuide/guides/recommendCookie")({
  component: CookieRecommend,
});
