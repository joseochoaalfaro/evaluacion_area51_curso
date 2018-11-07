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
import com.albavision.ar.elnueve.models.main_programa_model;
import com.albavision.ar.elnueve.models.programa_repeticiones_model;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.List;

/**
 * Created by yoshi on 06/09/17.
 */

public class programa_repeticiones_adapter   extends RecyclerView.Adapter<programa_repeticiones_adapter.ViewHolder>   {
    private List<programa_repeticiones_model> programaLista;
    private Context mcon;


    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imgPrograma;
        public TextView txtFrecuencia, txtTitulo, txtIdPrograma;

        public ViewHolder(View v){
            super(v);
            txtFrecuencia = (TextView)v.findViewById(R.id.txtFrecuencia);
            txtTitulo = (TextView)v.findViewById(R.id.txtTitulo);
            txtIdPrograma = (TextView)v.findViewById(R.id.txtIdPrograma);
            imgPrograma = (ImageView)v.findViewById(R.id.imgPrograma);
        }
    }

    public programa_repeticiones_adapter(Context con, List<programa_repeticiones_model> programaLista){
        mcon = con;
        this.programaLista = programaLista;
    }

    @Override
    public programa_repeticiones_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_programa_repeticion,parent,false);

        programa_repeticiones_adapter.ViewHolder vh = new programa_repeticiones_adapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(programa_repeticiones_adapter.ViewHolder holder,int position){
        programa_repeticiones_model programa = programaLista.get(position);
        String textoTmp=String.valueOf(programa.getFecha());

        //se parsea la frecuencia para forzar un salto de línea -> sugerencia de charly y marco, porque charly no lo puede sacar como estaen el diseño
        String[] arrayString = textoTmp.split(" ");

        if(arrayString.length == 7) {
            String textoResultado = arrayString[0].concat(" ");
            textoResultado = textoResultado.concat(arrayString[1].concat(" "));
            textoResultado = textoResultado.concat(arrayString[2].concat(" "));
            textoResultado = textoResultado.concat(arrayString[3].concat("\n"));
            textoResultado = textoResultado.concat(arrayString[4].concat(" "));
            textoResultado = textoResultado.concat(arrayString[5].concat(" "));
            textoResultado = textoResultado.concat(arrayString[6]);

            holder.txtFrecuencia.setText(textoResultado);
        }else
        {
            if(arrayString.length == 6){
                String textoResultado = arrayString[0].concat(" ");
                textoResultado = textoResultado.concat(arrayString[1].concat(" "));
                textoResultado = textoResultado.concat(arrayString[2].concat(" "));
                textoResultado = textoResultado.concat(arrayString[3].concat("\n"));
                textoResultado = textoResultado.concat(arrayString[4].concat(" "));
                textoResultado = textoResultado.concat(arrayString[5]);

                holder.txtFrecuencia.setText(textoResultado);
            }else{
                holder.txtFrecuencia.setText(textoTmp);
            }
        }


        //holder.txtFrecuencia.setText(String.valueOf(programa.getFecha()));
        holder.txtTitulo.setText(String.valueOf(programa.getNombre_programa()));
        holder.txtIdPrograma.setText(String.valueOf(programa.getIdprog()));

        Picasso.with(mcon).load(programa.getImgPrograma()).into(holder.imgPrograma);
        //Picasso.with(mcon).setIndicatorsEnabled(true);
        //new main_programa_adapter.DownloadImageFromInternet((ImageView) holder.imgPrograma)
        //        .execute(programa.getImgPrograma());


        holder.imgPrograma.getLayoutParams().height = holder.imgPrograma.getLayoutParams().width;
    }

    @Override
    public int getItemCount() {
        return programaLista.size();
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

            return bimage;
            //return bimage.createScaledBitmap(bimage,bimage.getWidth(),bimage.getWidth(),true);
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}
