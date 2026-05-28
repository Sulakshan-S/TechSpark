import React from "react";
import { useEffect, useState } from "react";
import EmptyState from "../../components/common/EmptyState";
import LoadingState from "../../components/common/LoadingState";
import SectionTitle from "../../components/common/SectionTitle";
import { formatCurrency, formatDateTime } from "../../lib/utils";
import { userService } from "../../services/userService";

export default function OrdersPage() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  const load = async () => {
    setLoading(true);
    const data = await userService.getMyOrders();
    setOrders(data);
    setLoading(false);
  };

  useEffect(() => { load(); }, []);

  if (loading) return <div className="container-shell py-12"><LoadingState text="Loading orders..." /></div>;

  return (
    <div className="container-shell py-12">
      <SectionTitle eyebrow="Orders" title="Order history" description="Track your placed orders and status changes." />
      <div className="mt-8">
        {!orders.length ? (
          <EmptyState title="No orders yet" description="Place your first order from the cart or checkout page." />
        ) : (
          <div className="space-y-4">
            {orders.map((order) => (
              <div key={order.orderId} className="card p-6">
                <div className="flex flex-col gap-4 md:flex-row md:items-center md:justify-between">
                  <div>
                    <p className="text-lg font-semibold">Order #{order.orderId}</p>
                    <p className="text-sm text-slate-400">{formatDateTime(order.createdAt)}</p>
                  </div>
                  <div className="flex items-center gap-3">
                    <span className="badge">{order.status}</span>
                    <span className="text-xl font-bold text-white">{formatCurrency(order.grandTotal)}</span>
                  </div>
                </div>
                <div className="mt-4 grid gap-3 text-sm text-slate-400">
                  {order.items?.map((item) => (
                    <div key={item.orderItemId} className="flex justify-between gap-4">
                      <span>{item.productName} • {item.variantName} x {item.quantity}</span>
                      <span>{formatCurrency(item.lineTotal)}</span>
                    </div>
                  ))}
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
