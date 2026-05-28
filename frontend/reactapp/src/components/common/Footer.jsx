import React from "react";
import { Link } from "react-router-dom";

export default function Footer() {
  return (
    <footer className="mt-16 border-t border-slate-200 bg-white">
      <div className="container-shell grid gap-10 py-12 md:grid-cols-[1.2fr_0.8fr_0.8fr_0.8fr]">
        <div>
          <p className="section-kicker">TechSpark Marketplace</p>
          <h3 className="mt-3 text-xl font-bold text-slate-950">Professional ecommerce UI inspired by large marketplaces.</h3>
          <p className="mt-4 max-w-md text-sm leading-6 text-slate-500">
            Connected to your Spring Boot backend for products, variants, cart, checkout, orders, inventory, coupons, and administration.
          </p>
        </div>
        <div>
          <h4 className="text-sm font-semibold uppercase tracking-[0.18em] text-slate-700">Shop</h4>
          <div className="mt-4 space-y-3 text-sm text-slate-500">
            <Link to="/shop" className="block hover:text-slate-950">All Products</Link>
            <Link to="/cart" className="block hover:text-slate-950">Cart</Link>
            <Link to="/wishlist" className="block hover:text-slate-950">Wishlist</Link>
          </div>
        </div>
        <div>
          <h4 className="text-sm font-semibold uppercase tracking-[0.18em] text-slate-700">Account</h4>
          <div className="mt-4 space-y-3 text-sm text-slate-500">
            <Link to="/login" className="block hover:text-slate-950">Login</Link>
            <Link to="/register" className="block hover:text-slate-950">Register</Link>
            <Link to="/orders" className="block hover:text-slate-950">Orders</Link>
          </div>
        </div>
        <div>
          <h4 className="text-sm font-semibold uppercase tracking-[0.18em] text-slate-700">Business</h4>
          <div className="mt-4 space-y-3 text-sm text-slate-500">
            <Link to="/admin" className="block hover:text-slate-950">Admin Panel</Link>
            <p>Catalog operations</p>
            <p>Order management</p>
          </div>
        </div>
      </div>
    </footer>
  );
}
