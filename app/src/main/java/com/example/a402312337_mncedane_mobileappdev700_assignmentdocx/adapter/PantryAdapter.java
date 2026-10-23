package com.example.a402312337_mncedane_mobileappdev700_assignmentdocx.adapter;


import com.example.a402312337_mncedane_mobileappdev700_assignmentdocx.R;
import com.example.a402312337_mncedane_mobileappdev700_assignmentdocx.model.PantryItem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    public interface Listener {
        void onItemClick(PantryItem item);     // tap row -> edit
        void onDeleteClick(PantryItem item);   // tap bin -> delete
    }

    private final ArrayList<PantryItem> items = new ArrayList<>();
    private final Listener listener;

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
        TextView tvName, tvQuantity, tvExpiry;
        ImageButton btnDelete;

        PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
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
        PantryItem item = items.get(position);
        holder.tvName.setText(item.getName());
        holder.tvQuantity.setText(formatQuantity(item.getQuantity()) + " " + item.getUnit());

        String expiry = item.getExpiryDate();
        if (expiry == null || expiry.isEmpty()) {
            holder.tvExpiry.setVisibility(View.GONE);
        } else {
            holder.tvExpiry.setVisibility(View.VISIBLE);
            holder.tvExpiry.setText("Expires: " + expiry);
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
