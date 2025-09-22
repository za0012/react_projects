import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/strategy/cookietier")({
  component: RouteComponent,
});

function RouteComponent() {
  return <div>흥</div>;
}
