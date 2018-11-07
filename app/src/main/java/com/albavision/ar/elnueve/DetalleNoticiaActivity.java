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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.albavision.ar.elnueve.adapters.detalle_noticia_cab_adapter;
import com.albavision.ar.elnueve.adapters.detalle_taxonomia_adapter;
import com.albavision.ar.elnueve.adapters.main_noticia_adapter;
import com.albavision.ar.elnueve.models.detalle_taxonomia_model;
import com.albavision.ar.elnueve.models.main_noticia_model;
import com.albavision.ar.elnueve.utils.AppSingleton;
import com.albavision.ar.elnueve.utils.Constant;
import com.albavision.ar.elnueve.utils.RecyclerItemClickListener;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetalleNoticiaActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView txtTitulo,txtFecha,txtContenido;
    private ImageView imgMedia,icoPlay;
    private JSONArray arrTaxonomias;

    private RecyclerView taxonomiaRecyclerView;
    private RecyclerView.LayoutManager taxonomiaManager;

    private List<detalle_taxonomia_model> taxonomiaLista;
    private detalle_taxonomia_adapter taxonomia_adapter;


    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<main_noticia_model> noticiaLista;
    private detalle_noticia_cab_adapter noticia_adapter;






    private RecyclerView masnoticiasRecyclerView;
    private RecyclerView.LayoutManager masnoticiasLayoutManager;

    private List<main_noticia_model> masnoticiasLista;
    private main_noticia_adapter masnoticias_adapter;

    final String TAG = "XXXXXXXXXXXXXXXXXXXX";
    String  REQUEST_TAG = "com.albavision.ar.elnueve";
    int ts = (int) (new Date().getTime()/1000);

    private FrameLayout flVideo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_noticia);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //***************************
        setSupportActionBar(toolbar); // punto de caida de app, se solucionó : https://stackoverflow.com/questions/26515058/this-activity-already-has-an-action-bar-supplied-by-the-window-decor

        getSupportActionBar().setDisplayShowTitleEnabled(false);//eliminar el título del actionbar

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




       flVideo = (FrameLayout) findViewById(R.id.flVideo);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_NOTICIAS_PRINCIPALES);
        //Use this setting to improve performance if you know that changes in
        //the content do not change the layout size of the RecyclerView
        if (mRecyclerView != null) {
            mRecyclerView.setHasFixedSize(true);
        }
        //mLayoutManager = new LinearLayoutManager(this);
        //mLayoutManager = new GridLayoutManager(this,4);
        mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        //mLayoutManager = new StaggeredGridLayoutManager(4, LinearLayoutManager.VERTICAL);


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

                            Integer Limite = 5;
                            if(ja.length() < Limite)
                                Limite = ja.length();
                            for(int i=0; i< Limite;i++) {
                                JSONObject jsonObjectDato = ja.getJSONObject(i);
                                String nid = jsonObjectDato.getString("nid");
                                if (!jsonObjectDato.getString("nid").equals(getIntent().getExtras().getString("idnoticia"))){
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
                                        video = jsonObjectDato.getString("video_yt");
                                    }else{
                                        video = jsonObjectDato.getString("video_mp4");
                                    }

                                    noticiaLista.add(new main_noticia_model(encabezado, titulo, urlImagen, Integer.parseInt(nid), Integer.parseInt(codcat), fecha, url_imagen3, contenido, taxonomias,video,tipo));
                                }
                            }

                            noticia_adapter = new detalle_noticia_cab_adapter ( DetalleNoticiaActivity.this,noticiaLista);

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











        taxonomiaRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_TAGS);
        //Use this setting to improve performance if you know that changes in
        //the content do not change the layout size of the RecyclerView
        if (taxonomiaRecyclerView != null) {
            taxonomiaRecyclerView.setHasFixedSize(true);
        }
        //taxonomiaManager = new LinearLayoutManager(this);
        //taxonomiaManager = new GridLayoutManager(this,3);
        //taxonomiaManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        taxonomiaManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        //taxonomiaManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.HORIZONTAL);

        taxonomiaRecyclerView.setLayoutManager(taxonomiaManager);

        taxonomiaLista = new ArrayList<>();


        txtTitulo = (TextView) findViewById(R.id.txtTitulo);
        txtTitulo.setText(getIntent().getExtras().getString("titulo"));
        //Toast.makeText(DetalleNoticiaActivity.this, "resumen: " + getIntent().getExtras().getString("titulo"), Toast.LENGTH_SHORT).show();

        txtFecha = (TextView) findViewById(R.id.txtFecha);
        txtFecha.setText(getIntent().getExtras().getString("fecha"));

        imgMedia = (ImageView) findViewById(R.id.imgMedia);
        icoPlay = (ImageView) findViewById(R.id.icoPlay);

        new DownloadImageFromInternet((ImageView) imgMedia)
                .execute(getIntent().getExtras().getString("imagen"));

        if(!getIntent().getExtras().getString("tipo").equals("galeria_fotos")){
            if(!getIntent().getExtras().getString("video").equals("")){
                icoPlay.setVisibility(View.VISIBLE);
            }
        }

        txtContenido = (TextView) findViewById(R.id.txtContenido);
        txtContenido.setText(Html.fromHtml(getIntent().getExtras().getString("contenido")));
        try{
            arrTaxonomias = new JSONArray(getIntent().getExtras().getString("taxonomias"));
            for(int i = 0; i< arrTaxonomias.length();i++)
            {
                JSONObject jsonObjectDato = arrTaxonomias.getJSONObject(i);
                String idTaxonomia =jsonObjectDato.getString("taxonomia_id");
                String nombre = jsonObjectDato.getString("taxonomia_nombre");
                String ruta = jsonObjectDato.getString("taxonomia_url");

                taxonomiaLista.add(new detalle_taxonomia_model(Integer.parseInt(idTaxonomia),nombre,ruta));
            }
            taxonomia_adapter = new detalle_taxonomia_adapter(DetalleNoticiaActivity.this,taxonomiaLista);
            taxonomiaRecyclerView.setAdapter(taxonomia_adapter);
            taxonomia_adapter.notifyDataSetChanged();
        }catch (Exception e){
            Toast.makeText(DetalleNoticiaActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }








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

                            Integer Limite = 6;
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
                                    video = jsonObjectDato.getString("video_yt");
                                }else{
                                    video = jsonObjectDato.getString("video_mp4");
                                }

                                masnoticiasLista.add(new main_noticia_model(encabezado,titulo,urlImagen,Integer.parseInt(nid),Integer.parseInt(codcat),fecha,url_imagen3,contenido,taxonomias,video,tipo));
                                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                            }

                            masnoticias_adapter = new main_noticia_adapter ( DetalleNoticiaActivity.this,masnoticiasLista);

                            masnoticiasRecyclerView.setAdapter(masnoticias_adapter);
                            masnoticias_adapter.notifyDataSetChanged();
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



        flVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!getIntent().getExtras().getString("video").equals("")) {
                    Intent intent = new Intent(DetalleNoticiaActivity.this, VideoFullscreenActivity.class);
                    intent.putExtra("from", "detalle");
                    intent.putExtra("streamEnvivo", getIntent().getExtras().getString("video"));
                    startActivity(intent);
                }
            }
        });




        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Toast.makeText(MainActivity.this, "Card at " + position + " is clicked" , Toast.LENGTH_SHORT).show();
                //Toast.makeText(MainActivity.this, "resumen: " + noticiaLista.get(position).getResumen(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(DetalleNoticiaActivity.this, DetalleNoticiaActivity.class);
                intent.putExtra("idnoticia", noticiaLista.get(position).getCodigo().toString());
                intent.putExtra("titulo", noticiaLista.get(position).getTitulo().toString());
                intent.putExtra("tipo", noticiaLista.get(position).getTipo().toString());
                intent.putExtra("fecha", noticiaLista.get(position).getFecha().toString());
                intent.putExtra("imagen", noticiaLista.get(position).getUrl_imagen3());
                intent.putExtra("contenido", noticiaLista.get(position).getContenido());
                intent.putExtra("taxonomias", noticiaLista.get(position).getTaxonomias().toString());
                intent.putExtra("video", noticiaLista.get(position).getVideo().toString());
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //finish();
            }
        }));



        masnoticiasRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Toast.makeText(MainActivity.this, "Card at " + position + " is clicked" , Toast.LENGTH_SHORT).show();
                //Toast.makeText(MainActivity.this, "resumen: " + noticiaLista.get(position).getResumen(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(DetalleNoticiaActivity.this, DetalleNoticiaActivity.class);
                intent.putExtra("idnoticia", masnoticiasLista.get(position).getCodigo().toString());
                intent.putExtra("titulo", masnoticiasLista.get(position).getTitulo().toString());
                intent.putExtra("tipo", masnoticiasLista.get(position).getTipo().toString());
                intent.putExtra("fecha", masnoticiasLista.get(position).getFecha().toString());
                intent.putExtra("imagen", masnoticiasLista.get(position).getUrl_imagen3());
                intent.putExtra("contenido", masnoticiasLista.get(position).getContenido());
                intent.putExtra("taxonomias", masnoticiasLista.get(position).getTaxonomias().toString());
                intent.putExtra("video", masnoticiasLista.get(position).getVideo().toString());
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //finish();
            }
        }));


        taxonomiaRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Toast.makeText(MainActivity.this, "Card at " + position + " is clicked" , Toast.LENGTH_SHORT).show();
                //Toast.makeText(MainActivity.this, "resumen: " + noticiaLista.get(position).getResumen(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(DetalleNoticiaActivity.this, ResultadoActivity.class);
                intent.putExtra("id", taxonomiaLista.get(position).getIdTaxonomia().toString());
                intent.putExtra("titulo", taxonomiaLista.get(position).getNombre().toString());
                intent.putExtra("ruta", taxonomiaLista.get(position).getRuta().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //finish();
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
            Intent intent = new Intent(DetalleNoticiaActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_Combate) {
            Intent intent = new Intent(DetalleNoticiaActivity.this, ProgramaDetalleActivity.class);
            intent.putExtra("idprograma",24);
            intent.putExtra("titulo","Combate");
            intent.putExtra("imagen","http://cdn.elnueve.com.ar/files/2017/08/22/170818_Combate_20x150.jpg");
            intent.putExtra("noticias","http://cdn.elnueve.com.ar/movil/noticias/23.js");
            intent.putExtra("clips","http://cdn.elnueve.com.ar/movil/clips/23.js");
            intent.putExtra("repeticiones","http://cdn.elnueve.com.ar/movil/repeticiones/23.js");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_Tl9) {
            Intent intent = new Intent(DetalleNoticiaActivity.this, ProgramaDetalleActivity.class);
            intent.putExtra("idprograma",20566);
            intent.putExtra("titulo","TL9");
            intent.putExtra("imagen","http://cdn.elnueve.com.ar/files/2017/05/12/170512_Amanecer_320x150.jpg");
            intent.putExtra("noticias","http://cdn.elnueve.com.ar/movil/noticias/9388.js");
            intent.putExtra("clips","http://cdn.elnueve.com.ar/movil/clips/9388.js");
            intent.putExtra("repeticiones","http://cdn.elnueve.com.ar/movil/repeticiones/9388.js");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_23pm) {
            Intent intent = new Intent(DetalleNoticiaActivity.this, ProgramaDetalleActivity.class);
            intent.putExtra("idprograma",46095);
            intent.putExtra("titulo","23 PM");
            intent.putExtra("imagen","http://cdn.elnueve.com.ar/files/2017/03/14/170314_23PM_320x150.jpg");
            intent.putExtra("noticias","http://cdn.elnueve.com.ar/movil/noticias/8202.js");
            intent.putExtra("clips","http://cdn.elnueve.com.ar/movil/clips/8202.js");
            intent.putExtra("repeticiones","http://cdn.elnueve.com.ar/movil/repeticiones/8202.js");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_programas) {
            Intent intent = new Intent(DetalleNoticiaActivity.this, ProgramasActivity.class);
            intent.putExtra("from", "inicio");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_noticias) {
            Intent intent = new Intent(DetalleNoticiaActivity.this, ResultadoActivity.class);
            intent.putExtra("id", "1");
            intent.putExtra("titulo", "Noticias");
            intent.putExtra("ruta", Constant.MenuTaxonomiasFijas_Noticias);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_espectaculos) {
            Intent intent = new Intent(DetalleNoticiaActivity.this, ResultadoActivity.class);
            intent.putExtra("id", "2");
            intent.putExtra("titulo", "Espectáculos");
            intent.putExtra("ruta", Constant.MenuTaxonomiasFijas_Espectaculos);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_deportes) {
            Intent intent = new Intent(DetalleNoticiaActivity.this, ResultadoActivity.class);
            intent.putExtra("id", "3");
            intent.putExtra("titulo", "Deportes");
            intent.putExtra("ruta", Constant.MenuTaxonomiasFijas_Deportes);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_comunidad) {
            Intent intent = new Intent(DetalleNoticiaActivity.this, ResultadoActivity.class);
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
