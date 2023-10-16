package com.kpbdstudio.mypos.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.kpbdstudio.mypos.EditProfileActivity;
import com.kpbdstudio.mypos.HotDealActvity;
import com.kpbdstudio.mypos.R;
import com.kpbdstudio.mypos.entities.LoginObject;
import com.kpbdstudio.mypos.util.CustomApplication;


public class ProfileFragment extends Fragment {

    private static final String TAG = ProfileFragment.class.getSimpleName();

    public ProfileFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.my_profile));
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ImageView profileImage = (ImageView)view.findViewById(R.id.profile_image);
        TextView profileName = (TextView)view.findViewById(R.id.profile_name);
        TextView profileAddress = (TextView)view.findViewById(R.id.profile_address);
        TextView profilePhone = (TextView)view.findViewById(R.id.profile_phone_number);

        LoginObject loginUser = ((CustomApplication)getActivity().getApplication()).getLoginUser();
        profileName.setText(loginUser.getUsername());
        profileAddress.setText(loginUser.getAddress());
        profilePhone.setText(loginUser.getPhone());

        Button editProfile = (Button)view.findViewById(R.id.edit_profile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editIntent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(editIntent);
            }
        });

        Button editCart = (Button)view.findViewById(R.id.edit_cart);
        editCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent hotDealIntent = new Intent(getActivity(), HotDealActvity.class);
                startActivity(hotDealIntent);
            }
        });

        return view;
    }

}
