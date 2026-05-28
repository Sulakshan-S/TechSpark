import React from "react";

export default function LoadingState({ text = "Loading..." }) {
  return (
    <div className="card flex min-h-[220px] items-center justify-center p-10 text-sm uppercase tracking-[0.2em] text-slate-400">
      {text}
    </div>
  );
}
