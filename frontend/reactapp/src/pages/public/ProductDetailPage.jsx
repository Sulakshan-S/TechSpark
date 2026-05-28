import React from "react";
import { Heart, ShoppingCart } from "lucide-react";
import { useEffect, useMemo, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import LoadingState from "../../components/common/LoadingState";
import { useAuth } from "../../contexts/AuthContext";
import { formatCurrency } from "../../lib/utils";
import { storeService } from "../../services/storeService";
import { userService } from "../../services/userService";
import { useCart } from "../../contexts/CartContext";

export default function ProductDetailPage() {
  const { productId } = useParams();
  const navigate = useNavigate();
  const { isAuthenticated } = useAuth();
  const { refreshCart } = useCart();
  const [loading, setLoading] = useState(true);
  const [product, setProduct] = useState(null);
  const [images, setImages] = useState([]);
  const [variants, setVariants] = useState([]);
  const [variantMeta, setVariantMeta] = useState({});
  const [selectedVariantId, setSelectedVariantId] = useState("");
  const [quantity, setQuantity] = useState(1);
  const [activeImage, setActiveImage] = useState(0);

  useEffect(() => {
    const load = async () => {
      setLoading(true);
      const [productData, imageData, variantData] = await Promise.all([
        storeService.getProduct(productId),
        storeService.getProductImages(productId).catch(() => []),
        storeService.getProductVariants(productId).catch(() => []),
      ]);
      setProduct(productData);
      setImages(imageData);
      setVariants(variantData);
      setSelectedVariantId(variantData[0]?.productVariantId ? String(variantData[0].productVariantId) : "");
      setActiveImage(0);

      const metaEntries = await Promise.all(
        variantData.map(async (variant) => {
          try {
            const values = await storeService.getVariantValuesByProductVariant(variant.productVariantId);
            return [variant.productVariantId, values];
          } catch {
            return [variant.productVariantId, []];
          }
        })
      );
      setVariantMeta(Object.fromEntries(metaEntries));
      setLoading(false);
    };

    load();
  }, [productId]);

  const selectedVariant = useMemo(
    () => variants.find((item) => String(item.productVariantId) === selectedVariantId),
    [variants, selectedVariantId]
  );

  const addToCart = async () => {
    if (!isAuthenticated) {
      navigate("/login");
      return;
    }
    if (!selectedVariant) {
      window.alert("Please select a variant");
      return;
    }
    await userService.addToCart({ productVariantId: selectedVariant.productVariantId, quantity });
    await refreshCart();
    window.alert("Added to cart");
  };

  const addToWishlist = async () => {
    if (!isAuthenticated) {
      navigate("/login");
      return;
    }
    await userService.addWishlist({ productId: Number(productId) });
    window.alert("Added to wishlist");
  };

  if (loading) return <div className="container-shell py-12"><LoadingState text="Loading product details" /></div>;
  if (!product) return null;

  const displayImage = images[activeImage]?.url || images[0]?.url || "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?auto=format&fit=crop&w=900&q=80";

  return (
    <div className="container-shell py-12">
      <div className="grid gap-8 xl:grid-cols-[1.05fr_0.95fr]">
        <div className="space-y-4">
          <div className="overflow-hidden rounded-2xl border border-slate-800 bg-slate-950">
            <img src={displayImage} alt={product.name} className="h-[460px] w-full object-cover" />
          </div>
          {!!images.length && (
            <div className="grid grid-cols-4 gap-3 lg:grid-cols-5">
              {images.map((img, index) => (
                <button
                  key={img.productImageId}
                  onClick={() => setActiveImage(index)}
                  className={`overflow-hidden rounded-xl border ${index === activeImage ? "border-blue-500" : "border-slate-800"}`}
                >
                  <img src={img.url} alt={product.name} className="h-20 w-full object-cover" />
                </button>
              ))}
            </div>
          )}
        </div>

        <div className="space-y-6">
          <div className="card p-6">
            <p className="section-kicker">{product.brandName || "Catalog item"}</p>
            <h1 className="mt-2 text-3xl font-semibold text-white sm:text-4xl">{product.name}</h1>
            <p className="mt-4 text-sm leading-7 text-slate-400">{product.description || "No product description has been added yet."}</p>

            <div className="mt-6 grid gap-4 border-t border-slate-800 pt-6 sm:grid-cols-2">
              <div className="card-subtle p-4">
                <p className="text-xs uppercase tracking-[0.16em] text-slate-500">Current price</p>
                <p className="mt-2 text-3xl font-semibold text-white">
                  {selectedVariant ? formatCurrency(selectedVariant.discountPrice || selectedVariant.price) : "Choose a variant"}
                </p>
              </div>
              <div className="card-subtle p-4">
                <p className="text-xs uppercase tracking-[0.16em] text-slate-500">Selected SKU</p>
                <p className="mt-2 text-lg font-medium text-slate-200">{selectedVariant?.sku || "Not selected"}</p>
              </div>
            </div>
          </div>

          <div className="card p-6">
            <div className="flex items-center justify-between gap-4 border-b border-slate-800 pb-4">
              <div>
                <p className="section-kicker">Variant selection</p>
                <h2 className="mt-2 text-xl font-semibold text-white">Choose configuration</h2>
              </div>
              <span className="badge">{variants.length} options</span>
            </div>

            <div className="mt-5 grid gap-3">
              {variants.map((variant) => (
                <button
                  key={variant.productVariantId}
                  onClick={() => setSelectedVariantId(String(variant.productVariantId))}
                  className={`rounded-xl border p-4 text-left transition ${
                    String(variant.productVariantId) === selectedVariantId
                      ? "border-blue-500 bg-blue-600/10"
                      : "border-slate-700 bg-slate-950 hover:border-slate-500"
                  }`}
                >
                  <div className="flex flex-wrap items-start justify-between gap-3">
                    <div>
                      <p className="font-medium text-white">{variant.variantName}</p>
                      <p className="mt-1 text-xs uppercase tracking-[0.12em] text-slate-500">SKU: {variant.sku}</p>
                    </div>
                    <p className="text-lg font-semibold text-white">{formatCurrency(variant.discountPrice || variant.price)}</p>
                  </div>
                  {!!variantMeta[variant.productVariantId]?.length && (
                    <div className="mt-3 flex flex-wrap gap-2">
                      {variantMeta[variant.productVariantId].map((item) => (
                        <span key={item.variantAttributeValueId} className="badge">
                          {item.variantAttributeName}: {item.value}
                        </span>
                      ))}
                    </div>
                  )}
                </button>
              ))}
            </div>

            <div className="mt-6 flex flex-wrap items-center gap-4 border-t border-slate-800 pt-6">
              <input
                type="number"
                min="1"
                value={quantity}
                onChange={(e) => setQuantity(Number(e.target.value))}
                className="input max-w-28"
              />
              <button onClick={addToCart} className="btn-primary flex-1">
                <ShoppingCart size={18} />
                Add to cart
              </button>
              <button onClick={addToWishlist} className="btn-secondary">
                <Heart size={18} />
                Wishlist
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
