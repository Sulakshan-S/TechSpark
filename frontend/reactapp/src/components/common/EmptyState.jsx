import React from "react";

export default function EmptyState({ title, description, action }) {
  return (
    <div className="card p-10 text-center">
      <p className="section-kicker">No results</p>
      <h3 className="mt-2 text-xl font-semibold text-white">{title}</h3>
      <p className="mx-auto mt-3 max-w-xl text-sm leading-6 text-slate-400">{description}</p>
      {action && <div className="mt-6">{action}</div>}
    </div>
  );
}
