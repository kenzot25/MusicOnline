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

import com.toan.musicapp.AdapterClass.TimKiemNhac_rvAdapter;
import com.toan.musicapp.DAOClass.DAO_Nhac;
import com.toan.musicapp.DialogClass.DialogThemVaCapNhatNhac;
import com.toan.musicapp.MainActivity;
import com.toan.musicapp.Model.Nhac;
import com.toan.musicapp.R;

import java.util.ArrayList;

public class XemThemNhacFragment extends Fragment {
    EditText txtTimKiemNhac;
    RecyclerView rvXemThemNhac;
    ArrayList<Nhac> dsn;
    TimKiemNhac_rvAdapter adapterTimKiemNhac;
    public XemThemNhacFragment(ArrayList dsn) {
        this.dsn = dsn;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timkiem_don, container, false);
        findView(view);
        adapterTimKiemNhac = new TimKiemNhac_rvAdapter(getContext(), 30);
        rvXemThemNhac.setAdapter(adapterTimKiemNhac);
        rvXemThemNhac.setLayoutManager(new LinearLayoutManager(getContext()));
        txtTimKiemNhac.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    adapterTimKiemNhac.resetAdapter();
                    String query = txtTimKiemNhac.getText().toString();
                    if (!query.isEmpty()) {
                        DAO_Nhac.readSpecificMusics(txtTimKiemNhac.getText().toString(), adapterTimKiemNhac, 20);
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
                    DialogThemVaCapNhatNhac dialog = new DialogThemVaCapNhatNhac(null, false);
                    dialog.show(getChildFragmentManager(), "DialogThemVaCapNhatNhac");
                }
            };
            mainActivity.setOnClickToolbarAction1(listener, R.drawable.ic_add, View.VISIBLE);
            mainActivity.setOnClickToolbarAction2(null, R.drawable.ic_edit, View.INVISIBLE);
        }
    }
    private void findView(View view){
        txtTimKiemNhac = view.findViewById(R.id.txtTimKiem);
        rvXemThemNhac = view.findViewById(R.id.rvXemThem);
    }
}
