import api from "./api";

export const userService = {
  getCart: async () => (await api.get("/cart")).data,
  addToCart: async (payload) => (await api.post("/cart/items", payload)).data,
  updateCartItem: async (cartItemId, payload) => (await api.put(`/cart/items/${cartItemId}`, payload)).data,
  removeCartItem: async (cartItemId) => (await api.delete(`/cart/items/${cartItemId}`)).data,
  clearCart: async () => (await api.delete("/cart/clear")).data,

  getWishlist: async () => (await api.get("/wishlist")).data,
  addWishlist: async (payload) => (await api.post("/wishlist", payload)).data,
  checkWishlist: async (productId) => (await api.get(`/wishlist/check/${productId}`)).data,
  removeWishlist: async (wishlistItemId) => (await api.delete(`/wishlist/${wishlistItemId}`)).data,
  removeWishlistByProduct: async (productId) => (await api.delete(`/wishlist/product/${productId}`)).data,

  getAddresses: async () => (await api.get("/addresses")).data,
  getDefaultAddress: async () => (await api.get("/addresses/default")).data,
  createAddress: async (payload) => (await api.post("/addresses", payload)).data,
  updateAddress: async (id, payload) => (await api.put(`/addresses/${id}`, payload)).data,
  setDefaultAddress: async (id) => (await api.patch(`/addresses/${id}/default`)).data,
  deleteAddress: async (id) => (await api.delete(`/addresses/${id}`)).data,

  placeOrder: async (payload) => (await api.post("/orders", payload)).data,
  getMyOrders: async () => (await api.get("/orders/my")).data,
  getMyOrderById: async (id) => (await api.get(`/orders/my/${id}`)).data,
  cancelMyOrder: async (id) => (await api.patch(`/orders/my/${id}/cancel`)).data,
};
