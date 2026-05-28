import React from "react";
import { Heart, Menu, ShoppingCart, Store, UserCircle2, X, Search, Grid2x2 } from "lucide-react";
import { useEffect, useState } from "react";
import { Link, NavLink, useNavigate } from "react-router-dom";
import { useAuth } from "../../contexts/AuthContext";
import { useCart } from "../../contexts/CartContext";

const navLinkClass = ({ isActive }) =>
  `text-sm font-medium transition ${isActive ? "text-orange-600" : "text-slate-600 hover:text-slate-950"}`;

export default function Navbar() {
  const [open, setOpen] = useState(false);
  const { user, logout, isAdmin, isAuthenticated } = useAuth();
  const { cart, refreshCart } = useCart();
  const navigate = useNavigate();

  useEffect(() => {
    if (isAuthenticated) refreshCart();
  }, [isAuthenticated]);

  const cartCount = cart?.totalItems || 0;

  return (
    <header className="sticky top-0 z-50 border-b border-slate-200 bg-white/95 backdrop-blur">
      <div className="border-b border-slate-100 bg-slate-50 text-xs text-slate-500">
        <div className="container-shell flex h-9 items-center justify-between gap-4">
          <p>Buyer protection • Fast checkout • Backend-connected commerce</p>
          <div className="hidden items-center gap-5 md:flex">
            <Link to="/orders" className="hover:text-slate-900">Track Orders</Link>
            <Link to="/addresses" className="hover:text-slate-900">Shipping Addresses</Link>
            {isAdmin && <Link to="/admin" className="font-medium text-orange-600">Admin Panel</Link>}
          </div>
        </div>
      </div>

      <div className="container-shell flex items-center justify-between gap-4 py-4">
        <Link to="/" className="flex items-center gap-3">
          <div className="flex h-11 w-11 items-center justify-center rounded-2xl bg-orange-500 text-white shadow-sm">
            <Store size={20} />
          </div>
          <div>
            <p className="text-xl font-bold tracking-tight text-slate-950">TechSpark</p>
            <p className="text-[11px] uppercase tracking-[0.28em] text-slate-400">Marketplace Commerce</p>
          </div>
        </Link>

        <div className="hidden flex-1 items-center gap-4 lg:flex">
          <div className="flex max-w-[720px] flex-1 items-center overflow-hidden rounded-full border-2 border-slate-900 bg-white">
            <div className="flex items-center gap-2 border-r border-slate-200 px-4 py-3 text-sm text-slate-500">
              <Grid2x2 size={16} />
              Categories
            </div>
            <div className="flex flex-1 items-center gap-3 px-4">
              <Search size={18} className="text-slate-400" />
              <input className="w-full border-0 bg-transparent py-3 text-sm outline-none placeholder:text-slate-400" placeholder="Search products, brands and collections" />
            </div>
            <button className="bg-slate-900 px-6 py-3 text-sm font-semibold text-white transition hover:bg-slate-800">Search</button>
          </div>
        </div>

        <nav className="hidden items-center gap-6 xl:flex">
          <NavLink to="/" className={navLinkClass}>Home</NavLink>
          <NavLink to="/shop" className={navLinkClass}>Shop</NavLink>
          {isAuthenticated && <NavLink to="/orders" className={navLinkClass}>My Orders</NavLink>}
          {isAdmin && <NavLink to="/admin" className={navLinkClass}>Admin</NavLink>}
        </nav>

        <div className="hidden items-center gap-2 lg:flex">
          {isAuthenticated ? (
            <>
              <Link to="/wishlist" className="relative rounded-full border border-slate-200 bg-white p-2.5 text-slate-600 hover:border-slate-300 hover:text-slate-950">
                <Heart size={18} />
              </Link>
              <Link to="/cart" className="relative rounded-full border border-slate-200 bg-white p-2.5 text-slate-600 hover:border-slate-300 hover:text-slate-950">
                <ShoppingCart size={18} />
                {cartCount > 0 && (
                  <span className="absolute -right-1 -top-1 flex h-5 min-w-5 items-center justify-center rounded-full bg-orange-500 px-1 text-[10px] font-bold text-white">
                    {cartCount}
                  </span>
                )}
              </Link>
              <button onClick={() => navigate("/profile")} className="btn-secondary rounded-full">
                <UserCircle2 size={18} />
                {user?.fullName?.split(" ")[0] || "Profile"}
              </button>
              <button onClick={logout} className="btn-primary rounded-full">Logout</button>
            </>
          ) : (
            <>
              <Link to="/login" className="btn-ghost rounded-full">Login</Link>
              <Link to="/register" className="btn-primary rounded-full">Join Free</Link>
            </>
          )}
        </div>

        <button onClick={() => setOpen((v) => !v)} className="rounded-full border border-slate-300 bg-white p-2.5 lg:hidden">
          {open ? <X size={18} /> : <Menu size={18} />}
        </button>
      </div>

      {open && (
        <div className="border-t border-slate-200 bg-white lg:hidden">
          <div className="container-shell flex flex-col gap-3 py-4">
            <Link to="/" onClick={() => setOpen(false)} className="text-sm text-slate-700">Home</Link>
            <Link to="/shop" onClick={() => setOpen(false)} className="text-sm text-slate-700">Shop</Link>
            {isAuthenticated && <Link to="/orders" onClick={() => setOpen(false)} className="text-sm text-slate-700">Orders</Link>}
            {isAuthenticated && <Link to="/wishlist" onClick={() => setOpen(false)} className="text-sm text-slate-700">Wishlist</Link>}
            {isAuthenticated && <Link to="/cart" onClick={() => setOpen(false)} className="text-sm text-slate-700">Cart</Link>}
            {isAdmin && <Link to="/admin" onClick={() => setOpen(false)} className="text-sm text-orange-600">Admin Panel</Link>}
            {!isAuthenticated ? (
              <>
                <Link to="/login" onClick={() => setOpen(false)} className="btn-secondary">Login</Link>
                <Link to="/register" onClick={() => setOpen(false)} className="btn-primary">Join Free</Link>
              </>
            ) : (
              <button onClick={logout} className="btn-primary">Logout</button>
            )}
          </div>
        </div>
      )}
    </header>
  );
}
