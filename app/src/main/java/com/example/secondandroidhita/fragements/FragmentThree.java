package com.example.secondandroidhita.fragements;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.secondandroidhita.R;
import com.example.secondandroidhita.activitys.models.Product;
import com.example.secondandroidhita.adapters.ProductAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class FragmentThree extends Fragment {

    private TextView userNameText;
    private RecyclerView productRecyclerView;
    private ProductAdapter productAdapter;
    private ArrayList<Product> productList;
    private FirebaseAuth auth;
    private DatabaseReference db;

    public FragmentThree() {
        // חובה שיהיה קונסטרקטור ריק
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_three, container, false);

        // אתחול רכיבי התצוגה
        auth = FirebaseAuth.getInstance();
        userNameText = view.findViewById(R.id.userNameText);
        userNameText.setText(auth.getCurrentUser().getEmail().toString());

        productRecyclerView = view.findViewById(R.id.productRecyclerView);
        db = FirebaseDatabase.getInstance().getReference("products");

        // אתחול רשימת מוצרים ומתאם
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(getContext(),productList);

        // הגדרת RecyclerView
        productRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        productRecyclerView.setAdapter(productAdapter);

        // שליפת מוצרים מה-FIREBASE
        getProductFromFirebase();

        return view;
    }

    private void getProductFromFirebase() {
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    if (product != null) {
                        productList.add(product);
                    }
                }
                Log.d("FirebaseRealtime", "Products loaded: " + productList.size());
                productAdapter.notifyDataSetChanged(); // Update adapter when data changes
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseRealtime", "Error fetching products", databaseError.toException());
            }
        });
    }
}
