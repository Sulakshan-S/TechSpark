import React from "react";
import { Pencil, Plus, Trash2 } from "lucide-react";
import { useEffect, useMemo, useState } from "react";
import DataTable from "../../components/common/DataTable";
import Modal from "../../components/common/Modal";
import SectionTitle from "../../components/common/SectionTitle";
import { slugify } from "../../lib/utils";
import { adminService } from "../../services/adminService";

function normalizeForRequest(values) {
  const payload = { ...values };

  Object.keys(payload).forEach((key) => {
    const value = payload[key];
    if (value === "") {
      payload[key] = null;
    }
    if (typeof value === "string" && /^\d+(\.\d+)?$/.test(value) && !key.toLowerCase().includes("slug") && !key.toLowerCase().includes("code") && !key.toLowerCase().includes("sku")) {
      payload[key] = Number(value);
    }
  });

  return payload;
}

export default function AdminResourcePage({ config }) {
  const [rows, setRows] = useState([]);
  const [search, setSearch] = useState("");
  const [open, setOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [form, setForm] = useState(config.initialValues);

  const load = async () => {
    const data = await adminService.list(config.endpoint);
    setRows(data);
  };

  useEffect(() => {
    load();
  }, [config.endpoint]);

  const filteredRows = useMemo(() => {
    if (!search) return rows;
    const value = search.toLowerCase();
    return rows.filter((row) => JSON.stringify(row).toLowerCase().includes(value));
  }, [rows, search]);

  const openCreate = () => {
    setEditing(null);
    setForm(config.initialValues);
    setOpen(true);
  };

  const openEdit = (row) => {
    setEditing(row);
    setForm({ ...config.initialValues, ...row });
    setOpen(true);
  };

  const submit = async (e) => {
    e.preventDefault();
    const payload = normalizeForRequest(form);
    if (payload.name && !payload.slug) payload.slug = slugify(payload.name);

    if (editing) {
      await adminService.update(config.endpoint, editing[config.idKey], payload);
    } else {
      await adminService.create(config.endpoint, payload);
    }

    setOpen(false);
    setEditing(null);
    setForm(config.initialValues);
    load();
  };

  const remove = async (row) => {
    if (!window.confirm(`Delete this ${config.title.slice(0, -1).toLowerCase()}?`)) return;
    await adminService.remove(config.endpoint, row[config.idKey]);
    load();
  };

  return (
    <div>
      <SectionTitle
        eyebrow="Resource management"
        title={config.title}
        description={`Manage ${config.title.toLowerCase()} through the same backend resources used by your admin APIs.`}
        action={<button onClick={openCreate} className="btn-primary rounded-full"><Plus size={16} /> Add new</button>}
      />

      <div className="mt-8 rounded-[28px] border border-slate-200 bg-white p-5 shadow-[0_10px_35px_rgba(15,23,42,0.05)]">
        <div className="grid gap-4 lg:grid-cols-[1fr_auto] lg:items-center">
          <input className="input rounded-full" placeholder={`Search ${config.title.toLowerCase()}...`} value={search} onChange={(e) => setSearch(e.target.value)} />
          <div className="text-sm text-slate-500">{filteredRows.length} record{filteredRows.length === 1 ? "" : "s"}</div>
        </div>
      </div>

      <div className="mt-6">
        <DataTable
          columns={config.columns}
          rows={filteredRows.map((row) => ({ ...row, id: row[config.idKey] }))}
          actions={(row) => (
            <div className="flex flex-wrap gap-2">
              {!config.disableEdit && <button onClick={() => openEdit(row)} className="btn-secondary rounded-full"><Pencil size={16} /></button>}
              {!config.disableDelete && <button onClick={() => remove(row)} className="btn-secondary rounded-full"><Trash2 size={16} /></button>}
            </div>
          )}
        />
      </div>

      <Modal open={open} title={`${editing ? "Edit" : "Add"} ${config.title.slice(0, -1)}`} onClose={() => setOpen(false)}>
        <form onSubmit={submit} className="grid gap-4 sm:grid-cols-2">
          {config.formFields.map((field) => (
            <div key={field.name} className={field.type === "textarea" ? "sm:col-span-2" : ""}>
              <label className="mb-2 block text-sm font-medium text-slate-700">{field.label}</label>

              {field.type === "textarea" ? (
                <textarea className="input min-h-32" value={form[field.name] ?? ""} onChange={(e) => setForm({ ...form, [field.name]: e.target.value })} />
              ) : field.type === "select" ? (
                <select className="select" value={form[field.name] ?? ""} onChange={(e) => setForm({ ...form, [field.name]: e.target.value })}>
                  {field.options.map((opt) => <option key={opt} value={opt}>{opt}</option>)}
                </select>
              ) : field.type === "checkbox" ? (
                <label className="flex items-center gap-3 rounded-2xl border border-slate-300 bg-slate-50 p-4 text-slate-700">
                  <input type="checkbox" checked={Boolean(form[field.name])} onChange={(e) => setForm({ ...form, [field.name]: e.target.checked })} />
                  <span>{field.label}</span>
                </label>
              ) : (
                <input
                  type={field.type}
                  className="input"
                  value={form[field.name] ?? ""}
                  disabled={editing && config.idReadOnlyOnEdit && field.name === config.idKey}
                  onChange={(e) => setForm({ ...form, [field.name]: e.target.value })}
                />
              )}
            </div>
          ))}
          <button className="btn-primary rounded-full sm:col-span-2">Save</button>
        </form>
      </Modal>
    </div>
  );
}
