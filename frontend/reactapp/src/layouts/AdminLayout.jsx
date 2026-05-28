import React from "react";
import {
  LayoutDashboard,
  ShoppingBag,
  Tags,
  Shapes,
  Layers3,
  Image as ImageIcon,
  SlidersHorizontal,
  ListTree,
  Warehouse,
  ArrowLeftRight,
  TicketPercent,
  ClipboardList,
  Store,
} from "lucide-react";
import { NavLink, Outlet } from "react-router-dom";

const links = [
  { to: "/admin", label: "Overview", icon: LayoutDashboard, exact: true },
  { to: "/admin/products", label: "Products", icon: ShoppingBag },
  { to: "/admin/brands", label: "Brands", icon: Tags },
  { to: "/admin/categories", label: "Categories", icon: Shapes },
  { to: "/admin/product-variants", label: "Variants", icon: Layers3 },
  { to: "/admin/product-images", label: "Images", icon: ImageIcon },
  { to: "/admin/product-spec-attributes", label: "Specifications", icon: SlidersHorizontal },
  { to: "/admin/variant-attributes", label: "Variant attributes", icon: ListTree },
  { to: "/admin/variant-attribute-values", label: "Variant values", icon: ListTree },
  { to: "/admin/variant-inventories", label: "Inventories", icon: Warehouse },
  { to: "/admin/stock-movements", label: "Stock movements", icon: ArrowLeftRight },
  { to: "/admin/coupons", label: "Coupons", icon: TicketPercent },
  { to: "/admin/orders", label: "Orders", icon: ClipboardList },
];

export default function AdminLayout() {
  return (
    <div className="min-h-screen bg-[#f4f6f8] lg:grid lg:grid-cols-[268px_1fr]">
      <aside className="border-b border-slate-200 bg-slate-950 lg:min-h-screen lg:border-b-0 lg:border-r lg:border-slate-900">
        <div className="p-6">
          <div className="flex items-center gap-3 border-b border-slate-800 pb-6">
            <div className="flex h-11 w-11 items-center justify-center rounded-2xl bg-orange-500 text-white">
              <Store size={19} />
            </div>
            <div>
              <p className="text-base font-semibold text-white">TechSpark Admin</p>
              <p className="text-xs uppercase tracking-[0.22em] text-slate-500">Commerce Console</p>
            </div>
          </div>

          <nav className="mt-6 space-y-1.5">
            {links.map(({ to, label, icon: Icon, exact }) => (
              <NavLink
                key={to}
                to={to}
                end={exact}
                className={({ isActive }) =>
                  `flex items-center gap-3 rounded-2xl px-4 py-3 text-sm font-medium transition ${
                    isActive
                      ? "bg-white text-slate-950"
                      : "text-slate-400 hover:bg-slate-900 hover:text-white"
                  }`
                }
              >
                <Icon size={17} />
                {label}
              </NavLink>
            ))}
          </nav>
        </div>
      </aside>

      <section>
        <div className="container-shell py-8">
          <div className="mb-6 rounded-[28px] border border-slate-200 bg-white px-6 py-5 shadow-[0_10px_35px_rgba(15,23,42,0.05)]">
            <p className="section-kicker">Admin control center</p>
            <h1 className="mt-2 text-3xl font-bold text-slate-950">Commerce administration</h1>
            <p className="mt-2 text-sm text-slate-500">Manage catalog, variants, images, promotions, orders, and inventory through a cleaner business-oriented panel.</p>
          </div>
          <Outlet />
        </div>
      </section>
    </div>
  );
}
