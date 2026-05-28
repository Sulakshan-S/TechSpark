import React from "react";
import { UserCircle2 } from "lucide-react";
import { useAuth } from "../../contexts/AuthContext";

export default function ProfilePage() {
  const { user } = useAuth();

  return (
    <div className="container-shell py-12">
      <div className="card mx-auto max-w-2xl p-8">
        <div className="mb-6 inline-flex rounded-3xl bg-sky-500/15 p-4 text-sky-400">
          <UserCircle2 size={34} />
        </div>
        <h1 className="text-3xl font-bold">My Profile</h1>
        <div className="mt-8 grid gap-4 sm:grid-cols-2">
          <div className="rounded-2xl border border-slate-800 p-4"><p className="text-xs text-slate-500">Full Name</p><p className="mt-2 font-semibold">{user?.fullName}</p></div>
          <div className="rounded-2xl border border-slate-800 p-4"><p className="text-xs text-slate-500">Email</p><p className="mt-2 font-semibold">{user?.email}</p></div>
          <div className="rounded-2xl border border-slate-800 p-4"><p className="text-xs text-slate-500">Role</p><p className="mt-2 font-semibold">{user?.role}</p></div>
          <div className="rounded-2xl border border-slate-800 p-4"><p className="text-xs text-slate-500">User ID</p><p className="mt-2 font-semibold">{user?.userId}</p></div>
        </div>
      </div>
    </div>
  );
}
