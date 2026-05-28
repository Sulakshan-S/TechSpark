import api from "./api";

export const storeService = {
  getProducts: async () => (await api.get("/products")).data,
  searchProducts: async (keyword) => (await api.get(`/products/search?keyword=${encodeURIComponent(keyword)}`)).data,
  getProduct: async (productId) => (await api.get(`/products/${productId}`)).data,
  getProductsByCategory: async (categoryId) => (await api.get(`/products/category/${categoryId}`)).data,
  getProductsByBrand: async (brandId) => (await api.get(`/products/brand/${brandId}`)).data,
  getProductImages: async (productId) => (await api.get(`/product-images/product/${productId}`)).data,
  getProductPrimaryImage: async (productId) => (await api.get(`/product-images/product/${productId}/primary`)).data,
  getProductVariants: async (productId) => (await api.get(`/product-variants/product/${productId}`)).data,
  getVariantValuesByProductVariant: async (productVariantId) => (await api.get(`/variant-attribute-values/product-variant/${productVariantId}`)).data,
  getBrands: async () => (await api.get("/brands")).data,
  searchBrands: async (keyword) => (await api.get(`/brands/search?keyword=${encodeURIComponent(keyword)}`)).data,
  getCategories: async () => (await api.get("/categories")).data,
  getActiveCategories: async () => (await api.get("/categories/active")).data,
  searchCategories: async (keyword) => (await api.get(`/categories/search?keyword=${encodeURIComponent(keyword)}`)).data,
  validateCoupon: async (code, orderAmount) =>
    (await api.get(`/coupons/validate?code=${encodeURIComponent(code)}&orderAmount=${encodeURIComponent(orderAmount)}`)).data,
};
