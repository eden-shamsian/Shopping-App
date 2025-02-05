package com.example.secondandroidhita.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.secondandroidhita.R;
import com.example.secondandroidhita.activitys.models.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;
    private Context context;
    private DatabaseReference cartDb;
    private HashMap<String, Integer> cart;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
        this.cartDb = FirebaseDatabase.getInstance().getReference("cart");
        this.cart = new HashMap<>();
        loadCartFromFirebase();
    }

    private void loadCartFromFirebase() {
        cartDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    String productId = child.getKey();
                    Integer quantity = child.getValue(Integer.class);
                    if (productId != null && quantity != null) {
                        cart.put(productId, quantity);
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
            }
        });
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getTitle());
        holder.productPrice.setText("$" + product.getPrice());

        // Load product image
        Glide.with(context).load(product.getImage()).into(holder.productImage);

        // Set up quantity spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                context, R.array.product_quantity_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.productQuantitySpinner.setAdapter(adapter);

        // Load previous quantity from cart
        int currentQuantity = cart.getOrDefault(String.valueOf(product.getId()), 1);
        holder.productQuantitySpinner.setSelection(currentQuantity - 1);

        holder.addSimilarProductButton.setOnClickListener(v -> {
            int quantity = Integer.parseInt(holder.productQuantitySpinner.getSelectedItem().toString());
            addToCart(product, quantity);
        });

        holder.removeProductButton.setOnClickListener(v -> {
            removeFromCart(product);
        });
    }

    private void addToCart(Product product, int quantity) {
        cart.put(String.valueOf(product.getId()), quantity);
        cartDb.child(String.valueOf(product.getId())).setValue(quantity);
        notifyDataSetChanged();
    }

    private void removeFromCart(Product product) {
        if (cart.containsKey(String.valueOf(product.getId())) && cart.get(String.valueOf(product.getId())) > 0) {
            int newQuantity = cart.get(String.valueOf(product.getId())) - 1;
            if (newQuantity == 0) {
                cart.remove(String.valueOf(product.getId()));
                cartDb.child(String.valueOf(product.getId())).removeValue();
            } else {
                cart.put(String.valueOf(product.getId()), newQuantity);
                cartDb.child(String.valueOf(product.getId())).setValue(newQuantity);
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice;
        ImageView productImage;
        Spinner productQuantitySpinner;
        Button addSimilarProductButton, removeProductButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productImage = itemView.findViewById(R.id.productImage);
            productQuantitySpinner = itemView.findViewById(R.id.productQuantitySpinner);
            addSimilarProductButton = itemView.findViewById(R.id.addToCartProductButton);
            removeProductButton = itemView.findViewById(R.id.removeProductButton);
        }
    }
}
