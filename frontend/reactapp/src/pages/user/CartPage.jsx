import React from "react";
import { Trash2 } from "lucide-react";
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import EmptyState from "../../components/common/EmptyState";
import LoadingState from "../../components/common/LoadingState";
import SectionTitle from "../../components/common/SectionTitle";
import { useCart } from "../../contexts/CartContext";
import { formatCurrency } from "../../lib/utils";
import { userService } from "../../services/userService";

export default function CartPage() {
  const { cart, refreshCart } = useCart();
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const load = async () => {
      setLoading(true);
      await refreshCart();
      setLoading(false);
    };
    load();
  }, []);

  const updateQuantity = async (item, quantity) => {
    await userService.updateCartItem(item.cartItemId, { quantity: Number(quantity) });
    await refreshCart();
  };

  const removeItem = async (id) => {
    await userService.removeCartItem(id);
    await refreshCart();
  };

  if (loading) return <div className="container-shell py-12"><LoadingState text="Loading cart..." /></div>;

  return (
    <div className="container-shell py-12">
      <SectionTitle eyebrow="Cart" title="Your shopping cart" description="Adjust quantities before checkout." />
      <div className="mt-8 grid gap-8 lg:grid-cols-[1fr_380px]">
        <div>
          {!cart?.items?.length ? (
            <EmptyState title="Cart is empty" description="Add a product variant to continue." action={<Link to="/shop" className="btn-primary">Go to shop</Link>} />
          ) : (
            <div className="space-y-4">
              {cart.items.map((item) => (
                <div key={item.cartItemId} className="card flex flex-col gap-4 p-5 md:flex-row md:items-center md:justify-between">
                  <div>
                    <p className="text-lg font-semibold">{item.productName}</p>
                    <p className="mt-1 text-sm text-slate-400">{item.variantName} • SKU: {item.sku}</p>
                    <p className="mt-2 font-bold text-sky-400">{formatCurrency(item.unitPrice)}</p>
                  </div>
                  <div className="flex flex-wrap items-center gap-3">
                    <input
                      type="number"
                      min="1"
                      className="input w-24"
                      value={item.quantity}
                      onChange={(e) => updateQuantity(item, e.target.value)}
                    />
                    <p className="min-w-28 text-right font-semibold">{formatCurrency(item.subTotal)}</p>
                    <button onClick={() => removeItem(item.cartItemId)} className="btn-secondary">
                      <Trash2 size={16} />
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        <div className="card h-fit p-6">
          <h3 className="text-xl font-semibold">Summary</h3>
          <div className="mt-6 space-y-4 text-sm">
            <div className="flex justify-between text-slate-400"><span>Items</span><span>{cart?.totalItems || 0}</span></div>
            <div className="flex justify-between text-lg font-bold text-white"><span>Total</span><span>{formatCurrency(cart?.totalAmount || 0)}</span></div>
          </div>
          <Link to="/checkout" className="btn-primary mt-6 w-full">Proceed to Checkout</Link>
        </div>
      </div>
    </div>
  );
}
