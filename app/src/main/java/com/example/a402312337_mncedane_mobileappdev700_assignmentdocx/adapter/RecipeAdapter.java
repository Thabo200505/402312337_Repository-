package com.example.a402312337_mncedane_mobileappdev700_assignmentdocx.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a402312337_mncedane_mobileappdev700_assignmentdocx.R;
import com.example.a402312337_mncedane_mobileappdev700_assignmentdocx.model.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    public interface Listener {
        void onRecipeClick(Recipe recipe);
    }

    private final ArrayList<Recipe> recipes = new ArrayList<>();
    private Map<Integer, String> notes = null;   // recipe id -> note text (may be null)
    private final Listener listener;

    public RecipeAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setData(List<Recipe> newRecipes, Map<Integer, String> newNotes) {
        recipes.clear();
        recipes.addAll(newRecipes);
        notes = newNotes;
        notifyDataSetChanged();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvInfo, tvNote;

        RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvRecipeName);
            tvInfo = itemView.findViewById(R.id.tvRecipeInfo);
            tvNote = itemView.findViewById(R.id.tvRecipeNote);
        }
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe r = recipes.get(position);
        holder.tvName.setText(r.getName());

        int count = r.getIngredients().size();
        holder.tvInfo.setText(count + (count == 1 ? " ingredient" : " ingredients"));

        String note = (notes == null) ? null : notes.get(r.getId());
        if (note == null || note.isEmpty()) {
            holder.tvNote.setVisibility(View.GONE);
        } else {
            holder.tvNote.setVisibility(View.VISIBLE);
            holder.tvNote.setText(note);
        }

        holder.itemView.setOnClickListener(v -> listener.onRecipeClick(r));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }
}
