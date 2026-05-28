import React from "react";
import { useEffect, useState } from "react";
import { adminService } from "../../services/adminService";
import { formatCurrency } from "../../lib/utils";

export default function DashboardPage() {
  const [stats, setStats] = useState({ products: 0, categories: 0, brands: 0, orders: 0, revenue: 0, coupons: 0 });

  useEffect(() => {
    const load = async () => {
      const [products, categories, brands, orders, coupons] = await Promise.all([
        adminService.list("/api/products"),
        adminService.list("/api/categories"),
        adminService.list("/api/brands"),
        adminService.list("/api/orders"),
        adminService.list("/api/coupons"),
      ]);
      setStats({
        products: products.length,
        categories: categories.length,
        brands: brands.length,
        orders: orders.length,
        coupons: coupons.length,
        revenue: orders.reduce((sum, order) => sum + Number(order.grandTotal || 0), 0),
      });
    };
    load();
  }, []);

  const cards = [
    ["Products", stats.products, "Catalog items available for sale"],
    ["Categories", stats.categories, "Storefront navigation groups"],
    ["Brands", stats.brands, "Brand records connected to products"],
    ["Orders", stats.orders, "Placed orders across the store"],
    ["Coupons", stats.coupons, "Promotional rules and discount codes"],
    ["Revenue", formatCurrency(stats.revenue), "Calculated from current order totals"],
  ];

  return (
    <div>
      <div className="mb-8 grid gap-4 xl:grid-cols-[1.1fr_0.9fr]">
        <div className="rounded-[28px] bg-slate-950 p-7 text-white">
          <p className="text-xs font-semibold uppercase tracking-[0.22em] text-slate-400">Overview</p>
          <h2 className="mt-3 text-3xl font-bold">Business snapshot</h2>
          <p className="mt-3 max-w-2xl text-sm leading-6 text-slate-300">A cleaner, more professional dashboard focused on operations, catalog scale, order volume, and pricing activity.</p>
        </div>
        <div className="rounded-[28px] border border-slate-200 bg-white p-7">
          <p className="text-sm text-slate-500">Current priority</p>
          <p className="mt-2 text-2xl font-bold text-slate-950">Keep products, variants, and orders aligned.</p>
          <p className="mt-3 text-sm leading-6 text-slate-500">Use the resource pages to manage catalog details and the orders page to update lifecycle status quickly.</p>
        </div>
      </div>

      <div className="grid gap-4 lg:grid-cols-2 2xl:grid-cols-3">
        {cards.map(([title, value, helper]) => (
          <div key={title} className="stat-card rounded-[28px] border-slate-200 shadow-[0_10px_35px_rgba(15,23,42,0.05)]">
            <p className="text-xs font-semibold uppercase tracking-[0.18em] text-slate-400">{title}</p>
            <p className="mt-4 text-3xl font-bold text-slate-950">{value}</p>
            <p className="mt-3 text-sm text-slate-500">{helper}</p>
          </div>
        ))}
      </div>
    </div>
  );
}
