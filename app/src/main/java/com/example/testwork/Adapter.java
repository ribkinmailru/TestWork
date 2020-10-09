package com.example.testwork;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@SuppressLint("ParcelCreator")
public class Adapter extends BaseAdapter implements Serializable, Parcelable {

    Context context;
    int raws,columns;
    public static ArrayList<String> images = new ArrayList<>();
    String allimage;
    Resources res;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    enum stat{close,open,deleted}
    public static ArrayList<String> stats = new ArrayList<>();


    public Adapter(Context context, int raws, int columns){
        this.raws = raws;
        this.columns = columns;
        this.context = context;
        allimage = "image";
        MainActivity.loses = 0;
        MainActivity.attempts.setText("0");
        res = context.getResources();
        makesiquence();
        for(int i = 0; i<(raws*columns); i++){
            stats.add(i, stat.open.name());
        }
        StartGame start = new StartGame();
        start.execute();

    }



    public void makesiquence(){
        images.clear();
        for(int i = 0; i<(raws*columns)/2; i++){
            images.add(allimage+ i);
            images.add(allimage+ i);
        }
        Collections.shuffle(images);

    }
    @Override
    public int getCount() {
        return raws*columns;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView view = null;
        if(convertView == null) {
            view = new ImageView(context);
        }
        else{view = (ImageView) convertView;}
            if(stats.get(position).equals(stat.open.name())){
                Integer drawableId = res.getIdentifier(images.get(position), "drawable", context.getPackageName());
                view.setImageResource(drawableId);
            }
            if(stats.get(position).equals(stat.close.name())) {
                view.setImageResource(R.drawable.simple);
            }
            if(stats.get(position).equals(stat.deleted.name())){
                view.setImageResource(R.drawable.red);

            }
return view;
    }

    public void opencells(){
        final int firstpos = stats.indexOf(stat.open.name());
        final int secondpos = stats.lastIndexOf(stat.open.name());
        if(secondpos == firstpos ){
            return;
        }
        if(images.get(firstpos).equals(images.get(secondpos))){
            stats.set(firstpos, stat.deleted.name());
            stats.set(secondpos, stat.deleted.name());
        }else{
            Async as = new Async(firstpos, secondpos);
            as.execute();

        }
    }

    public void update(){
        for(int i = 0; i<16; i++){
            stats.add(i, stat.open.name());
        }
        MainActivity.loses = 0;
        StartGame start = new StartGame();
        start.execute();
        final Handler lateclose = new Handler();
        lateclose.post(new Runnable() {
            int x = 6;
            @Override
            public void run() {
                Game.secundtostart.setVisibility(View.VISIBLE);
                MainActivity.refresh.setEnabled(false);
                if (x >= 0) {

                    Game.secundtostart.setText(x + "");
                    if(Game.secundtostart!=null && x<=0) {
                        Game.secundtostart.setVisibility(View.GONE);
                    }
                    x--;
                }
                if(x>-1) {
                    lateclose.postDelayed(this, 1000);
                }else{
                    MainActivity.refresh.setEnabled(true);
                }
            }
        });
        Collections.shuffle(images);
        MainActivity.loses = 0;
        MainActivity.attempts.setText(MainActivity.loses+"");
        notifyDataSetChanged();
    }

    public void onecell(int pos){
        if(!stats.get(pos).equals(stat.deleted.name())) {
            stats.set(pos, stat.open.name());
        }
        opencells();
        notifyDataSetChanged();
    }

    public class Async extends AsyncTask<Void,Void,Void>{  // Стоило бы чекнуть на утечки, но мне лень:)
        int firstpos;
        int secondpos;
        public Async(int firstpos, int secondpos){
            this.firstpos = firstpos;
            this.secondpos = secondpos;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(100);
            }catch (InterruptedException ex){}
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            stats.set(firstpos,stat.close.name());
            stats.set(secondpos,stat.close.name());
            MainActivity.loses ++;
            MainActivity.attempts.setText(""+MainActivity.loses);
            notifyDataSetChanged();
        }
    }



    public class StartGame extends AsyncTask<Void,Void,Void> implements Serializable{ // то же самое как и в первом стоило бы пройтись канарейкой
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(6000);
            }catch (InterruptedException ex){}
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            for(int i = 0; i<(raws*columns); i++){
                stats.set(i, stat.close.name());
            }
            notifyDataSetChanged();
        }
    }

    public boolean checklose(){
        boolean result = false;
        if(stats.indexOf(stat.close.name())<0 && stats.indexOf(stat.open.name())<0){
           result = true;
           if(MainActivity.firstrun){
               MainActivity.los = MainActivity.loses;
           }
        }

        return result;
    }
}
