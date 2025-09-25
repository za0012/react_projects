import BannerManagement from "@/pages/management/BannerManagement";
import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/management/banner")({
  component: BannerManagement,
});
