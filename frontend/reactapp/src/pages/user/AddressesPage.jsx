import React from "react";
import { Pencil, Plus, Trash2 } from "lucide-react";
import { useEffect, useState } from "react";
import Modal from "../../components/common/Modal";
import SectionTitle from "../../components/common/SectionTitle";
import { userService } from "../../services/userService";

const initialForm = {
  fullName: "",
  phone: "",
  line1: "",
  line2: "",
  city: "",
  district: "",
  postalCode: "",
  country: "Sri Lanka",
  isDefault: false,
};

export default function AddressesPage() {
  const [items, setItems] = useState([]);
  const [open, setOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [form, setForm] = useState(initialForm);

  const load = async () => {
    const data = await userService.getAddresses();
    setItems(data);
  };

  useEffect(() => { load(); }, []);

  const submit = async (e) => {
    e.preventDefault();
    if (editing) {
      await userService.updateAddress(editing.addressId, form);
    } else {
      await userService.createAddress(form);
    }
    setOpen(false);
    setEditing(null);
    setForm(initialForm);
    load();
  };

  return (
    <div className="container-shell py-12">
      <SectionTitle
        eyebrow="Addresses"
        title="Manage shipping addresses"
        description="Choose a default address for checkout."
        action={<button onClick={() => { setEditing(null); setForm(initialForm); setOpen(true); }} className="btn-primary"><Plus size={16} /> New address</button>}
      />

      <div className="mt-8 grid gap-4 md:grid-cols-2">
        {items.map((item) => (
          <div key={item.addressId} className="card p-5">
            <div className="flex items-start justify-between gap-4">
              <div>
                <div className="flex items-center gap-2">
                  <h3 className="text-lg font-semibold">{item.fullName}</h3>
                  {(item.isDefault ?? item.default) && <span className="badge">Default</span>}
                </div>
                <p className="mt-2 text-sm text-slate-400">{item.phone}</p>
                <p className="mt-2 text-sm text-slate-400">{item.line1}, {item.line2}</p>
                <p className="text-sm text-slate-400">{item.city}, {item.district}, {item.postalCode}</p>
                <p className="text-sm text-slate-400">{item.country}</p>
              </div>
              <div className="flex gap-2">
                <button onClick={() => { setEditing(item); setForm({ ...item, isDefault: (item.isDefault ?? item.default) }); setOpen(true); }} className="btn-secondary"><Pencil size={16} /></button>
                <button onClick={async () => { await userService.deleteAddress(item.addressId); load(); }} className="btn-secondary"><Trash2 size={16} /></button>
              </div>
            </div>
            {!(item.isDefault ?? item.default) && <button onClick={async () => { await userService.setDefaultAddress(item.addressId); load(); }} className="btn-primary mt-4">Set as default</button>}
          </div>
        ))}
      </div>

      <Modal open={open} title={editing ? "Edit address" : "Add address"} onClose={() => setOpen(false)}>
        <form onSubmit={submit} className="grid gap-4 sm:grid-cols-2">
          {[
            ["fullName", "Full name"], ["phone", "Phone"], ["line1", "Line 1"], ["line2", "Line 2"],
            ["city", "City"], ["district", "District"], ["postalCode", "Postal code"], ["country", "Country"],
          ].map(([key, label]) => (
            <input key={key} className="input" placeholder={label} value={form[key] || ""} onChange={(e) => setForm({ ...form, [key]: e.target.value })} />
          ))}
          <label className="flex items-center gap-2 text-sm text-slate-300 sm:col-span-2">
            <input type="checkbox" checked={form.isDefault || false} onChange={(e) => setForm({ ...form, isDefault: e.target.checked })} />
            Make default
          </label>
          <button className="btn-primary sm:col-span-2">Save address</button>
        </form>
      </Modal>
    </div>
  );
}
