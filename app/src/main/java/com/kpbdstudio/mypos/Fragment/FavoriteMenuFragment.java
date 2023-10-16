package com.kpbdstudio.mypos.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.kpbdstudio.mypos.R;
import com.kpbdstudio.mypos.adapter.FavouriteAdapter;
import com.kpbdstudio.mypos.database.Query;
import com.kpbdstudio.mypos.entities.FavoriteObject;
import com.kpbdstudio.mypos.util.Helper;


import java.util.List;


public class FavoriteMenuFragment extends Fragment {

    private static final String TAG = FavoriteMenuFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private Query databaseQuery;

    public FavoriteMenuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.favorite_menu_items));
        View view = inflater.inflate(R.layout.fragment_favorite_menu, container, false);

        databaseQuery = new Query(getActivity());
        List<FavoriteObject> favoriteObjects = databaseQuery.listFavoriteMenu();

        recyclerView = (RecyclerView)view.findViewById(R.id.favorite_menu);
        GridLayoutManager mGrid = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mGrid);
        recyclerView.setHasFixedSize(true);

        if(favoriteObjects == null){
            Helper.displayErrorMessage(getActivity(), "Your favorite list is empty");
        }else {
            FavouriteAdapter mAdapter = new FavouriteAdapter(getActivity(), favoriteObjects);
            recyclerView.setAdapter(mAdapter);
        }

        return view;
    }

}
