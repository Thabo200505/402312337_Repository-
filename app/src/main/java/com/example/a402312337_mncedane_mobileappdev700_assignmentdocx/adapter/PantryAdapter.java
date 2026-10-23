package com.example.a402312337_mncedane_mobileappdev700_assignmentdocx.adapter;


import com.example.a402312337_mncedane_mobileappdev700_assignmentdocx.ExpiryHelper;
import com.example.a402312337_mncedane_mobileappdev700_assignmentdocx.R;
import com.example.a402312337_mncedane_mobileappdev700_assignmentdocx.model.PantryItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import android.content.res.ColorStateList;
import androidx.core.content.ContextCompat;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private PantryItem item;

    public interface Listener {
        void onItemClick(PantryItem item);     // tap row -> edit
        void onDeleteClick(PantryItem item);   // tap bin -> delete
    }

    private final ArrayList<PantryItem> items = new ArrayList<>();
    private final Listener listener;
    private boolean alertsEnabled = true;

    public void setAlertsEnabled(boolean enabled) {
        this.alertsEnabled = enabled;
    }

    public PantryAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setItems(List<PantryItem> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    // ----- ViewHolder -----
    static class PantryViewHolder extends RecyclerView.ViewHolder {
        private final ColorStateList defaultExpiryColor;
        TextView tvName, tvQuantity, tvExpiry;
        ImageButton btnDelete;

        PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            defaultExpiryColor = tvExpiry.getTextColors();
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvExpiry = itemView.findViewById(R.id.tvExpiry);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);
        return new PantryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        String expiry = item.getExpiryDate();
        if (expiry == null || expiry.isEmpty()) {
            holder.tvExpiry.setVisibility(View.GONE);
        } else {
            holder.tvExpiry.setVisibility(View.VISIBLE);
            holder.tvExpiry.setTextColor(holder.defaultExpiryColor);   // reset (rows get recycled)
            holder.tvExpiry.setText("Expires: " + expiry);

            if (alertsEnabled) {
                Context ctx = holder.itemView.getContext();
                if (ExpiryHelper.isExpired(expiry)) {
                    holder.tvExpiry.setText("EXPIRED (" + expiry + ")");
                    holder.tvExpiry.setTextColor(
                            ContextCompat.getColor(ctx, R.color.ingredient_missing));
                } else if (ExpiryHelper.isExpiringSoon(expiry)) {
                    Long days = ExpiryHelper.daysUntil(expiry);
                    holder.tvExpiry.setText(days == 0
                            ? "Expires TODAY (" + expiry + ")"
                            : "Expires in " + days + (days == 1 ? " day" : " days")
                            + " (" + expiry + ")");
                    holder.tvExpiry.setTextColor(
                            ContextCompat.getColor(ctx, R.color.expiry_soon));
                }
            }
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // 2.0 -> "2", 2.5 -> "2.5"
    private String formatQuantity(double q) {
        return (q == Math.floor(q)) ? String.valueOf((long) q) : String.valueOf(q);
    }
}
