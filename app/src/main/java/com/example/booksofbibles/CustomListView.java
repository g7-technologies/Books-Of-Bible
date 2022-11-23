package com.example.booksofbibles;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.white.progressview.CircleProgressView;

public class CustomListView extends ArrayAdapter<String>{

    private String[] book_name;
//    private String[] path;
//    private Integer[] completion;
    private Activity context;
    Bitmap bitmap;

    public CustomListView(Activity context,String[] bookName) {
        super(context,R.layout.layout_book_list, bookName);
        //super();
        this.context=context;
        this.book_name =bookName;
//        this.path =path;
//        this.completion = completion;
    }

    @NonNull
    @Override

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View r=convertView;
        ViewHolder viewHolder=null;
        if(r==null){
            LayoutInflater layoutInflater=context.getLayoutInflater();
            r=layoutInflater.inflate(R.layout.layout_book_list,null,true);
            viewHolder=new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else {
            viewHolder=(ViewHolder)r.getTag();

        }

        viewHolder.tvw1.setText(book_name[position]);
     //   Glide.with(context).asBitmap().load(path[position]).into(viewHolder.tvw2);

       // viewHolder.tw3.setProgress(completion[position]);
//        viewHolder.tw4.setText(complainapprove[position]);

//      new GetImageFromURL(viewHolder.ivw).execute(imagepath[position]);
      //  PicassoClient.downloadImage(c,spacecraft.getImageUrl(),img);

        return r;
    }

    class ViewHolder{

        TextView tvw1;
        ImageView tvw2;
        //CircleProgressView tw3;


        ViewHolder(View v){
            tvw1=v.findViewById(R.id.book_name);
            tvw2=v.findViewById(R.id.image_book);
            //tw3=v.findViewById(R.id.image_book_completion);
//            tw4=(TextView)v.findViewById(R.id.status);
        }

    }

//    public class GetImageFromURL extends AsyncTask<String,Void,Bitmap>
//    {
//
//        ImageView imgView;
//        public GetImageFromURL(ImageView imgv)
//        {
//            this.imgView=imgv;
//        }
//        @Override
//        protected Bitmap doInBackground(String... url) {
//            String urldisplay=url[0];
//            bitmap=null;
//
//            try{
//
//                InputStream ist=new java.net.URL(urldisplay).openStream();
//                bitmap= BitmapFactory.decodeStream(ist);
//            }
//            catch (Exception ex)
//            {
//                ex.printStackTrace();
//            }
//
//            return bitmap;
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap){
//
//            super.onPostExecute(bitmap);
//            imgView.setImageBitmap(bitmap);
//        }
   // }



}