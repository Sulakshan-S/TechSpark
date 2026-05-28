import React from "react";
import { ShoppingCart, Trash2 } from "lucide-react";
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import EmptyState from "../../components/common/EmptyState";
import LoadingState from "../../components/common/LoadingState";
import SectionTitle from "../../components/common/SectionTitle";
import { userService } from "../../services/userService";

export default function WishlistPage() {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);

  const load = async () => {
    setLoading(true);
    const data = await userService.getWishlist();
    setItems(data);
    setLoading(false);
  };

  useEffect(() => { load(); }, []);

  const remove = async (item) => {
    await userService.removeWishlist(item.wishlistItemId);
    load();
  };

  if (loading) return <div className="container-shell py-12"><LoadingState text="Loading wishlist..." /></div>;

  return (
    <div className="container-shell py-12">
      <SectionTitle eyebrow="Wishlist" title="Saved products" description="Come back later and move them to cart." />
      <div className="mt-8">
        {!items.length ? (
          <EmptyState title="Wishlist is empty" description="Save products you like for later." action={<Link to="/shop" className="btn-primary">Browse products</Link>} />
        ) : (
          <div className="grid gap-4">
            {items.map((item) => (
              <div key={item.wishlistItemId} className="card flex flex-col gap-4 p-5 md:flex-row md:items-center md:justify-between">
                <div>
                  <p className="text-lg font-semibold">{item.productName}</p>
                  <p className="mt-1 text-sm text-slate-400">{item.categoryName} • {item.brandName}</p>
                </div>
                <div className="flex flex-wrap gap-3">
                  <Link to={`/products/${item.productId}`} className="btn-primary"><ShoppingCart size={16} /> View</Link>
                  <button onClick={() => remove(item)} className="btn-secondary"><Trash2 size={16} /> Remove</button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
