package com.toan.musicapp.FragmentClass;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.toan.musicapp.R;

public class WebTinTucFragment extends Fragment {
    WebView wvTinTuc;
    String link;
    public WebTinTucFragment(String link){
        this.link = link;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_webtintuc, container, false);
        findView(view);
        if (!link.isEmpty()){
            wvTinTuc.loadUrl(link);
        }
        return view;
    }
    private void findView(View view){
        wvTinTuc = view.findViewById(R.id.wvTinTuc);
    }
}
