package com.example.testwork;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;


public class Game extends Fragment {
    MainActivity activity;
    public static Adapter adapter;
    public boolean timer;
    public static TextView secundtostart;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = ((MainActivity) getActivity());
        if(savedInstanceState!=null){
            timer = true;
            adapter = (Adapter) savedInstanceState.getSerializable("adapter");
        }else{
            adapter = new Adapter(activity, 4, 4);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_game,container,false);
        TextView title = ((MainActivity) getActivity()).findViewById(R.id.textView3);
        title.setText(R.string.game);
        GridView grid = view.findViewById(R.id.grid);
        MainActivity.refresh.setVisibility(View.VISIBLE);

        setRetainInstance(true);

        final Handler lateclose = new Handler();
        secundtostart = view.findViewById(R.id.timetostart);
        if(!timer) {
            lateclose.post(new Runnable() {
                int x = 6;

                @Override
                public void run() {
                    secundtostart = view.findViewById(R.id.timetostart);
                    MainActivity.refresh.setEnabled(false);
                    if (x >= 0) {

                        secundtostart.setText(x + "");
                        if (secundtostart != null && x <= 0) {
                            secundtostart.setVisibility(View.GONE);
                        }
                        x--;
                    }
                    if (x > -1){
                        lateclose.postDelayed(this, 1000);
                }else
                        MainActivity.refresh.setEnabled(true);
                }
            });
        }
        grid.setNumColumns(4);
        grid.setEnabled(true);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.onecell(position);
                if(adapter.checklose()){
                    showdialog();
                }

            }
        });
        grid.setAdapter(adapter);
        MainActivity.refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.update();
            }
        });


        return view;
    }

    public void showdialog(){
        CustomDialogFragment dialog = new CustomDialogFragment();
        dialog.show(getFragmentManager(), "show");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("adapter", adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivity.refresh.setVisibility(View.GONE);
        adapter = null;
        secundtostart = null;   //Незнаю есть ли смысл занулять ссылки при использование контекстных статических классов
    }
}