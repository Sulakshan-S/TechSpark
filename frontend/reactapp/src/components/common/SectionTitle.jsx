import React from "react";

export default function SectionTitle({ eyebrow, title, description, action }) {
  return (
    <div className="flex flex-col gap-4 md:flex-row md:items-end md:justify-between">
      <div>
        {eyebrow && <p className="section-kicker">{eyebrow}</p>}
        <h2 className="mt-2 text-2xl font-bold text-slate-950 sm:text-3xl">{title}</h2>
        {description && <p className="mt-3 max-w-2xl text-sm leading-6 text-slate-500">{description}</p>}
      </div>
      {action}
    </div>
  );
}
