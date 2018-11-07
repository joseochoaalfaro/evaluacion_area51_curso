package com.albavision.ar.elnueve.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.albavision.ar.elnueve.R;
import com.albavision.ar.elnueve.models.detalle_taxonomia_model;
import com.albavision.ar.elnueve.models.main_noticia_model;

import java.util.List;

/**
 * Created by yoshi on 18/08/17.
 */

public class detalle_taxonomia_adapter  extends RecyclerView.Adapter<detalle_taxonomia_adapter.ViewHolder>  {
    private List<detalle_taxonomia_model> taxonomiaLista;
    private Context mcon;

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtIdTaxonomia, txtTaxonomia;

        public ViewHolder(View v){
            super(v);
            txtIdTaxonomia = (TextView)v.findViewById(R.id.txtIdTaxonomia);
            txtTaxonomia = (TextView)v.findViewById(R.id.txtTaxonomia);
        }
    }

    public detalle_taxonomia_adapter(Context con, List<detalle_taxonomia_model> taxonomiaLista){
        mcon = con;
        this.taxonomiaLista = taxonomiaLista;
    }

    @Override
    public detalle_taxonomia_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detalle_taxonomia,parent,false);

        detalle_taxonomia_adapter.ViewHolder vh = new detalle_taxonomia_adapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(detalle_taxonomia_adapter.ViewHolder holder,int position){
        detalle_taxonomia_model taxonomia = taxonomiaLista.get(position);
        holder.txtIdTaxonomia.setText(String.valueOf(taxonomia.getIdTaxonomia()));
        holder.txtTaxonomia.setText(String.valueOf(taxonomia.getNombre()));
    }

    @Override
    public int getItemCount() {
        return taxonomiaLista.size();
    }

}
