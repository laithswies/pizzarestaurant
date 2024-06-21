package com.example.pizzarestaurant.ui.view_special_offers;

import androidx.lifecycle.ViewModelProvider;
import android.content.ContentValues;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pizzarestaurant.R;
import com.example.pizzarestaurant.databinding.FragmentViewSpecialOffersBinding;

import java.util.List;

public class ViewSpecialOffersFragment extends Fragment {

    private ViewSpecialOffersViewModel mViewModel;
    private FragmentViewSpecialOffersBinding binding;
    private SpecialOffersAdapter adapter;

    public static ViewSpecialOffersFragment newInstance() {
        return new ViewSpecialOffersFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(ViewSpecialOffersViewModel.class);
        binding = FragmentViewSpecialOffersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.recyclerViewSpecialOffers;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mViewModel.getSpecialOffers().observe(getViewLifecycleOwner(), specialOffers -> {
            adapter = new SpecialOffersAdapter(specialOffers, this);
            recyclerView.setAdapter(adapter);
        });

        return root;
    }
}
