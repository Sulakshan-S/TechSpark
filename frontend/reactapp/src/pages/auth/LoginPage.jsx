import React from "react";
import { motion } from "framer-motion";
import { useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { useAuth } from "../../contexts/AuthContext";

export default function LoginPage() {
  const { login } = useAuth();
  const [form, setForm] = useState({ email: "", password: "" });
  const [submitting, setSubmitting] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();

  const onSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      await login(form);
      const destination = location.state?.from?.pathname || "/";
      navigate(destination, { replace: true });
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="container-shell grid min-h-[75vh] items-center py-12 lg:grid-cols-[1.02fr_0.98fr] lg:gap-10">
      <div className="hidden lg:block">
        <p className="section-kicker">Buyer account</p>
        <h1 className="mt-4 text-4xl font-bold text-slate-950">Sign in to continue shopping, tracking orders, and managing your account.</h1>
        <p className="mt-5 max-w-xl text-base leading-7 text-slate-500">
          A cleaner marketplace-style login flow connected directly to your backend authentication and protected routes.
        </p>
      </div>

      <motion.form onSubmit={onSubmit} initial={{ opacity: 0, y: 16 }} animate={{ opacity: 1, y: 0 }} className="w-full max-w-xl rounded-[30px] border border-slate-200 bg-white p-8 shadow-[0_16px_42px_rgba(15,23,42,0.08)] lg:justify-self-end">
        <p className="section-kicker">Welcome back</p>
        <h2 className="mt-3 text-3xl font-bold text-slate-950">Login</h2>
        <div className="mt-8 space-y-4">
          <input className="input rounded-2xl" placeholder="Email address" type="email" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} />
          <input className="input rounded-2xl" placeholder="Password" type="password" value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} />
        </div>
        <button disabled={submitting} className="btn-primary mt-6 w-full rounded-full">{submitting ? "Logging in..." : "Login"}</button>
        <p className="mt-4 text-center text-sm text-slate-500">
          New here? <Link to="/register" className="font-medium text-orange-600 hover:text-orange-700">Create an account</Link>
        </p>
      </motion.form>
    </div>
  );
}
