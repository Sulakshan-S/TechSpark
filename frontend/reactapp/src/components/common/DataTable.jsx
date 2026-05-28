import React from "react";
import { formatCurrency, formatDateTime } from "../../lib/utils";

export default function DataTable({ columns, rows, actions }) {
  return (
    <div className="overflow-hidden rounded-[28px] border border-slate-200 bg-white shadow-[0_10px_35px_rgba(15,23,42,0.05)]">
      <div className="overflow-x-auto">
        <table className="min-w-full divide-y divide-slate-200 text-left text-sm">
          <thead className="bg-slate-50">
            <tr>
              {columns.map((column) => (
                <th key={column.key} className="px-4 py-3 text-xs font-semibold uppercase tracking-[0.16em] text-slate-500">
                  {column.label}
                </th>
              ))}
              {actions && <th className="px-4 py-3 text-xs font-semibold uppercase tracking-[0.16em] text-slate-500">Actions</th>}
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-100 bg-white">
            {rows.map((row, index) => (
              <tr key={row.id ?? row.key ?? index} className="hover:bg-slate-50">
                {columns.map((column) => {
                  const value = row[column.key];
                  let display = value;

                  if (column.type === "currency") display = formatCurrency(value);
                  if (column.type === "datetime") display = formatDateTime(value);
                  if (column.render) display = column.render(row);

                  return (
                    <td key={column.key} className="px-4 py-3 align-top text-slate-700">
                      {display ?? "-"}
                    </td>
                  );
                })}
                {actions && <td className="px-4 py-3">{actions(row)}</td>}
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
