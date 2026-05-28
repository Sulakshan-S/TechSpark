import React from "react";
import { motion } from "framer-motion";
import { ShieldCheck, Boxes, BadgeDollarSign, Truck, ChevronRight } from "lucide-react";
import { Link } from "react-router-dom";
import { useEffect, useState } from "react";
import SectionTitle from "../../components/common/SectionTitle";
import ProductCard from "../../components/shop/ProductCard";
import { storeService } from "../../services/storeService";
import { userService } from "../../services/userService";
import { useAuth } from "../../contexts/AuthContext";

const highlights = [
  { icon: Truck, title: "Fast storefront flow", desc: "Marketplace-style discovery, filtering, and quick cart actions for shoppers." },
  { icon: Boxes, title: "Variant-driven catalog", desc: "Product variants, images, and live inventory make catalog browsing feel complete." },
  { icon: BadgeDollarSign, title: "Coupons and pricing", desc: "Discount pricing, coupons, and totals supported by your backend APIs." },
  { icon: ShieldCheck, title: "Business admin", desc: "A cleaner admin layer for product operations, inventory, and orders." },
];

export default function HomePage() {
  const [products, setProducts] = useState([]);
  const [images, setImages] = useState({});
  const [variantPrices, setVariantPrices] = useState({});
  const { isAuthenticated } = useAuth();

  useEffect(() => {
    const load = async () => {
      const productList = await storeService.getProducts();
      const featured = productList.slice(0, 8);
      setProducts(featured);

      const imageEntries = await Promise.all(
        featured.map(async (item) => {
          try {
            const img = await storeService.getProductPrimaryImage(item.productId);
            return [item.productId, img?.url];
          } catch {
            return [item.productId, null];
          }
        })
      );
      setImages(Object.fromEntries(imageEntries));

      const priceEntries = await Promise.all(
        featured.map(async (item) => {
          try {
            const variants = await storeService.getProductVariants(item.productId);
            const minPrice = variants.length
              ? Math.min(...variants.map((v) => Number(v.discountPrice || v.price || 0)))
              : null;
            return [item.productId, minPrice];
          } catch {
            return [item.productId, null];
          }
        })
      );
      setVariantPrices(Object.fromEntries(priceEntries));
    };

    load();
  }, []);

  const addToWishlist = async (product) => {
    if (!isAuthenticated) {
      window.alert("Please login first");
      return;
    }
    await userService.addWishlist({ productId: product.productId });
    window.alert("Added to wishlist");
  };

  return (
    <div>
      <section className="bg-[#fff7f1]">
        <div className="container-shell grid gap-8 py-10 lg:grid-cols-[1.45fr_0.95fr] lg:items-stretch lg:py-12">
          <div className="overflow-hidden rounded-[32px] bg-[linear-gradient(135deg,#ff6a00_0%,#ff8f1f_45%,#ffb258_100%)] p-8 text-white shadow-[0_18px_50px_rgba(249,115,22,0.30)] lg:p-10">
            <motion.p initial={{ opacity: 0, y: 16 }} animate={{ opacity: 1, y: 0 }} className="text-xs font-semibold uppercase tracking-[0.3em] text-orange-100">
              Marketplace electronics
            </motion.p>
            <motion.h1 initial={{ opacity: 0, y: 16 }} animate={{ opacity: 1, y: 0 }} transition={{ delay: 0.06 }} className="mt-4 max-w-3xl text-4xl font-bold leading-tight sm:text-5xl lg:text-6xl">
              Clean ecommerce UI that feels closer to a real online marketplace.
            </motion.h1>
            <motion.p initial={{ opacity: 0, y: 16 }} animate={{ opacity: 1, y: 0 }} transition={{ delay: 0.12 }} className="mt-5 max-w-2xl text-base leading-7 text-orange-50/90 sm:text-lg">
              Browse products, variants, pricing, cart, checkout, and orders through a storefront styled more like AliExpress, but cleaner and more professional.
            </motion.p>
            <motion.div initial={{ opacity: 0, y: 16 }} animate={{ opacity: 1, y: 0 }} transition={{ delay: 0.18 }} className="mt-8 flex flex-wrap gap-3">
              <Link to="/shop" className="inline-flex items-center gap-2 rounded-full bg-slate-950 px-6 py-3 text-sm font-semibold text-white hover:bg-slate-900">Shop now <ChevronRight size={16} /></Link>
              <Link to="/admin" className="inline-flex items-center gap-2 rounded-full border border-white/50 bg-white/15 px-6 py-3 text-sm font-semibold text-white hover:bg-white/20">Open admin</Link>
            </motion.div>
            <div className="mt-8 grid gap-3 sm:grid-cols-3">
              {[["Live catalog", "Products and variants"], ["Order flow", "Cart to delivery"], ["Admin tools", "Inventory and pricing"]].map(([label, value]) => (
                <div key={label} className="rounded-2xl border border-white/20 bg-white/10 p-4 backdrop-blur-sm">
                  <p className="text-xs uppercase tracking-[0.18em] text-orange-100">{label}</p>
                  <p className="mt-2 text-sm font-medium text-white">{value}</p>
                </div>
              ))}
            </div>
          </div>

          <div className="grid gap-4">
            <div className="rounded-[28px] border border-slate-200 bg-white p-6 shadow-[0_10px_35px_rgba(15,23,42,0.06)]">
              <p className="section-kicker">Today’s highlights</p>
              <div className="mt-4 space-y-4">
                {highlights.map(({ icon: Icon, title, desc }) => (
                  <div key={title} className="flex items-start gap-4 rounded-2xl border border-slate-100 bg-slate-50 p-4">
                    <div className="mt-0.5 flex h-11 w-11 items-center justify-center rounded-2xl bg-orange-50 text-orange-600">
                      <Icon size={18} />
                    </div>
                    <div>
                      <h3 className="text-base font-semibold text-slate-950">{title}</h3>
                      <p className="mt-1 text-sm leading-6 text-slate-500">{desc}</p>
                    </div>
                  </div>
                ))}
              </div>
            </div>
            <div className="grid gap-4 sm:grid-cols-2">
              <div className="rounded-[28px] bg-slate-950 p-6 text-white">
                <p className="text-xs uppercase tracking-[0.22em] text-slate-400">Member perks</p>
                <p className="mt-3 text-2xl font-bold">Secure account area</p>
                <p className="mt-2 text-sm leading-6 text-slate-300">Wishlist, profile, addresses, and orders connected to authentication.</p>
              </div>
              <div className="rounded-[28px] border border-slate-200 bg-white p-6">
                <p className="text-xs uppercase tracking-[0.22em] text-slate-400">Admin operations</p>
                <p className="mt-3 text-2xl font-bold text-slate-950">Catalog control</p>
                <p className="mt-2 text-sm leading-6 text-slate-500">Manage products, images, coupons, orders, and stock from one panel.</p>
              </div>
            </div>
          </div>
        </div>
      </section>

      <section className="container-shell py-14">
        <SectionTitle
          eyebrow="Popular products"
          title="Featured deals from your backend"
          description="These products come from your live backend endpoints, including image and variant pricing support."
          action={<Link to="/shop" className="btn-secondary rounded-full">View all products</Link>}
        />

        <div className="mt-8 grid gap-6 md:grid-cols-2 xl:grid-cols-4">
          {products.map((product) => (
            <ProductCard
              key={product.productId}
              product={product}
              image={images[product.productId]}
              priceRange={variantPrices[product.productId]}
              onWishlist={addToWishlist}
            />
          ))}
        </div>
      </section>
    </div>
  );
}
