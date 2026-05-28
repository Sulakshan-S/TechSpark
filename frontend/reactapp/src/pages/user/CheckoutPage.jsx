import React from "react";
import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import SectionTitle from "../../components/common/SectionTitle";
import { useCart } from "../../contexts/CartContext";
import { formatCurrency } from "../../lib/utils";
import { storeService } from "../../services/storeService";
import { userService } from "../../services/userService";

export default function CheckoutPage() {
  const navigate = useNavigate();
  const { cart, refreshCart } = useCart();
  const [addresses, setAddresses] = useState([]);
  const [shippingAddressId, setShippingAddressId] = useState("");
  const [couponCode, setCouponCode] = useState("");
  const [couponPreview, setCouponPreview] = useState(null);
  const [placing, setPlacing] = useState(false);

  useEffect(() => {
    const load = async () => {
      await refreshCart();
      const data = await userService.getAddresses();
      setAddresses(data);
      const defaultAddress = data.find((item) => (item.isDefault ?? item.default)) || data[0];
      if (defaultAddress) setShippingAddressId(String(defaultAddress.addressId));
    };
    load();
  }, []);

  const subtotal = Number(cart?.totalAmount || 0);
  const shippingFee = 0;
  const discountAmount = Number(couponPreview?.discountAmount || 0);
  const grandTotal = subtotal - discountAmount + shippingFee;

  const applyCoupon = async () => {
    if (!couponCode.trim()) return;
    const result = await storeService.validateCoupon(couponCode.trim(), subtotal);
    setCouponPreview(result);
  };

  const placeOrder = async () => {
    setPlacing(true);
    try {
      const response = await userService.placeOrder({
        shippingAddressId: Number(shippingAddressId),
        couponCode: couponCode.trim() || null,
      });
      await refreshCart();
      navigate("/orders", { state: { justPlaced: response.orderId } });
    } finally {
      setPlacing(false);
    }
  };

  return (
    <div className="container-shell py-12">
      <SectionTitle eyebrow="Checkout" title="Place your order" description="Your backend calculates subtotal, discount, shipping fee, and grand total." />

      <div className="mt-8 grid gap-8 lg:grid-cols-[1fr_380px]">
        <div className="space-y-6">
          <div className="card p-6">
            <h3 className="text-xl font-semibold">Select shipping address</h3>
            <div className="mt-4 grid gap-3">
              {addresses.map((item) => (
                <label key={item.addressId} className={`rounded-2xl border p-4 ${String(item.addressId) === shippingAddressId ? "border-sky-500 bg-sky-500/10" : "border-slate-700"}`}>
                  <div className="flex gap-3">
                    <input type="radio" checked={String(item.addressId) === shippingAddressId} onChange={() => setShippingAddressId(String(item.addressId))} />
                    <div>
                      <p className="font-semibold">{item.fullName}</p>
                      <p className="text-sm text-slate-400">{item.line1}, {item.city}, {item.district}</p>
                    </div>
                  </div>
                </label>
              ))}
            </div>
          </div>

          <div className="card p-6">
            <h3 className="text-xl font-semibold">Coupon</h3>
            <div className="mt-4 flex flex-col gap-3 sm:flex-row">
              <input value={couponCode} onChange={(e) => setCouponCode(e.target.value)} className="input flex-1" placeholder="Enter coupon code" />
              <button onClick={applyCoupon} className="btn-secondary">Validate</button>
            </div>
            {couponPreview && (
              <div className="mt-4 rounded-2xl border border-slate-800 bg-slate-950 p-4 text-sm">
                <p className="font-semibold text-white">{couponPreview.message}</p>
                <p className="mt-2 text-slate-400">Discount: {formatCurrency(couponPreview.discountAmount)}</p>
              </div>
            )}
          </div>
        </div>

        <div className="card h-fit p-6">
          <h3 className="text-xl font-semibold">Order summary</h3>
          <div className="mt-6 space-y-3 text-sm">
            {cart?.items?.map((item) => (
              <div key={item.cartItemId} className="flex justify-between gap-4 text-slate-400">
                <span>{item.productName} x {item.quantity}</span>
                <span>{formatCurrency(item.subTotal)}</span>
              </div>
            ))}
            <div className="flex justify-between text-slate-400"><span>Subtotal</span><span>{formatCurrency(subtotal)}</span></div>
            <div className="flex justify-between text-slate-400"><span>Discount</span><span>- {formatCurrency(discountAmount)}</span></div>
            <div className="flex justify-between text-slate-400"><span>Shipping Fee</span><span>{formatCurrency(shippingFee)}</span></div>
            <div className="border-t border-slate-800 pt-3 text-lg font-bold flex justify-between"><span>Grand Total</span><span>{formatCurrency(grandTotal)}</span></div>
          </div>
          <button disabled={!shippingAddressId || placing} onClick={placeOrder} className="btn-primary mt-6 w-full">
            {placing ? "Placing order..." : "Place Order"}
          </button>
        </div>
      </div>
    </div>
  );
}
