import React from "react";
import { motion } from "framer-motion";
import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../../contexts/AuthContext";

export default function RegisterPage() {
  const { register } = useAuth();
  const [form, setForm] = useState({ fullName: "", email: "", password: "", phone: "" });
  const [submitting, setSubmitting] = useState(false);
  const navigate = useNavigate();

  const onSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      await register(form);
      navigate("/");
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="container-shell grid min-h-[75vh] items-center py-12 lg:grid-cols-[1.02fr_0.98fr] lg:gap-10">
      <div className="hidden lg:block">
        <p className="section-kicker">Create account</p>
        <h1 className="mt-4 text-4xl font-bold text-slate-950">Register for checkout, addresses, wishlist, and order tracking.</h1>
        <p className="mt-5 max-w-xl text-base leading-7 text-slate-500">
          This customer signup flow stays simple, professional, and aligned with your backend registration endpoint.
        </p>
      </div>

      <motion.form onSubmit={onSubmit} initial={{ opacity: 0, y: 16 }} animate={{ opacity: 1, y: 0 }} className="w-full max-w-xl rounded-[30px] border border-slate-200 bg-white p-8 shadow-[0_16px_42px_rgba(15,23,42,0.08)] lg:justify-self-end">
        <p className="section-kicker">Get started</p>
        <h2 className="mt-3 text-3xl font-bold text-slate-950">Create account</h2>
        <div className="mt-8 grid gap-4 sm:grid-cols-2">
          <input className="input rounded-2xl sm:col-span-2" placeholder="Full name" value={form.fullName} onChange={(e) => setForm({ ...form, fullName: e.target.value })} />
          <input className="input rounded-2xl sm:col-span-2" placeholder="Email address" type="email" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} />
          <input className="input rounded-2xl" placeholder="Phone number" value={form.phone} onChange={(e) => setForm({ ...form, phone: e.target.value })} />
          <input className="input rounded-2xl" placeholder="Password" type="password" value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} />
        </div>
        <button disabled={submitting} className="btn-primary mt-6 w-full rounded-full">{submitting ? "Creating account..." : "Register"}</button>
        <p className="mt-4 text-center text-sm text-slate-500">
          Already have an account? <Link to="/login" className="font-medium text-orange-600 hover:text-orange-700">Login</Link>
        </p>
      </motion.form>
    </div>
  );
}
