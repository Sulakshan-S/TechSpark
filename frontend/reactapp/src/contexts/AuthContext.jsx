import React, { createContext, useContext, useEffect, useMemo, useState } from "react";
import { authService } from "../services/authService";
import { getErrorMessage } from "../services/api";
import { toast } from "../lib/toast";

const AuthContext = createContext(null);

const storageKeys = {
  token: "techspark_token",
  user: "techspark_user",
};

export function AuthProvider({ children }) {
  const [token, setToken] = useState(localStorage.getItem(storageKeys.token));
  const [user, setUser] = useState(() => {
    const stored = localStorage.getItem(storageKeys.user);
    return stored ? JSON.parse(stored) : null;
  });
  const [loading, setLoading] = useState(Boolean(token && !user));

  useEffect(() => {
    const bootstrap = async () => {
      if (!token) return;
      try {
        const me = await authService.getMe();
        setUser(me);
        localStorage.setItem(storageKeys.user, JSON.stringify(me));
      } catch (error) {
        logout();
      } finally {
        setLoading(false);
      }
    };

    bootstrap();
  }, []);

  const saveSession = (authResponse) => {
    setToken(authResponse.token);
    localStorage.setItem(storageKeys.token, authResponse.token);

    const normalizedUser = {
      userId: authResponse.userId,
      fullName: authResponse.fullName,
      email: authResponse.email,
      role: authResponse.role,
    };
    setUser(normalizedUser);
    localStorage.setItem(storageKeys.user, JSON.stringify(normalizedUser));
  };

  const login = async (payload) => {
    try {
      const response = await authService.login(payload);
      saveSession(response);
      toast.success("Login successful");
      return response;
    } catch (error) {
      const message = getErrorMessage(error);
      toast.error(message);
      throw error;
    }
  };

  const register = async (payload) => {
    try {
      const response = await authService.register(payload);
      saveSession(response);
      toast.success("Registration successful");
      return response;
    } catch (error) {
      const message = getErrorMessage(error);
      toast.error(message);
      throw error;
    }
  };

  const logout = () => {
    setToken(null);
    setUser(null);
    localStorage.removeItem(storageKeys.token);
    localStorage.removeItem(storageKeys.user);
  };

  const value = useMemo(
    () => ({
      token,
      user,
      loading,
      isAuthenticated: Boolean(token),
      isAdmin: user?.role === "ADMIN",
      login,
      register,
      logout,
      refreshUser: async () => {
        const me = await authService.getMe();
        setUser(me);
        localStorage.setItem(storageKeys.user, JSON.stringify(me));
        return me;
      },
    }),
    [token, user, loading]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) throw new Error("useAuth must be used inside AuthProvider");
  return context;
}
