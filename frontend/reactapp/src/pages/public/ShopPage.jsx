import React from "react";
import { useEffect, useMemo, useState } from "react";
import EmptyState from "../../components/common/EmptyState";
import LoadingState from "../../components/common/LoadingState";
import SectionTitle from "../../components/common/SectionTitle";
import ProductCard from "../../components/shop/ProductCard";
import ProductFilters from "../../components/shop/ProductFilters";
import { useAuth } from "../../contexts/AuthContext";
import { storeService } from "../../services/storeService";
import { userService } from "../../services/userService";

export default function ShopPage() {
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [brands, setBrands] = useState([]);
  const [images, setImages] = useState({});
  const [prices, setPrices] = useState({});
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState("");
  const [selectedCategory, setSelectedCategory] = useState("");
  const [selectedBrand, setSelectedBrand] = useState("");
  const { isAuthenticated } = useAuth();

  useEffect(() => {
    const load = async () => {
      setLoading(true);
      const [productData, categoryData, brandData] = await Promise.all([
        storeService.getProducts(),
        storeService.getActiveCategories(),
        storeService.getBrands(),
      ]);
      setProducts(productData);
      setCategories(categoryData);
      setBrands(brandData);

      const imageEntries = await Promise.all(
        productData.map(async (item) => {
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
        productData.map(async (item) => {
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
      setPrices(Object.fromEntries(priceEntries));
      setLoading(false);
    };

    load();
  }, []);

  const filteredProducts = useMemo(() => {
    return products.filter((product) => {
      const matchSearch = !search || product.name.toLowerCase().includes(search.toLowerCase());
      const matchCategory = !selectedCategory || String(product.categoryId) === selectedCategory;
      const matchBrand = !selectedBrand || String(product.brandId) === selectedBrand;
      return matchSearch && matchCategory && matchBrand;
    });
  }, [products, search, selectedCategory, selectedBrand]);

  const addToWishlist = async (product) => {
    if (!isAuthenticated) {
      window.alert("Please login first");
      return;
    }
    await userService.addWishlist({ productId: product.productId });
    window.alert("Added to wishlist");
  };

  return (
    <div className="container-shell py-10">
      <div className="rounded-[28px] bg-white p-6 shadow-[0_10px_34px_rgba(15,23,42,0.06)] sm:p-8">
        <SectionTitle
          eyebrow="All products"
          title="Marketplace catalog"
          description="Browse your full product catalog with category and brand filters, then open each item to review variants and pricing."
        />

        <div className="mt-7 grid gap-6 xl:grid-cols-[320px_1fr] xl:items-start">
          <div className="space-y-4">
            <ProductFilters
              search={search}
              setSearch={setSearch}
              categories={categories}
              brands={brands}
              selectedCategory={selectedCategory}
              setSelectedCategory={setSelectedCategory}
              selectedBrand={selectedBrand}
              setSelectedBrand={setSelectedBrand}
            />
            <div className="rounded-3xl border border-slate-200 bg-slate-50 p-5">
              <p className="section-kicker">Catalog summary</p>
              <div className="mt-4 grid gap-4 sm:grid-cols-3 xl:grid-cols-1">
                <div>
                  <p className="text-sm text-slate-500">Products</p>
                  <p className="mt-1 text-2xl font-bold text-slate-950">{products.length}</p>
                </div>
                <div>
                  <p className="text-sm text-slate-500">Categories</p>
                  <p className="mt-1 text-2xl font-bold text-slate-950">{categories.length}</p>
                </div>
                <div>
                  <p className="text-sm text-slate-500">Brands</p>
                  <p className="mt-1 text-2xl font-bold text-slate-950">{brands.length}</p>
                </div>
              </div>
            </div>
          </div>

          <div>
            {loading ? (
              <LoadingState text="Loading catalog" />
            ) : filteredProducts.length ? (
              <div className="grid gap-6 md:grid-cols-2 2xl:grid-cols-3">
                {filteredProducts.map((product) => (
                  <ProductCard
                    key={product.productId}
                    product={product}
                    image={images[product.productId]}
                    priceRange={prices[product.productId]}
                    onWishlist={addToWishlist}
                  />
                ))}
              </div>
            ) : (
              <EmptyState title="No products found" description="Try a different keyword or clear one of the catalog filters." />
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
