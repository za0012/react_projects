import CookieManagement from "@/pages/management/cookie/CookieManagement";
import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/management/cookieMan")({
  component: CookieManagement,
});
