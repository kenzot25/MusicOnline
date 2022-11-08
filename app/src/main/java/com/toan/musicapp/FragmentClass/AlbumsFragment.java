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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.toan.musicapp.AdapterClass.AlbumTrangChu_rvAdapter;
import com.toan.musicapp.DAOClass.DAO_Album;
import com.toan.musicapp.DialogClass.DialogThemVaCapNhatAlbum;
import com.toan.musicapp.MainActivity;
import com.toan.musicapp.R;

public class AlbumsFragment extends Fragment {
    EditText txtTimKiemAlbums;
    RecyclerView rvAlbum;
    AlbumTrangChu_rvAdapter adapter;
    LinearLayoutManager layoutManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_albums, container, false);
        findView(view);
        adapter = new AlbumTrangChu_rvAdapter(getContext(), 20);
        layoutManager = new GridLayoutManager(getContext(), 2);
        rvAlbum.setAdapter(adapter);
        rvAlbum.setLayoutManager(layoutManager);
        DAO_Album.readPopularAlbum(15, adapter);
        txtTimKiemAlbums.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE && !txtTimKiemAlbums.getText().toString().isEmpty()){
                    adapter.resetAdapter();
                    DAO_Album.readSpecificAlbums(txtTimKiemAlbums.getText().toString(), adapter, 20);
                }
                return false;
            }
        });
        ifUserIsAnAdmin();
        ((MainActivity)getActivity()).setOnClickToolbarAction1(null, 0, View.INVISIBLE);
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
        rvAlbum = view.findViewById(R.id.rvTimKiemAlbums);
        txtTimKiemAlbums = view.findViewById(R.id.txtTimKiemAlbums);
    }
}
