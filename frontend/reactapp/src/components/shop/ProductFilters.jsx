import React from "react";
import { Search } from "lucide-react";

export default function ProductFilters({ search, setSearch, categories, brands, selectedCategory, setSelectedCategory, selectedBrand, setSelectedBrand }) {
  return (
    <div className="grid gap-4 rounded-3xl border border-slate-200 bg-white p-5 shadow-[0_8px_28px_rgba(15,23,42,0.05)] lg:grid-cols-[1.4fr_0.8fr_0.8fr]">
      <label className="relative block">
        <Search size={16} className="pointer-events-none absolute left-4 top-1/2 -translate-y-1/2 text-slate-400" />
        <input
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          className="input rounded-full pl-11"
          placeholder="Search products, brands, or keywords"
        />
      </label>
      <select value={selectedCategory} onChange={(e) => setSelectedCategory(e.target.value)} className="select rounded-full">
        <option value="">All categories</option>
        {categories.map((category) => (
          <option key={category.categoryId} value={category.categoryId}>{category.name}</option>
        ))}
      </select>
      <select value={selectedBrand} onChange={(e) => setSelectedBrand(e.target.value)} className="select rounded-full">
        <option value="">All brands</option>
        {brands.map((brand) => (
          <option key={brand.brandId} value={brand.brandId}>{brand.name}</option>
        ))}
      </select>
    </div>
  );
}
