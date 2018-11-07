package com.albavision.ar.elnueve;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.albavision.ar.elnueve.adapters.main_noticia_adapter;
import com.albavision.ar.elnueve.adapters.programa_repeticiones_adapter;
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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MasRepeticionesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {


    private ImageView imgPrograma;
    private Button bntBuscar;
    private DatePicker dpResult;
    private LinearLayout llmasNoticias,buscarFecha,llBuscador;
    private String FechaBuscar="";


    private RecyclerView recycler_view_REPETICIONES;
    private RecyclerView.LayoutManager repeticionesLayoutManager;

    private List<programa_repeticiones_model> repeticionesLista;
    private programa_repeticiones_adapter repeticiones_adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mas_repeticiones);

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



        buscarFecha = (LinearLayout) findViewById(R.id.buscarFecha);
        llBuscador = (LinearLayout) findViewById(R.id.llBuscador);
        bntBuscar = (Button) findViewById(R.id.bntBuscar);
        dpResult = (DatePicker) findViewById(R.id.dpResult);


        final String TAG = "XXXXXXXXXXXXXXXXXXXX";
        String  REQUEST_TAG = "com.albavision.ar.elnueve";
        int ts = (int) (new Date().getTime()/1000);


        imgPrograma = (ImageView) findViewById(R.id.imgPrograma);
        Picasso.with(this).load(getIntent().getExtras().getString("imagen")).into(imgPrograma);

        //new ProgramaDetalleActivity.DownloadImageFromInternet( imgPrograma)
        //        .execute(getIntent().getExtras().getString("imagen"));


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
                            for(int i=0; i< ja.length();i++){
                                JSONObject jsonObjectDato = ja.getJSONObject(i);
                                String idprog = jsonObjectDato.getString("nid");
                                String titulo = jsonObjectDato.getString("nombre_programa");
                                String frecuencia = jsonObjectDato.getString("titulo") ;
                                String urlImagen = jsonObjectDato.getString("imagen_316x202");
                                String vod_yt = jsonObjectDato.getString("vod_yt");
                                String vod_mp4 = jsonObjectDato.getString("vod_mp4");
                                repeticionesLista.add(new programa_repeticiones_model(Integer.parseInt(idprog),frecuencia, titulo,urlImagen,vod_yt,vod_mp4,titulo));

                            }

                            repeticiones_adapter = new programa_repeticiones_adapter ( MasRepeticionesActivity.this,repeticionesLista);

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




        recycler_view_REPETICIONES.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                String video = "";
                if(repeticionesLista.get(position).getVod_mp4().equals(""))
                {

                    Intent intent = new Intent(MasRepeticionesActivity.this, DetalleRepeticionActivity.class);
                    intent.putExtra("from", "programadetalle");
                    video = repeticionesLista.get(position).getVod_yt();
                    intent.putExtra("stream", video);
                    intent.putExtra("fecha",  repeticionesLista.get(position).getFecha());
                    intent.putExtra("idprog", repeticionesLista.get(position).getIdprog().toString());
                    intent.putExtra("imgPrograma", repeticionesLista.get(position).getImgPrograma());
                    intent.putExtra("nombre_programa", repeticionesLista.get(position).getNombre_programa());
                    intent.putExtra("titulo", repeticionesLista.get(position).getTitulo());
                    intent.putExtra("repeticiones",getIntent().getExtras().getString("repeticiones"));
                    intent.putExtra("clips",getIntent().getExtras().getString("clips"));
                    intent.putExtra("noticias",getIntent().getExtras().getString("noticias"));
                    intent.putExtra("logo", getIntent().getExtras().getString("logo"));
                    startActivity(intent);

                }else{
                    Intent intent = new Intent(MasRepeticionesActivity.this, DetalleRepeticionActivity.class);
                    intent.putExtra("from", "programadetalle");
                    video = repeticionesLista.get(position).getVod_yt().toString().toString();
                    intent.putExtra("streamEnvivo", video);
                    intent.putExtra("fecha",  repeticionesLista.get(position).getFecha());
                    intent.putExtra("idprog", repeticionesLista.get(position).getIdprog().toString());
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




        buscarFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llBuscador.setVisibility(View.VISIBLE);
                bntBuscar.requestFocus();
                buscarFecha.setVisibility(View.GONE);
            }
        });


        bntBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FechaBuscar = String.valueOf(dpResult.getYear()) + "-" + String.valueOf(dpResult.getMonth() + 1) + "-" + String.valueOf(dpResult.getDayOfMonth());
                cargarFecha(FechaBuscar);

                llBuscador.setVisibility(View.GONE);
                buscarFecha.setVisibility(View.VISIBLE);
                buscarFecha.requestFocus();
            }
        });
    }


    private void cargarFecha(String fecha){
        // http://www.elnueve.com.ar/cign/index.php/nodos/recuperarNoticias/id_taxonomia/fecha_busqueda

        repeticionesLista.clear();
        final String TAG = "XXXXXXXXXXXXXXXXXXXX";
        String  REQUEST_TAG = "com.albavision.ar.elnueve";


        String urlBuscaNoticias = "http://www.elnueve.com.ar/cign/index.php/nodos/recuperarRepeticiones/";
        urlBuscaNoticias = urlBuscaNoticias.concat(getIntent().getExtras().getString("idprog").toString());
        urlBuscaNoticias = urlBuscaNoticias.concat("/");
        urlBuscaNoticias = urlBuscaNoticias.concat(fecha);
        //urlMasNoticias= urlMasNoticias.concat("?ver=");
        //urlMasNoticias= urlMasNoticias.concat(String.valueOf(ts));

        JsonObjectRequest jsonObjectReqMasNoticias = new JsonObjectRequest(urlBuscaNoticias, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try{
                            JSONArray ja = response.getJSONArray("datos");


                            for(int i=0; i< ja.length();i++){
                                JSONObject jsonObjectDato = ja.getJSONObject(i);
                                String idprog = jsonObjectDato.getString("nid");
                                String titulo = jsonObjectDato.getString("nombre_programa");
                                String frecuencia = jsonObjectDato.getString("titulo") ;
                                String urlImagen = jsonObjectDato.getString("imagen_316x202");
                                String vod_yt = jsonObjectDato.getString("vod_yt");
                                String vod_mp4 = jsonObjectDato.getString("vod_mp4");
                                repeticionesLista.add(new programa_repeticiones_model(Integer.parseInt(idprog),frecuencia, titulo,urlImagen,vod_yt,vod_mp4,titulo));

                            }
                            repeticiones_adapter = null;
                            repeticiones_adapter = new programa_repeticiones_adapter ( MasRepeticionesActivity.this,repeticionesLista);

                            recycler_view_REPETICIONES.setAdapter(repeticiones_adapter);
                            repeticiones_adapter.notifyDataSetChanged();
                            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                        }
                        catch (Exception e){
                            //Toast.makeText(getApplicationContext(),"ERROR llamado:" + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(),"No se encuentra contendo, elegí otra fecha.", Toast.LENGTH_LONG).show();
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
            Intent intent = new Intent(MasRepeticionesActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_Combate) {
            Intent intent = new Intent(MasRepeticionesActivity.this, ProgramaDetalleActivity.class);
            intent.putExtra("idprograma",24);
            intent.putExtra("titulo","Combate");
            intent.putExtra("imagen","http://cdn.elnueve.com.ar/files/2017/08/22/170818_Combate_20x150.jpg");
            intent.putExtra("noticias","http://cdn.elnueve.com.ar/movil/noticias/23.js");
            intent.putExtra("clips","http://cdn.elnueve.com.ar/movil/clips/23.js");
            intent.putExtra("repeticiones","http://cdn.elnueve.com.ar/movil/repeticiones/23.js");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_Tl9) {
            Intent intent = new Intent(MasRepeticionesActivity.this, ProgramaDetalleActivity.class);
            intent.putExtra("idprograma",20566);
            intent.putExtra("titulo","TL9");
            intent.putExtra("imagen","http://cdn.elnueve.com.ar/files/2017/05/12/170512_Amanecer_320x150.jpg");
            intent.putExtra("noticias","http://cdn.elnueve.com.ar/movil/noticias/9388.js");
            intent.putExtra("clips","http://cdn.elnueve.com.ar/movil/clips/9388.js");
            intent.putExtra("repeticiones","http://cdn.elnueve.com.ar/movil/repeticiones/9388.js");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_23pm) {
            Intent intent = new Intent(MasRepeticionesActivity.this, ProgramaDetalleActivity.class);
            intent.putExtra("idprograma",46095);
            intent.putExtra("titulo","23 PM");
            intent.putExtra("imagen","http://cdn.elnueve.com.ar/files/2017/03/14/170314_23PM_320x150.jpg");
            intent.putExtra("noticias","http://cdn.elnueve.com.ar/movil/noticias/8202.js");
            intent.putExtra("clips","http://cdn.elnueve.com.ar/movil/clips/8202.js");
            intent.putExtra("repeticiones","http://cdn.elnueve.com.ar/movil/repeticiones/8202.js");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_programas) {
            Intent intent = new Intent(MasRepeticionesActivity.this, ProgramasActivity.class);
            intent.putExtra("from", "inicio");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_noticias) {
            Intent intent = new Intent(MasRepeticionesActivity.this, ResultadoActivity.class);
            intent.putExtra("id", "1");
            intent.putExtra("titulo", "Noticias");
            intent.putExtra("ruta", Constant.MenuTaxonomiasFijas_Noticias);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_espectaculos) {
            Intent intent = new Intent(MasRepeticionesActivity.this, ResultadoActivity.class);
            intent.putExtra("id", "2");
            intent.putExtra("titulo", "Espectáculos");
            intent.putExtra("ruta", Constant.MenuTaxonomiasFijas_Espectaculos);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_deportes) {
            Intent intent = new Intent(MasRepeticionesActivity.this, ResultadoActivity.class);
            intent.putExtra("id", "3");
            intent.putExtra("titulo", "Deportes");
            intent.putExtra("ruta", Constant.MenuTaxonomiasFijas_Deportes);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_comunidad) {
            Intent intent = new Intent(MasRepeticionesActivity.this, ResultadoActivity.class);
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
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}
