import React from "react";
import { useEffect, useState } from "react";
import SectionTitle from "../../components/common/SectionTitle";
import { formatCurrency, formatDateTime } from "../../lib/utils";
import { adminService } from "../../services/adminService";

export default function AdminOrdersPage() {
  const [orders, setOrders] = useState([]);
  const [selectedStatus, setSelectedStatus] = useState({});

  const load = async () => {
    const data = await adminService.list("/api/orders");
    setOrders(data);
    setSelectedStatus(Object.fromEntries(data.map((item) => [item.orderId, item.status])));
  };

  useEffect(() => { load(); }, []);

  const updateStatus = async (orderId) => {
    await adminService.patch(`/api/orders/${orderId}/status`, { status: selectedStatus[orderId], note: "Updated from admin panel" });
    load();
  };

  return (
    <div>
      <SectionTitle eyebrow="Order operations" title="Orders" description="Review customer orders and update order status from a cleaner admin workflow." />
      <div className="mt-8 space-y-4">
        {orders.map((order) => (
          <div key={order.orderId} className="rounded-[28px] border border-slate-200 bg-white p-6 shadow-[0_10px_35px_rgba(15,23,42,0.05)]">
            <div className="flex flex-col gap-4 xl:flex-row xl:items-start xl:justify-between">
              <div>
                <div className="flex flex-wrap items-center gap-3">
                  <h3 className="text-xl font-bold text-slate-950">Order #{order.orderId}</h3>
                  <span className="badge">{order.status}</span>
                </div>
                <p className="mt-2 text-sm text-slate-500">{order.userName} • {order.userEmail}</p>
                <p className="text-sm text-slate-500">Placed at {formatDateTime(order.createdAt)}</p>
                <p className="mt-2 text-sm text-slate-500">Ship to: {order.shippingFullName}, {order.shippingLine1}, {order.shippingCity}</p>
              </div>

              <div className="w-full max-w-sm rounded-3xl border border-slate-200 bg-slate-50 p-4">
                <p className="text-sm font-medium text-slate-700">Update status</p>
                <div className="mt-3 flex gap-3">
                  <select className="select rounded-full" value={selectedStatus[order.orderId] || order.status} onChange={(e) => setSelectedStatus({ ...selectedStatus, [order.orderId]: e.target.value })}>
                    {["PLACED", "PAID", "PACKED", "SHIPPED", "DELIVERED", "CANCELLED", "REFUNDED"].map((status) => (
                      <option key={status} value={status}>{status}</option>
                    ))}
                  </select>
                  <button onClick={() => updateStatus(order.orderId)} className="btn-primary rounded-full">Save</button>
                </div>
                <p className="mt-4 text-2xl font-bold text-orange-600">{formatCurrency(order.grandTotal)}</p>
              </div>
            </div>

            <div className="mt-6 grid gap-3 text-sm text-slate-500">
              {order.items?.map((item) => (
                <div key={item.orderItemId} className="flex justify-between gap-4 rounded-2xl border border-slate-200 bg-slate-50 px-4 py-3">
                  <span>{item.productName} • {item.variantName} x {item.quantity}</span>
                  <span className="font-semibold text-slate-900">{formatCurrency(item.lineTotal)}</span>
                </div>
              ))}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
