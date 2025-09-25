import Inquiry from "@/pages/management/Inquiry";
import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/management/inquiry")({
  component: Inquiry,
});
