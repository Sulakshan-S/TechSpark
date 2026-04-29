import { createBrowserRouter } from "react-router-dom";

function HomePage() {
  return (
    <main className="min-h-screen bg-slate-50 px-6 py-10">
      <section className="mx-auto max-w-6xl rounded-3xl border border-slate-200 bg-white p-8 shadow-sm">
        <p className="text-sm font-medium text-indigo-600">TechSpark</p>

        <h1 className="mt-3 text-4xl font-bold tracking-tight text-slate-950">
          Frontend setup completed
        </h1>

        <p className="mt-4 max-w-2xl text-base leading-7 text-slate-600">
          React, Tailwind CSS, React Router, and React Query are ready.
          Now we can continue with the clean frontend structure.
        </p>

        <div className="mt-8 flex flex-wrap gap-3">
          <span className="rounded-full bg-slate-100 px-4 py-2 text-sm font-medium text-slate-700">
            React
          </span>
          <span className="rounded-full bg-slate-100 px-4 py-2 text-sm font-medium text-slate-700">
            Tailwind CSS
          </span>
          <span className="rounded-full bg-slate-100 px-4 py-2 text-sm font-medium text-slate-700">
            React Router
          </span>
          <span className="rounded-full bg-slate-100 px-4 py-2 text-sm font-medium text-slate-700">
            React Query
          </span>
        </div>
      </section>
    </main>
  );
}

function NotFoundPage() {
  return (
    <main className="flex min-h-screen items-center justify-center bg-slate-50 px-6">
      <div className="text-center">
        <p className="text-sm font-semibold text-indigo-600">404</p>
        <h1 className="mt-2 text-3xl font-bold text-slate-950">
          Page not found
        </h1>
        <p className="mt-3 text-slate-600">
          The page you are looking for does not exist.
        </p>
      </div>
    </main>
  );
}

export const router = createBrowserRouter([
  {
    path: "/",
    element: <HomePage />,
    errorElement: <NotFoundPage />,
  },
]);