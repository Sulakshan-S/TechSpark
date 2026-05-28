import React from "react";
import { Route, Routes } from "react-router-dom";
import PublicLayout from "./layouts/PublicLayout";
import AdminLayout from "./layouts/AdminLayout";
import HomePage from "./pages/public/HomePage";
import ShopPage from "./pages/public/ShopPage";
import ProductDetailPage from "./pages/public/ProductDetailPage";
import LoginPage from "./pages/auth/LoginPage";
import RegisterPage from "./pages/auth/RegisterPage";
import CartPage from "./pages/user/CartPage";
import WishlistPage from "./pages/user/WishlistPage";
import AddressesPage from "./pages/user/AddressesPage";
import CheckoutPage from "./pages/user/CheckoutPage";
import OrdersPage from "./pages/user/OrdersPage";
import ProfilePage from "./pages/user/ProfilePage";
import RequireAuth from "./routes/RequireAuth";
import RequireAdmin from "./routes/RequireAdmin";
import DashboardPage from "./pages/admin/DashboardPage";
import AdminResourcePage from "./pages/admin/AdminResourcePage";
import AdminOrdersPage from "./pages/admin/AdminOrdersPage";
import NotFoundPage from "./pages/shared/NotFoundPage";
import { adminResourceConfigs } from "./config/adminResources";

export default function App() {
  return (
    <Routes>
      <Route element={<PublicLayout />}>
        <Route path="/" element={<HomePage />} />
        <Route path="/shop" element={<ShopPage />} />
        <Route path="/products/:productId" element={<ProductDetailPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />

        <Route element={<RequireAuth />}>
          <Route path="/cart" element={<CartPage />} />
          <Route path="/wishlist" element={<WishlistPage />} />
          <Route path="/addresses" element={<AddressesPage />} />
          <Route path="/checkout" element={<CheckoutPage />} />
          <Route path="/orders" element={<OrdersPage />} />
          <Route path="/profile" element={<ProfilePage />} />
        </Route>
      </Route>

      <Route element={<RequireAdmin />}>
        <Route path="/admin" element={<AdminLayout />}>
          <Route index element={<DashboardPage />} />
          {adminResourceConfigs.map((config) => (
            <Route
              key={config.slug}
              path={config.slug}
              element={<AdminResourcePage config={config} />}
            />
          ))}
          <Route path="orders" element={<AdminOrdersPage />} />
        </Route>
      </Route>

      <Route path="*" element={<NotFoundPage />} />
    </Routes>
  );
}
