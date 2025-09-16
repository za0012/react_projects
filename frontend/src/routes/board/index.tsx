import { createFileRoute } from "@tanstack/react-router";
import Board from "../../pages/board/Board";

export const Route = createFileRoute("/board/")({
  component: Board,
});
