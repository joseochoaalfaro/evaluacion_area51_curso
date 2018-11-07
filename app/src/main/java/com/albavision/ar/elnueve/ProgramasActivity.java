package com.albavision.ar.elnueve;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.albavision.ar.elnueve.adapters.main_programa_adapter;
import com.albavision.ar.elnueve.models.main_programa_model;
import com.albavision.ar.elnueve.utils.AppSingleton;
import com.albavision.ar.elnueve.utils.Constant;
import com.albavision.ar.elnueve.utils.RecyclerItemClickListener;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProgramasActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView programaRecyclerView;
    private RecyclerView.LayoutManager programaLayoutManager;

    private List<main_programa_model> programaLista;
    private main_programa_adapter programa_adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programas);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);//eliminar el título del actionbar

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);





        final String TAG = "XXXXXXXXXXXXXXXXXXXX";
        String  REQUEST_TAG = "com.albavision.ar.elnueve";
        int ts = (int) (new Date().getTime()/1000);


        programaRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_PROGRAMAS_LISTADO);
        if (programaRecyclerView != null) {
            programaRecyclerView.setHasFixedSize(true);
        }
        //programaLayoutManager = new LinearLayoutManager(this);
        //programaLayoutManager = new GridLayoutManager(this,1);
        programaLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);


        programaRecyclerView.setLayoutManager(programaLayoutManager);
        programaLista = new ArrayList<>();

        String urlProgramas = Constant.ListadoProgramas;
        urlProgramas= urlProgramas.concat("?ver=");
        urlProgramas= urlProgramas.concat(String.valueOf(ts));

        JsonObjectRequest jsonObjectReqProgs = new JsonObjectRequest(urlProgramas, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try{
                            JSONArray ja = response.getJSONArray("datos");

                            for(int i=0; i< ja.length();i++){
                                JSONObject jsonObjectDato = ja.getJSONObject(i);
                                String idprog = jsonObjectDato.getString("nid");
                                String titulo = jsonObjectDato.getString("titulo");
                                String logo = jsonObjectDato.getString("logo");
                                String frecuencia = jsonObjectDato.getString("frecuencia") + " " + jsonObjectDato.getString("horario");
                                String urlImagen = jsonObjectDato.getString("imagen_programa");
                                String noticias = jsonObjectDato.getString("noticias");
                                String clips = jsonObjectDato.getString("clips");
                                String repeticiones = jsonObjectDato.getString("repeticiones");
                                programaLista.add(new main_programa_model(Integer.parseInt(idprog),titulo,logo,frecuencia,urlImagen,noticias,clips,repeticiones));

                            }

                            programa_adapter = new main_programa_adapter ( ProgramasActivity.this,programaLista);

                            programaRecyclerView.setAdapter(programa_adapter);
                            programa_adapter.notifyDataSetChanged();

                            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                        }
                        catch (Exception e){
                            Toast.makeText(getApplicationContext(),"ERROR llamado:" + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

            }
        });

        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectReqProgs,REQUEST_TAG);







        programaRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Toast.makeText(MainActivity.this, "Card at " + position + " is clicked", Toast.LENGTH_SHORT).show();
                //Toast.makeText(MainActivity.this, "programa: " + programaLista.get(position).getTituloProg(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ProgramasActivity.this, ProgramaDetalleActivity.class);
                intent.putExtra("idprog",programaLista.get(position).getIdprog().toString());
                intent.putExtra("titulo",programaLista.get(position).getTituloProg());
                intent.putExtra("logo",programaLista.get(position).getLogo());
                intent.putExtra("imagen",programaLista.get(position).getImgPrograma());
                intent.putExtra("noticias",programaLista.get(position).getNoticias());
                intent.putExtra("clips",programaLista.get(position).getClips());
                intent.putExtra("repeticiones",programaLista.get(position).getRepeticiones());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }));

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();




        if (id == R.id.nav_inicio) {
            Intent intent = new Intent(ProgramasActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_Combate) {
            Intent intent = new Intent(ProgramasActivity.this, ProgramaDetalleActivity.class);
            intent.putExtra("idprograma",24);
            intent.putExtra("titulo","Combate");
            intent.putExtra("imagen","http://cdn.elnueve.com.ar/files/2017/08/22/170818_Combate_20x150.jpg");
            intent.putExtra("noticias","http://cdn.elnueve.com.ar/movil/noticias/23.js");
            intent.putExtra("clips","http://cdn.elnueve.com.ar/movil/clips/23.js");
            intent.putExtra("repeticiones","http://cdn.elnueve.com.ar/movil/repeticiones/23.js");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_Tl9) {
            Intent intent = new Intent(ProgramasActivity.this, ProgramaDetalleActivity.class);
            intent.putExtra("idprograma",20566);
            intent.putExtra("titulo","TL9");
            intent.putExtra("imagen","http://cdn.elnueve.com.ar/files/2017/05/12/170512_Amanecer_320x150.jpg");
            intent.putExtra("noticias","http://cdn.elnueve.com.ar/movil/noticias/9388.js");
            intent.putExtra("clips","http://cdn.elnueve.com.ar/movil/clips/9388.js");
            intent.putExtra("repeticiones","http://cdn.elnueve.com.ar/movil/repeticiones/9388.js");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_23pm) {
            Intent intent = new Intent(ProgramasActivity.this, ProgramaDetalleActivity.class);
            intent.putExtra("idprograma",46095);
            intent.putExtra("titulo","23 PM");
            intent.putExtra("imagen","http://cdn.elnueve.com.ar/files/2017/03/14/170314_23PM_320x150.jpg");
            intent.putExtra("noticias","http://cdn.elnueve.com.ar/movil/noticias/8202.js");
            intent.putExtra("clips","http://cdn.elnueve.com.ar/movil/clips/8202.js");
            intent.putExtra("repeticiones","http://cdn.elnueve.com.ar/movil/repeticiones/8202.js");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_programas) {
            Intent intent = new Intent(ProgramasActivity.this, ProgramasActivity.class);
            intent.putExtra("from", "inicio");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_noticias) {
            Intent intent = new Intent(ProgramasActivity.this, ResultadoActivity.class);
            intent.putExtra("id", "1");
            intent.putExtra("titulo", "Noticias");
            intent.putExtra("ruta", Constant.MenuTaxonomiasFijas_Noticias);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_espectaculos) {
            Intent intent = new Intent(ProgramasActivity.this, ResultadoActivity.class);
            intent.putExtra("id", "2");
            intent.putExtra("titulo", "Espectáculos");
            intent.putExtra("ruta", Constant.MenuTaxonomiasFijas_Espectaculos);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_deportes) {
            Intent intent = new Intent(ProgramasActivity.this, ResultadoActivity.class);
            intent.putExtra("id", "3");
            intent.putExtra("titulo", "Deportes");
            intent.putExtra("ruta", Constant.MenuTaxonomiasFijas_Deportes);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_comunidad) {
            Intent intent = new Intent(ProgramasActivity.this, ResultadoActivity.class);
            intent.putExtra("id", "188");
            intent.putExtra("titulo", "Comunidad");
            intent.putExtra("ruta", Constant.MenuTaxonomiasFijas_Comunidad);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }






    public void volleyStringRequst(String url){
        final String TAG = "XXXXXXXXXXXXXXXXXXXX";
        String  REQUEST_TAG = "com.albavision.ar.elnueve";

        int ts = (int) (new Date().getTime()/1000);
        url= url.concat("?ver=");
        url= url.concat(String.valueOf(ts));

        //Log.d(TAG, url.toString());

        StringRequest strReq = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());

                //progressDialog.hide();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Log.d(TAG, error.getMessage().toString());
                //progressDialog.hide();
            }
        });
        // Adding String request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, REQUEST_TAG);
    }


    public void volleyJsonObjectRequest(String url){
        final String TAG = "XXXXXXXXXXXXXXXXXXXX";
        String  REQUEST_TAG = "com.albavision.ar.elnueve";

        int ts = (int) (new Date().getTime()/1000);
        url= url.concat("?ver=");
        url= url.concat(String.valueOf(ts));

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

            }
        });

        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectReq,REQUEST_TAG);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //volleyStringRequst(Constant.ListaMainNoticiasPrincipales);
    }
}
