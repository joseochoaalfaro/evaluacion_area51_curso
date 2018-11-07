/*
cambio de color de icono:
        https://stackoverflow.com/questions/38974445/android-studio-navigation-drawer-icon-color
*/

package com.albavision.ar.elnueve;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.albavision.ar.elnueve.adapters.main_noticia_adapter;
import com.albavision.ar.elnueve.adapters.main_programa_adapter;
import com.albavision.ar.elnueve.models.main_noticia_model;
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
import java.util.List;

import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private LinearLayout llMainPlayer;
    private String streamEnvivo;

    private LinearLayout llmasProgramas;
    private LinearLayout llmasNoticias;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<main_noticia_model> noticiaLista;
    private main_noticia_adapter noticia_adapter;



    private RecyclerView programaRecyclerView;
    private RecyclerView.LayoutManager programaLayoutManager;

    private List<main_programa_model> programaLista;
    private main_programa_adapter programa_adapter;



    private RecyclerView masnoticiasRecyclerView;
    private RecyclerView.LayoutManager masnoticiasLayoutManager;

    private List<main_noticia_model> masnoticiasLista;
    private main_noticia_adapter masnoticias_adapter;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);//eliminar el título del actionbar
/*

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/

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

        llMainPlayer = (LinearLayout) findViewById(R.id.llMainPlayer);
        llmasProgramas = (LinearLayout) findViewById(R.id.llmasProgramas);
        llmasNoticias = (LinearLayout) findViewById(R.id.llmasNoticias);




        String urlENVIVO = Constant.MainENVIVO;

        urlENVIVO= urlENVIVO.concat("?ver=");
        urlENVIVO= urlENVIVO.concat(String.valueOf(ts));

        JsonObjectRequest jsonObjectReqENVIVO = new JsonObjectRequest(urlENVIVO, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try{
                            JSONObject jdatos = response.getJSONObject("datos");
                            JSONObject jo = jdatos.getJSONObject("programacion");
                            String onLine = jo.getString("online");
                            if(onLine.equals("2")){
                                llMainPlayer.setVisibility(View.GONE);
                            }else {
                                JSONArray arrPlayers = jdatos.getJSONArray("players");
                                JSONObject objPlayer = arrPlayers.getJSONObject(0);
                                //String cadPlayer = objPlayer.getString("web");
                                String cadPlayer = objPlayer.getString("movil");
                                streamEnvivo = cadPlayer;
                                //Toast.makeText(getApplicationContext(), "PLAYER:" + cadPlayer.toString(), Toast.LENGTH_LONG).show();
                            }
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
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectReqENVIVO,REQUEST_TAG);


        llMainPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VideoFullscreenActivity.class);
                intent.putExtra("from", "inicio");
                intent.putExtra("streamEnvivo", streamEnvivo);
                startActivity(intent);
                //finish();
            }
        });


        llmasProgramas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProgramasActivity.class);
                intent.putExtra("from", "inicio");
                intent.putExtra("streamEnvivo", streamEnvivo);
                startActivity(intent);
            }
        });





        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_NOTICIAS_PRINCIPALES);
        //Use this setting to improve performance if you know that changes in
        //the content do not change the layout size of the RecyclerView
        if (mRecyclerView != null) {
            mRecyclerView.setHasFixedSize(true);
        }
        //mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager = new GridLayoutManager(this,2);
        //mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);


        mRecyclerView.setLayoutManager(mLayoutManager);

        noticiaLista = new ArrayList<>();




        String url = Constant.ListaMainNoticiasPrincipales;
        //int ts = (int) (new Date().getTime()/1000);
        url= url.concat("?ver=");
        url= url.concat(String.valueOf(ts));

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try{
                            JSONArray ja = response.getJSONArray("datos");

                            Integer Limite = 4;
                            if(ja.length() < Limite)
                                Limite = ja.length();
                            for(int i=0; i< Limite;i++){
                                JSONObject jsonObjectDato = ja.getJSONObject(i);
                                String nid = jsonObjectDato.getString("nid");
                                String titulo = jsonObjectDato.getString("titulo");
                                String tipo = jsonObjectDato.getString("tipo");
                                String encabezado = jsonObjectDato.getString("nombre_categoria");
                                //encabezado = encabezado.concat(" / ");
                                //encabezado = encabezado.concat(jsonObjectDato.getString("nombre_programa"));
                                String codcat = jsonObjectDato.getString("id_categoria");
                                String urlImagen = jsonObjectDato.getString("imagen_316x202");
                                String fecha = jsonObjectDato.getString("fecha");
                                String url_imagen3 = jsonObjectDato.getString("imagen");
                                String contenido = jsonObjectDato.getString("cuerpo");
                                JSONArray taxonomias = jsonObjectDato.getJSONArray("taxonomias");

                                String video = "";
                                if(jsonObjectDato.getString("video_mp4").equals(""))
                                {
                                    video = ("https://www.youtube.com/watch?v=").concat(jsonObjectDato.getString("video_yt"));
                                }else{
                                     video = jsonObjectDato.getString("video_mp4");
                                }

                                noticiaLista.add(new main_noticia_model(encabezado,titulo,urlImagen,Integer.parseInt(nid),Integer.parseInt(codcat),fecha,url_imagen3,contenido,taxonomias,video,tipo));

                            }

                            noticia_adapter = new main_noticia_adapter ( MainActivity.this,noticiaLista);

                            mRecyclerView.setAdapter(noticia_adapter);
                            noticia_adapter.notifyDataSetChanged();
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
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectReq,REQUEST_TAG);



        // ********** PROGRAMAS

        programaRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_PROGRAMAS_PRINCIPALES);
        //Use this setting to improve performance if you know that changes in
        //the content do not change the layout size of the RecyclerView
        if (programaRecyclerView != null) {
            programaRecyclerView.setHasFixedSize(true);
        }
        //programaLayoutManager = new LinearLayoutManager(this);
        //programaLayoutManager = new GridLayoutManager(this,1);
        programaLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);


        programaRecyclerView.setLayoutManager(programaLayoutManager);
        programaLista = new ArrayList<>();

        String urlProgramas = Constant.ListaMainProgramas;
        urlProgramas= urlProgramas.concat("?ver=");
        urlProgramas= urlProgramas.concat(String.valueOf(ts));

        JsonObjectRequest jsonObjectReqProgs = new JsonObjectRequest(urlProgramas, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try{
                            JSONArray ja = response.getJSONArray("datos");

                            Integer Limite = 3;
                            if(ja.length() < Limite)
                                Limite = ja.length();
                            for(int i=0; i< Limite;i++){
                                JSONObject jsonObjectDato = ja.getJSONObject(i);
                                //String idprog = jsonObjectDato.getString("nid");
                                String idprog = jsonObjectDato.getString("id_programa");
                                String titulo = jsonObjectDato.getString("titulo");
                                String logo = jsonObjectDato.getString("logo");
                                String frecuencia = jsonObjectDato.getString("frecuencia") + " " + jsonObjectDato.getString("horario");
                                String urlImagen = jsonObjectDato.getString("imagen_programa");
                                String noticias = jsonObjectDato.getString("noticias");
                                String clips = jsonObjectDato.getString("clips");
                                String repeticiones = jsonObjectDato.getString("repeticiones");
                                programaLista.add(new main_programa_model(Integer.parseInt(idprog),titulo,logo,frecuencia,urlImagen,noticias,clips,repeticiones));

                            }

                            programa_adapter = new main_programa_adapter ( MainActivity.this,programaLista);

                            programaRecyclerView.setAdapter(programa_adapter);
                            programa_adapter.notifyDataSetChanged();

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




        // ********** MAS NOTICIAS

        masnoticiasRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_MASNOTICIAS_PRINCIPALES);
        //Use this setting to improve performance if you know that changes in
        //the content do not change the layout size of the RecyclerView
        if (masnoticiasRecyclerView != null) {
            masnoticiasRecyclerView.setHasFixedSize(true);
        }
        //masnoticiasLayoutManager = new LinearLayoutManager(this);
        masnoticiasLayoutManager = new GridLayoutManager(this,2);
        //masnoticiasLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);


        masnoticiasRecyclerView.setLayoutManager(masnoticiasLayoutManager);
        masnoticiasLista = new ArrayList<>();

        String urlMasNoticias = Constant.ListaMainNoticiasSecundarias;
        urlMasNoticias= urlMasNoticias.concat("?ver=");
        urlMasNoticias= urlMasNoticias.concat(String.valueOf(ts));

        JsonObjectRequest jsonObjectReqMasNoticias = new JsonObjectRequest(urlMasNoticias, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try{
                            JSONArray ja = response.getJSONArray("datos");

                            Integer Limite = 8;
                            if(ja.length() < Limite)
                                Limite = ja.length();
                            for(int i=0; i< Limite;i++){
                                JSONObject jsonObjectDato = ja.getJSONObject(i);
                                String nid = jsonObjectDato.getString("nid");
                                String titulo = jsonObjectDato.getString("titulo");
                                String tipo = jsonObjectDato.getString("tipo");
                                String encabezado = jsonObjectDato.getString("nombre_categoria");
                                String codcat = jsonObjectDato.getString("id_categoria");
                                String urlImagen = jsonObjectDato.getString("imagen_316x202");
                                String fecha = jsonObjectDato.getString("fecha");
                                String url_imagen3 = jsonObjectDato.getString("imagen");
                                String contenido = jsonObjectDato.getString("cuerpo");
                                JSONArray taxonomias = jsonObjectDato.getJSONArray("taxonomias");

                                String video = "";
                                if(jsonObjectDato.getString("video_mp4").equals(""))
                                {
                                    video = ("https://www.youtube.com/watch?v=").concat(jsonObjectDato.getString("video_yt"));
                                }else{
                                    video = jsonObjectDato.getString("video_mp4");
                                }

                                masnoticiasLista.add(new main_noticia_model(encabezado,titulo,urlImagen,Integer.parseInt(nid),Integer.parseInt(codcat),fecha,url_imagen3,contenido,taxonomias,video, tipo));

                            }

                            masnoticias_adapter = new main_noticia_adapter ( MainActivity.this,masnoticiasLista);

                            masnoticiasRecyclerView.setAdapter(masnoticias_adapter);
                            masnoticias_adapter.notifyDataSetChanged();
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
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectReqMasNoticias,REQUEST_TAG);







        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Toast.makeText(MainActivity.this, "Card at " + position + " is clicked" , Toast.LENGTH_SHORT).show();
                //Toast.makeText(MainActivity.this, "resumen: " + noticiaLista.get(position).getResumen(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, DetalleNoticiaActivity.class);
                intent.putExtra("idnoticia", noticiaLista.get(position).getCodigo().toString());
                intent.putExtra("titulo", noticiaLista.get(position).getTitulo().toString());
                intent.putExtra("tipo", noticiaLista.get(position).getTipo().toString());
                intent.putExtra("fecha", noticiaLista.get(position).getFecha().toString());
                intent.putExtra("imagen", noticiaLista.get(position).getUrl_imagen3());
                intent.putExtra("contenido", noticiaLista.get(position).getContenido());
                intent.putExtra("taxonomias", noticiaLista.get(position).getTaxonomias().toString());
                intent.putExtra("video", noticiaLista.get(position).getVideo().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //finish();
            }
        }));

        programaRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Toast.makeText(MainActivity.this, "Card at " + position + " is clicked", Toast.LENGTH_SHORT).show();
                //Toast.makeText(MainActivity.this, "programa: " + programaLista.get(position).getTituloProg(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, ProgramaDetalleActivity.class);
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

        masnoticiasRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Toast.makeText(MainActivity.this, "Card at " + position + " is clicked", Toast.LENGTH_SHORT).show();
                //Toast.makeText(MainActivity.this, "resumen: " + masnoticiasLista.get(position).getResumen(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, DetalleNoticiaActivity.class);
                intent.putExtra("idnoticia", masnoticiasLista.get(position).getCodigo().toString());
                intent.putExtra("titulo", masnoticiasLista.get(position).getTitulo().toString());
                intent.putExtra("tipo", masnoticiasLista.get(position).getTipo().toString());
                intent.putExtra("fecha", masnoticiasLista.get(position).getFecha().toString());
                intent.putExtra("imagen", masnoticiasLista.get(position).getUrl_imagen3());
                intent.putExtra("contenido", masnoticiasLista.get(position).getContenido());
                intent.putExtra("taxonomias", masnoticiasLista.get(position).getTaxonomias().toString());
                intent.putExtra("video", masnoticiasLista.get(position).getVideo().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }));






        List<String> arrProgramas = new ArrayList<String>();
        arrProgramas.add("23 PM");
        arrProgramas.add("Bendita");
        arrProgramas.add("Campeones");
        arrProgramas.add("Caramelito");
        arrProgramas.add("Clave Argentina");

        //ExpandableListView expandableListView;
        //expandableListView = (ExpandableListView) navigationView.getMenu().findItem(R.id.navigation_drawer_programas).getActionView();
        //expandableListView.setAdapter();
        //expandableListView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.expandable_list_content,arrProgramas));

        /*
        Spinner spinner = (Spinner) navigationView.getMenu().findItem(R.id.navigation_drawer_programas).getActionView();
        spinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,arrProgramas));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainActivity.this,arrProgramas[position],Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
*/







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




        if (id == R.id.nav_Combate) {
            Intent intent = new Intent(MainActivity.this, ProgramaDetalleActivity.class);
            intent.putExtra("idprograma",24);
            intent.putExtra("titulo","Combate");
            intent.putExtra("imagen","http://cdn.elnueve.com.ar/files/2017/08/22/170818_Combate_20x150.jpg");
            intent.putExtra("noticias","http://cdn.elnueve.com.ar/movil/noticias/23.js");
            intent.putExtra("clips","http://cdn.elnueve.com.ar/movil/clips/23.js");
            intent.putExtra("repeticiones","http://cdn.elnueve.com.ar/movil/repeticiones/23.js");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_Tl9) {
            Intent intent = new Intent(MainActivity.this, ProgramaDetalleActivity.class);
            intent.putExtra("idprograma",20566);
            intent.putExtra("titulo","TL9");
            intent.putExtra("imagen","http://cdn.elnueve.com.ar/files/2017/05/12/170512_Amanecer_320x150.jpg");
            intent.putExtra("noticias","http://cdn.elnueve.com.ar/movil/noticias/9388.js");
            intent.putExtra("clips","http://cdn.elnueve.com.ar/movil/clips/9388.js");
            intent.putExtra("repeticiones","http://cdn.elnueve.com.ar/movil/repeticiones/9388.js");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_23pm) {
            Intent intent = new Intent(MainActivity.this, ProgramaDetalleActivity.class);
            intent.putExtra("idprograma",46095);
            intent.putExtra("titulo","23 PM");
            intent.putExtra("imagen","http://cdn.elnueve.com.ar/files/2017/03/14/170314_23PM_320x150.jpg");
            intent.putExtra("noticias","http://cdn.elnueve.com.ar/movil/noticias/8202.js");
            intent.putExtra("clips","http://cdn.elnueve.com.ar/movil/clips/8202.js");
            intent.putExtra("repeticiones","http://cdn.elnueve.com.ar/movil/repeticiones/8202.js");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_programas) {
            Intent intent = new Intent(MainActivity.this, ProgramasActivity.class);
            intent.putExtra("from", "inicio");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_noticias) {
            Intent intent = new Intent(MainActivity.this, ResultadoActivity.class);
            intent.putExtra("id", "1");
            intent.putExtra("titulo", "Noticias");
            intent.putExtra("ruta", Constant.MenuTaxonomiasFijas_Noticias);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_espectaculos) {
            Intent intent = new Intent(MainActivity.this, ResultadoActivity.class);
            intent.putExtra("id", "2");
            intent.putExtra("titulo", "Espectáculos");
            intent.putExtra("ruta", Constant.MenuTaxonomiasFijas_Espectaculos);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_deportes) {
            Intent intent = new Intent(MainActivity.this, ResultadoActivity.class);
            intent.putExtra("id", "3");
            intent.putExtra("titulo", "Deportes");
            intent.putExtra("ruta", Constant.MenuTaxonomiasFijas_Deportes);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_comunidad) {
            Intent intent = new Intent(MainActivity.this, ResultadoActivity.class);
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
