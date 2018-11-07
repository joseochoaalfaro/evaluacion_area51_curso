package com.albavision.ar.elnueve.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.albavision.ar.elnueve.R;
import com.albavision.ar.elnueve.models.main_noticia_model;

import java.io.InputStream;
import java.util.List;

/**
 * Created by yoshi on 10/08/17.
 */

public class detalle_noticia_cab_adapter  extends RecyclerView.Adapter<detalle_noticia_cab_adapter.ViewHolder> {
    private List<main_noticia_model> noticiaLista;
    private Context mcon;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imgNoticia,icoVideo,icoFotos;
        public TextView txtEncabezado, txtTitulo, txtId;

        public ViewHolder(View v){
            super(v);
            txtEncabezado = (TextView)v.findViewById(R.id.txtEncabezado);
            txtTitulo = (TextView)v.findViewById(R.id.txtTitulo);
            txtId = (TextView)v.findViewById(R.id.txtId);
            imgNoticia = (ImageView)v.findViewById(R.id.imgNoticia);
            icoVideo = (ImageView)v.findViewById(R.id.icoVideo);
            icoFotos = (ImageView)v.findViewById(R.id.icoFotos);
        }
    }

    public detalle_noticia_cab_adapter(Context con, List<main_noticia_model> noticiaLista){
        mcon = con;
        this.noticiaLista = noticiaLista;
    }

    @Override
    public detalle_noticia_cab_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detalle_noticias_cab,parent,false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(detalle_noticia_cab_adapter.ViewHolder holder,int position){
        main_noticia_model noticia = noticiaLista.get(position);
        holder.txtEncabezado.setText(String.valueOf(noticia.getCategoria()));
        holder.txtTitulo.setText(String.valueOf(noticia.getTitulo()));
        holder.txtId.setText(String.valueOf(noticia.getCodigo()));
        if(noticia.getTipo().equals("galeria_fotos")){
            holder.icoVideo.setVisibility(View.GONE);
            holder.icoFotos.setVisibility(View.VISIBLE);
        }else
        {
            holder.icoVideo.setVisibility(View.VISIBLE);
            holder.icoFotos.setVisibility(View.GONE);
        }

        new  DownloadImageFromInternet((ImageView) holder.imgNoticia)
                .execute(noticia.getUrl_imagen());
        switch (String.valueOf(noticia.getCodcat())){
            case "1":
                holder.txtEncabezado.setBackgroundColor(mcon.getApplicationContext().getResources().getColor(R.color.fondo_encabezado_noticias));
                break;
            case "2":
                holder.txtEncabezado.setBackgroundColor(mcon.getApplicationContext().getResources().getColor(R.color.fondo_encabezado_espectaculos));
                break;
            case "3":
                holder.txtEncabezado.setBackgroundColor(mcon.getApplicationContext().getResources().getColor(R.color.fondo_encabezado_deportes));
                break;
            default:
                holder.txtEncabezado.setBackgroundColor(mcon.getApplicationContext().getResources().getColor(R.color.fondo_encabezado_noticias));
                break;
        }

        holder.imgNoticia.getLayoutParams().height = holder.imgNoticia.getLayoutParams().width;
    }

    @Override
    public int getItemCount() {
        return noticiaLista.size();
    }





    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
            //Toast.makeText(getApplicationContext(), "Please wait, it may take a few minute...", Toast.LENGTH_SHORT).show();
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }

            //return bimage;
            return bimage.createScaledBitmap(bimage,bimage.getWidth(),bimage.getWidth(),true);
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}
