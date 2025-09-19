import { createFileRoute } from "@tanstack/react-router";
import CookieList from "@/pages/cookie/CookieList";

export const Route = createFileRoute("/cookie/")({
  component: CookieList,
});
