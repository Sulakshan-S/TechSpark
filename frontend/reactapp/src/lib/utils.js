export const formatCurrency = (value) => {
  const num = Number(value || 0);
  return new Intl.NumberFormat("en-LK", {
    style: "currency",
    currency: "LKR",
    maximumFractionDigits: 2,
  }).format(num);
};

export const formatDateTime = (value) => {
  if (!value) return "-";
  const date = new Date(value);
  return new Intl.DateTimeFormat("en-LK", {
    dateStyle: "medium",
    timeStyle: "short",
  }).format(date);
};

export const slugify = (value = "") =>
  value
    .toLowerCase()
    .trim()
    .replace(/[^a-z0-9]+/g, "-")
    .replace(/^-+|-+$/g, "");

export const cn = (...classes) => classes.filter(Boolean).join(" ");
