import AddCookie from "@/pages/management/cookie/AddCookie";
import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/management/cookie/add")({
  component: AddCookie,
});
