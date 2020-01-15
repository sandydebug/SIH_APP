package com.example.sih;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.util.TypedValue;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sih.Models.ProductModel;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<ProductModel> productList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Log.e("Dashboard", "In dashboard activity");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        productList = new ArrayList<>();
        adapter = new ProductAdapter(this, productList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareProducts();
    }

    private void prepareProducts() {
        ProductModel p = new ProductModel("Name 1", "400", "21-05-2020");
        productList.add(p);

        p = new ProductModel("Name 2", "500", "31-12-2019");
        productList.add(p);

        p = new ProductModel("Name 3", "600", "08-10-2021");
        productList.add(p);

        p = new ProductModel("Name 4", "700", "05-08-2020");
        productList.add(p);

        p = new ProductModel("Name 5", "800", "31-08-2000");
        productList.add(p);

        adapter.notifyDataSetChanged();
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
