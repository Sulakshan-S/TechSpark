import React from "react";
import { Link } from "react-router-dom";

export default function NotFoundPage() {
  return (
    <div className="container-shell flex min-h-[70vh] flex-col items-center justify-center py-12 text-center">
      <p className="text-sm uppercase tracking-[0.35em] text-sky-400">404</p>
      <h1 className="mt-4 text-4xl font-bold">Page not found</h1>
      <p className="mt-4 max-w-lg text-slate-400">The page you requested does not exist.</p>
      <Link to="/" className="btn-primary mt-8">Go home</Link>
    </div>
  );
}
