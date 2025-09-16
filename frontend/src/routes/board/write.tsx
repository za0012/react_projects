import { createFileRoute } from "@tanstack/react-router";
import WriteBoard from "../../pages/board/WriteBoard";

export const Route = createFileRoute("/board/write")({
  component: WriteBoard,
});
