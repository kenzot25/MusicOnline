package com.toan.musicapp.FragmentClass;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.toan.musicapp.AdapterClass.TimKiemAlbum_rvAdapter;
import com.toan.musicapp.DAOClass.DAO_Album;
import com.toan.musicapp.DialogClass.DialogThemVaCapNhatAlbum;
import com.toan.musicapp.MainActivity;
import com.toan.musicapp.Model.Albums;
import com.toan.musicapp.R;

import java.util.ArrayList;

public class XemThemAlbumFragment extends Fragment {
    EditText txtTimKiem;
    RecyclerView rvXemThem;
    ArrayList<Albums> dsalbums;
    TimKiemAlbum_rvAdapter adapterTimKiemAlbums;
    public XemThemAlbumFragment(ArrayList dsalbums) {
        this.dsalbums = dsalbums;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timkiem_don, container, false);
        findView(view);
        adapterTimKiemAlbums = new TimKiemAlbum_rvAdapter(getContext(), 30);
        rvXemThem.setAdapter(adapterTimKiemAlbums);
        rvXemThem.setLayoutManager(new LinearLayoutManager(getContext()));
        txtTimKiem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    adapterTimKiemAlbums.resetAdapter();
                    String query = txtTimKiem.getText().toString();
                    if (!query.isEmpty()) {
                        DAO_Album.readSpecificAlbums(txtTimKiem.getText().toString(), adapterTimKiemAlbums, 20);
                    }
                }
                return false;
            }
        });
        ifUserIsAnAdmin();
        return view;
    }
    private void ifUserIsAnAdmin(){
        MainActivity mainActivity = (MainActivity)getContext();
        if (mainActivity.isUserAnAdmin()){
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogThemVaCapNhatAlbum dialog = new DialogThemVaCapNhatAlbum(null, false);
                    dialog.show(getChildFragmentManager(), "DialogThemVaCapNhatAlbum");
                }
            };
            mainActivity.setOnClickToolbarAction1(listener, R.drawable.ic_add, View.VISIBLE);
            mainActivity.setOnClickToolbarAction2(null, R.drawable.ic_edit, View.INVISIBLE);
        }
    }
    private void findView(View view){
        txtTimKiem = view.findViewById(R.id.txtTimKiem);
        rvXemThem = view.findViewById(R.id.rvXemThem);
    }
}
