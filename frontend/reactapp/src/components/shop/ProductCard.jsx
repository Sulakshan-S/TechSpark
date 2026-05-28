import React from "react";
import { motion } from "framer-motion";
import { Heart, ArrowRight } from "lucide-react";
import { Link } from "react-router-dom";
import { formatCurrency } from "../../lib/utils";

export default function ProductCard({ product, image, priceRange, onWishlist }) {
  return (
    <motion.div whileHover={{ y: -3 }} transition={{ duration: 0.18 }} className="overflow-hidden rounded-3xl border border-slate-200 bg-white shadow-[0_10px_32px_rgba(15,23,42,0.06)]">
      <div className="relative aspect-[1/1] bg-[#fafafa]">
        <img
          src={image || "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?auto=format&fit=crop&w=900&q=80"}
          alt={product.name}
          className="h-full w-full object-cover"
        />
        <button
          onClick={() => onWishlist?.(product)}
          className="absolute right-4 top-4 rounded-full border border-slate-200 bg-white/95 p-2 text-slate-500 shadow-sm hover:text-orange-600"
        >
          <Heart size={17} />
        </button>
      </div>

      <div className="p-5">
        <div className="flex items-start justify-between gap-3">
          <div>
            <p className="text-[11px] font-semibold uppercase tracking-[0.24em] text-slate-400">{product.brandName || "TechSpark"}</p>
            <Link to={`/products/${product.productId}`} className="mt-2 line-clamp-2 block text-base font-semibold text-slate-950 hover:text-orange-600">
              {product.name}
            </Link>
          </div>
          <span className="badge">Hot</span>
        </div>

        <p className="mt-3 line-clamp-2 text-sm leading-6 text-slate-500">{product.description || "Check product variants, images, pricing, and specifications on the detail page."}</p>

        <div className="mt-5 flex items-end justify-between gap-4">
          <div>
            <p className="text-xs uppercase tracking-[0.16em] text-slate-400">Price from</p>
            <p className="mt-1 text-2xl font-bold leading-none text-orange-600">{priceRange ? formatCurrency(priceRange) : "See details"}</p>
          </div>
          <Link to={`/products/${product.productId}`} className="btn-secondary rounded-full">
            View
            <ArrowRight size={16} />
          </Link>
        </div>
      </div>
    </motion.div>
  );
}
