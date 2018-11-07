package com.albavision.ar.elnueve;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.albavision.ar.elnueve.adapters.main_noticia_adapter;
import com.albavision.ar.elnueve.adapters.programa_repeticiones_adapter;
import com.albavision.ar.elnueve.fragment.YoutubeFragment;
import com.albavision.ar.elnueve.models.main_noticia_model;
import com.albavision.ar.elnueve.models.programa_repeticiones_model;
import com.albavision.ar.elnueve.utils.AppSingleton;
import com.albavision.ar.elnueve.utils.Constant;
import com.albavision.ar.elnueve.utils.RecyclerItemClickListener;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetalleRepeticionActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private LinearLayout youTubeView,llmasNoticias,llmasRepeticiones;
    private ImageView imgLogo;
    private TextView txtPrograma,txtTitulo,txtResumen;
    private Boolean fullScreen;
    private YoutubeFragment fragment;


    private RecyclerView recycler_view_REPETICIONES;
    private RecyclerView.LayoutManager repeticionesLayoutManager;

    private List<programa_repeticiones_model> repeticionesLista;
    private programa_repeticiones_adapter repeticiones_adapter;



    private RecyclerView recycler_view_NOTICIAS;
    private RecyclerView.LayoutManager noticiasLayoutManager;

    private List<main_noticia_model> noticiasLista;
    private main_noticia_adapter noticias_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_repeticion);
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

        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        txtPrograma = (TextView) findViewById(R.id.txtPrograma);
        txtTitulo = (TextView) findViewById(R.id.txtTitulo);
        txtResumen = (TextView) findViewById(R.id.txtResumen);
        llmasNoticias = (LinearLayout) findViewById(R.id.llmasNoticias);
        llmasRepeticiones = (LinearLayout) findViewById(R.id.llmasRepeticiones);

        youTubeView = (LinearLayout) findViewById(R.id.youtube_view);

        youTubeView.setVisibility(View.VISIBLE);

        Bundle bundle = new Bundle();
        bundle.putString("ytvideoID", getIntent().getExtras().getString("stream"));

         fragment = new YoutubeFragment();


        fragment.setArguments(bundle);

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.youtube_view, fragment)
                .addToBackStack(null)
                .commit();

        Picasso.with(DetalleRepeticionActivity.this).load(getIntent().getExtras().getString("logo")).into(imgLogo);
        txtPrograma.setText(getIntent().getExtras().getString("nombre_programa"));
        txtTitulo.setText(getIntent().getExtras().getString("fecha"));
        txtResumen.setText(getIntent().getExtras().getString("titulo") + ' ' + getIntent().getExtras().getString("fecha"));
        //intent.putExtra("titulo", repeticionesLista.get(position).getTitulo());


        recycler_view_REPETICIONES = (RecyclerView) findViewById(R.id.recycler_view_REPETICIONES);
        if (recycler_view_REPETICIONES != null) {
            recycler_view_REPETICIONES.setHasFixedSize(true);
        }
        //programaLayoutManager = new LinearLayoutManager(this);
        //programaLayoutManager = new GridLayoutManager(this,1);
        repeticionesLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);


        recycler_view_REPETICIONES.setLayoutManager(repeticionesLayoutManager);
        repeticionesLista = new ArrayList<>();

        String urlProgramas = getIntent().getExtras().getString("repeticiones");
        urlProgramas= urlProgramas.concat("?ver=");
        urlProgramas= urlProgramas.concat(String.valueOf(ts));

        JsonObjectRequest jsonObjectReqProgs = new JsonObjectRequest(urlProgramas, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try{
                            JSONArray ja = response.getJSONArray("datos");
                            Integer Limite = 5;
                            if(ja.length() < Limite)
                                Limite = ja.length();
                            for(int i=0; i< Limite;i++){
                                JSONObject jsonObjectDato = ja.getJSONObject(i);
                                if(!jsonObjectDato.getString("nid").equals(String.valueOf(getIntent().getExtras().getInt("idprog")  ))) {
                                    String idprog = jsonObjectDato.getString("id_programa");
                                    String titulo = jsonObjectDato.getString("nombre_programa");
                                    String frecuencia = jsonObjectDato.getString("titulo");
                                    String urlImagen = jsonObjectDato.getString("imagen_316x202");
                                    String vod_yt = jsonObjectDato.getString("vod_yt");
                                    String vod_mp4 = jsonObjectDato.getString("vod_mp4");
                                    repeticionesLista.add(new programa_repeticiones_model(Integer.parseInt(idprog), frecuencia, titulo, urlImagen, vod_yt, vod_mp4, titulo));
                                }

                            }

                            repeticiones_adapter = new programa_repeticiones_adapter ( DetalleRepeticionActivity.this,repeticionesLista);

                            recycler_view_REPETICIONES.setAdapter(repeticiones_adapter);
                            repeticiones_adapter.notifyDataSetChanged();
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




        // ********** CLIPS

        recycler_view_NOTICIAS = (RecyclerView) findViewById(R.id.recycler_view_NOTICIAS);
        //Use this setting to improve performance if you know that changes in
        //the content do not change the layout size of the RecyclerView
        if (recycler_view_NOTICIAS != null) {
            recycler_view_NOTICIAS.setHasFixedSize(true);
        }
        //masnoticiasLayoutManager = new LinearLayoutManager(this);
        noticiasLayoutManager = new GridLayoutManager(this,2);
        //masnoticiasLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);


        recycler_view_NOTICIAS.setLayoutManager(noticiasLayoutManager);
        noticiasLista = new ArrayList<>();

        String urlClips = getIntent().getExtras().getString("noticias");
        urlClips= urlClips.concat("?ver=");
        urlClips= urlClips.concat(String.valueOf(ts));

        JsonObjectRequest jsonObjectReqMasNoticias = new JsonObjectRequest(urlClips, null,
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

                                noticiasLista.add(new main_noticia_model(encabezado,titulo,urlImagen,Integer.parseInt(nid),Integer.parseInt(codcat),fecha,url_imagen3,contenido,taxonomias,video, tipo));

                            }

                            noticias_adapter = new main_noticia_adapter( DetalleRepeticionActivity.this,noticiasLista);

                            recycler_view_NOTICIAS.setAdapter(noticias_adapter);
                            noticias_adapter.notifyDataSetChanged();
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






        recycler_view_REPETICIONES.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                String video = "";
                if(repeticionesLista.get(position).getVod_mp4().equals(""))
                {

                    Intent intent = new Intent(DetalleRepeticionActivity.this, DetalleRepeticionActivity.class);
                    intent.putExtra("from", "programadetalle");
                    video = repeticionesLista.get(position).getVod_yt();
                    intent.putExtra("stream", video);
                    intent.putExtra("fecha",  repeticionesLista.get(position).getFecha());
                    intent.putExtra("idprog", repeticionesLista.get(position).getIdprog());
                    intent.putExtra("imgPrograma", repeticionesLista.get(position).getImgPrograma());
                    intent.putExtra("nombre_programa", repeticionesLista.get(position).getNombre_programa());
                    intent.putExtra("titulo", repeticionesLista.get(position).getTitulo());
                    intent.putExtra("repeticiones",getIntent().getExtras().getString("repeticiones"));
                    intent.putExtra("clips",getIntent().getExtras().getString("clips"));
                    intent.putExtra("noticias",getIntent().getExtras().getString("noticias"));
                    intent.putExtra("logo", getIntent().getExtras().getString("logo"));
                    startActivity(intent);

                }else{
                    Intent intent = new Intent(DetalleRepeticionActivity.this, DetalleRepeticionActivity.class);
                    intent.putExtra("from", "programadetalle");
                    video = repeticionesLista.get(position).getVod_yt().toString().toString();
                    intent.putExtra("streamEnvivo", video);
                    intent.putExtra("fecha",  repeticionesLista.get(position).getFecha());
                    intent.putExtra("idprog", repeticionesLista.get(position).getIdprog());
                    intent.putExtra("imgPrograma", repeticionesLista.get(position).getImgPrograma());
                    intent.putExtra("nombre_programa", repeticionesLista.get(position).getNombre_programa());
                    intent.putExtra("titulo", repeticionesLista.get(position).getTitulo());
                    intent.putExtra("repeticiones",getIntent().getExtras().getString("repeticiones"));
                    intent.putExtra("clips",getIntent().getExtras().getString("clips"));
                    intent.putExtra("noticias",getIntent().getExtras().getString("noticias"));
                    intent.putExtra("logo", getIntent().getExtras().getString("logo"));
                    startActivity(intent);
                }

            }
        }));




        recycler_view_NOTICIAS.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Toast.makeText(MainActivity.this, "Card at " + position + " is clicked", Toast.LENGTH_SHORT).show();
                //Toast.makeText(MainActivity.this, "resumen: " + masnoticiasLista.get(position).getResumen(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(DetalleRepeticionActivity.this, DetalleNoticiaActivity.class);
                intent.putExtra("idnoticia", noticiasLista.get(position).getCodigo().toString());
                intent.putExtra("titulo", noticiasLista.get(position).getTitulo().toString());
                intent.putExtra("tipo", noticiasLista.get(position).getTipo().toString());
                intent.putExtra("fecha", noticiasLista.get(position).getFecha().toString());
                intent.putExtra("imagen", noticiasLista.get(position).getUrl_imagen3());
                intent.putExtra("contenido", noticiasLista.get(position).getContenido());
                intent.putExtra("taxonomias", noticiasLista.get(position).getTaxonomias().toString());
                intent.putExtra("video", noticiasLista.get(position).getVideo().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }));



        llmasNoticias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetalleRepeticionActivity.this, ResultadoActivity.class);
                intent.putExtra("id", "1");
                intent.putExtra("titulo", "Noticias");
                intent.putExtra("ruta", Constant.MenuTaxonomiasFijas_Noticias);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        llmasRepeticiones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetalleRepeticionActivity.this, MasRepeticionesActivity.class);
                intent.putExtra("id", "1");
                intent.putExtra("titulo", "Repeticiones");
                intent.putExtra("imagen", getIntent().getExtras().getString("imagen"));
                intent.putExtra("idprog",getIntent().getExtras().getString("idprog").toString());
                intent.putExtra("repeticiones",  getIntent().getExtras().getString("repeticiones"));
                intent.putExtra("clips",getIntent().getExtras().getString("clips"));
                intent.putExtra("noticias",getIntent().getExtras().getString("noticias"));
                intent.putExtra("logo", getIntent().getExtras().getString("logo"));

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


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
            Intent intent = new Intent(DetalleRepeticionActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_Combate) {
            Intent intent = new Intent(DetalleRepeticionActivity.this, ProgramaDetalleActivity.class);
            intent.putExtra("idprograma",24);
            intent.putExtra("titulo","Combate");
            intent.putExtra("imagen","http://cdn.elnueve.com.ar/files/2017/08/22/170818_Combate_20x150.jpg");
            intent.putExtra("noticias","http://cdn.elnueve.com.ar/movil/noticias/23.js");
            intent.putExtra("clips","http://cdn.elnueve.com.ar/movil/clips/23.js");
            intent.putExtra("repeticiones","http://cdn.elnueve.com.ar/movil/repeticiones/23.js");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_Tl9) {
            Intent intent = new Intent(DetalleRepeticionActivity.this, ProgramaDetalleActivity.class);
            intent.putExtra("idprograma",20566);
            intent.putExtra("titulo","TL9");
            intent.putExtra("imagen","http://cdn.elnueve.com.ar/files/2017/05/12/170512_Amanecer_320x150.jpg");
            intent.putExtra("noticias","http://cdn.elnueve.com.ar/movil/noticias/9388.js");
            intent.putExtra("clips","http://cdn.elnueve.com.ar/movil/clips/9388.js");
            intent.putExtra("repeticiones","http://cdn.elnueve.com.ar/movil/repeticiones/9388.js");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_23pm) {
            Intent intent = new Intent(DetalleRepeticionActivity.this, ProgramaDetalleActivity.class);
            intent.putExtra("idprograma",46095);
            intent.putExtra("titulo","23 PM");
            intent.putExtra("imagen","http://cdn.elnueve.com.ar/files/2017/03/14/170314_23PM_320x150.jpg");
            intent.putExtra("noticias","http://cdn.elnueve.com.ar/movil/noticias/8202.js");
            intent.putExtra("clips","http://cdn.elnueve.com.ar/movil/clips/8202.js");
            intent.putExtra("repeticiones","http://cdn.elnueve.com.ar/movil/repeticiones/8202.js");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_programas) {
            Intent intent = new Intent(DetalleRepeticionActivity.this, ProgramasActivity.class);
            intent.putExtra("from", "inicio");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_noticias) {
            Intent intent = new Intent(DetalleRepeticionActivity.this, ResultadoActivity.class);
            intent.putExtra("id", "1");
            intent.putExtra("titulo", "Noticias");
            intent.putExtra("ruta", Constant.MenuTaxonomiasFijas_Noticias);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_espectaculos) {
            Intent intent = new Intent(DetalleRepeticionActivity.this, ResultadoActivity.class);
            intent.putExtra("id", "2");
            intent.putExtra("titulo", "Espectáculos");
            intent.putExtra("ruta", Constant.MenuTaxonomiasFijas_Espectaculos);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_deportes) {
            Intent intent = new Intent(DetalleRepeticionActivity.this, ResultadoActivity.class);
            intent.putExtra("id", "3");
            intent.putExtra("titulo", "Deportes");
            intent.putExtra("ruta", Constant.MenuTaxonomiasFijas_Deportes);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_comunidad) {
            Intent intent = new Intent(DetalleRepeticionActivity.this, ResultadoActivity.class);
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
