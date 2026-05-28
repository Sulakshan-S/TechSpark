import React,  { createContext, useContext, useMemo, useState } from "react";
import { userService } from "../services/userService";
import { useAuth } from "./AuthContext";

const CartContext = createContext(null);

export function CartProvider({ children }) {
  const { isAuthenticated } = useAuth();
  const [cart, setCart] = useState(null);

  const refreshCart = async () => {
    if (!isAuthenticated) {
      setCart(null);
      return null;
    }
    const data = await userService.getCart();
    setCart(data);
    return data;
  };

  const value = useMemo(() => ({ cart, setCart, refreshCart }), [cart]);

  return <CartContext.Provider value={value}>{children}</CartContext.Provider>;
}

export function useCart() {
  const context = useContext(CartContext);
  if (!context) throw new Error("useCart must be used inside CartProvider");
  return context;
}
