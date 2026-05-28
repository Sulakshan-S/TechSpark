import React from "react";

export default function Modal({ open, title, onClose, children }) {
  if (!open) return null;

  return (
    <div className="fixed inset-0 z-[60] flex items-center justify-center bg-slate-950/45 p-4 backdrop-blur-sm">
      <div className="w-full max-w-2xl rounded-[28px] border border-slate-200 bg-white p-6 shadow-[0_25px_60px_rgba(15,23,42,0.20)]">
        <div className="mb-6 flex items-center justify-between border-b border-slate-200 pb-4">
          <h3 className="text-lg font-bold text-slate-950">{title}</h3>
          <button onClick={onClose} className="btn-secondary rounded-full">Close</button>
        </div>
        {children}
      </div>
    </div>
  );
}
