package com.example.nimesukiapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nimesukiapp.R;
import com.example.nimesukiapp.models.entities.AnimeEntity;
import com.example.nimesukiapp.models.vo.Anime;

import java.util.ArrayList;
import java.util.Date;

public class VistaAnimeFavorito extends AppCompatActivity {
    /*ArrayList<Anime> animes = new ArrayList<>();
    private static final int REQUEST_CODE_SELECCIONAR = 1;
    TextView f;
    TextView h;
    AnimeEntity animeEntity;
    String nombre = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_anime);

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);
        nombre = intent.getStringExtra("nombre");
        long fechaMillis = intent.getLongExtra("fecha", -1);
        String sinopsis = intent.getStringExtra("sinopsis");
        String genero = intent.getStringExtra("genero");
        String imagen = intent.getStringExtra("imagen");

        Date fecha = new Date(fechaMillis);

        cargarPeliculas();

        ListView listaActores = findViewById(R.id.listview_actores_pelicula);

        adapter = new ActorAdapter(this.getBaseContext(), actores, this);

        listaActores.setAdapter(adapter);

        listaActores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Actor actorSeleccionado = (Actor) parent.getItemAtPosition(position);
                OnActorSelected(actorSeleccionado);
            }
        });

        TextView nombreTextView = findViewById(R.id.tvNombrePelicula);
        TextView generoTextView = findViewById(R.id.tvGeneroPelicula);
        TextView sinopsisTextView = findViewById(R.id.tvSinopsisPelicula);
        TextView fechaTextView = findViewById(R.id.tvFechaPelicula);
        Button editar = findViewById(R.id.btnEditarComentarios);
        Button indicar = findViewById(R.id.btnIndicarFecha);
        Button guardar = findViewById(R.id.btnGuardarPelicula);
        f = findViewById(R.id.tvFechaEmision);
        h = findViewById(R.id.tvHoraEmision);
        RatingBar estrellas = findViewById(R.id.ratingBar);
        EditText coments = findViewById(R.id.etComentarios);
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());

        coments.setEnabled(false);

        ImageView imagenView = findViewById(R.id.imagen_pelicula);

        imagenView.setImageBitmap(null);
        imagenView.setVisibility(View.INVISIBLE);

        if (imagen != null && !imagen.isEmpty()) {
            new Thread(() -> {
                try {
                    byte[] decodedBytes = Base64.decode(imagen, Base64.DEFAULT);

                    Bitmap bmp = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

                    runOnUiThread(() -> {
                        if (bmp != null) {
                            imagenView.setImageBitmap(bmp);
                            imagenView.setVisibility(View.VISIBLE);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }

        nombreTextView.setText(nombre);
        fechaTextView.setText(dateFormat.format(fecha));
        sinopsisTextView.setText(sinopsis);
        generoTextView.setText(genero);

        AppDatabase db = AppDatabase.getDatabase(getBaseContext());
        peliculaEntity = db.peliculaDao().obtenerPelicula(id);

        String prefs = "PeliculaPrefs" + nombre;
        String fecha_emision = "fecha_emision" + nombre;
        String hora_emision = "hora_emision" + nombre;
        String comentariosPeli = "comentarios" + nombre;
        String ratingPeli = "rating" + nombre;

        if (peliculaEntity != null) {
            String fechaEmision = peliculaEntity.getFechaEmision();
            float rating = 0;
            rating = peliculaEntity.getPuntuacion();

            if (fechaEmision != null) {
                f.setText(fechaEmision);
            }
            if (rating != 0) {
                estrellas.setRating(rating);
            }
        }

        SharedPreferences sharedPreferences = getSharedPreferences(prefs, MODE_PRIVATE);
        //String fechaEmision = sharedPreferences.getString(fecha_emision, "No guardada");
        String horaEmision = sharedPreferences.getString(hora_emision, "");
        String comentarios = sharedPreferences.getString(comentariosPeli, "Sin comentarios");
        //float rating = 0;
        //rating = sharedPreferences.getFloat(ratingPeli, 0.0f);


        if (comentarios != null) {
            coments.setText(comentarios);
        }
        if (horaEmision != null) {
            h.setText(horaEmision);
        }

        indicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VistaPelicula.this, SeleccionFechaActivity.class);
                intent.putExtra("Fecha", f.getText().toString());
                intent.putExtra("Hora", h.getText().toString());
                startActivityForResult(intent, REQUEST_CODE_SELECCIONAR);
            }
        });

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b = new AlertDialog.Builder(VistaPelicula.this);
                View comentarios = inflater.inflate(R.layout.dialog_comentarios, null);
                EditText textSet = comentarios.findViewById(R.id.etComentariosDialog);
                textSet.setText(coments.getText().toString());
                b.setView(comentarios);
                b.setTitle("Comentarios de la película");
                b.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText text = comentarios.findViewById(R.id.etComentariosDialog);
                        coments.setText(text.getText().toString());
                    }
                });
                b.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = b.create();
                dialog.show();
            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b = new AlertDialog.Builder(VistaPelicula.this);
                View comentarios = inflater.inflate(R.layout.dialog_guardado, null);
                TextView textSet = comentarios.findViewById(R.id.tvGuardado);
                textSet.setText("Fecha de emision: " + f.getText().toString() + " " + h.getText().toString() + "\nComentarios: " + coments.getText().toString() + "\nRating: " + estrellas.getRating());
                b.setView(comentarios);
                b.setTitle("Datos a Guardar");
                b.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedPreferences = getSharedPreferences(prefs, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        //editor.putString(fecha_emision, f.getText().toString());
                        editor.putString(hora_emision, h.getText().toString());
                        editor.putString(comentariosPeli, coments.getText().toString());
                        //editor.putFloat(ratingPeli, estrellas.getRating());
                        editor.apply();
                        if (peliculaEntity == null) {
                            peliculaEntity=new PeliculaEntity(estrellas.getRating(), f.getText().toString(), id);
                            int idInsr=(int) db.peliculaDao().insertarPelicula(peliculaEntity);
                            if(idInsr!=-1) {
                                peliculaEntity.setId(idInsr);
                            }
                        } else {
                            peliculaEntity.setFechaEmision(f.getText().toString());
                            peliculaEntity.setPuntuacion(estrellas.getRating());
                            db.peliculaDao().actualizarPelicula(peliculaEntity);
                        }

                        Toast.makeText(VistaPelicula.this, "Datos guardados correctamente", Toast.LENGTH_SHORT).show();

                    }
                });
                b.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = b.create();
                dialog.show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SELECCIONAR && resultCode == RESULT_OK) {
            if (data != null) {
                String fechaSeleccionada = data.getStringExtra("FECHA_SELECCIONADA");
                String horaSeleccionada = data.getStringExtra("HORA_SELECCIONADA");

                f.setText(fechaSeleccionada);
                h.setText(horaSeleccionada);
            }
        }
    }

    @Override
    public void OnActorSelected(Actor actor) {
        Intent intent = new Intent(this, VistaActor.class);
        intent.putExtra("id", actor.getId());
        intent.putExtra("nombre", actor.getNombre());
        intent.putExtra("fechaNacimiento", actor.getFechaNacimiento().getTime());
        intent.putExtra("foto", actor.getFoto());
        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_acciones, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.visualizar) {
            return true;
        } else if (item.getItemId() == R.id.pelis) {
            Intent intent = new Intent(VistaPelicula.this, MainActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.actors) {
            Intent intent = new Intent(VistaPelicula.this, ListadoActoresActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.sincronizar) {
            Toast.makeText(getBaseContext(), "Sincronizar", Toast.LENGTH_LONG).show();
            return true;
        } else if (item.getItemId() == R.id.salir) {
            finishAffinity();
            System.exit(0);
            return true;
        }
        return false;
    }

    private void cargarPeliculas() {
        ServicioREST servicioREST = new ServicioREST();
        servicioREST.obtenerPeliculas(new ServicioREST.OnPeliculasObtenidasListener() {
            @Override
            public void onSuccess(ArrayList<Pelicula> listaPeliculas) {
                runOnUiThread(() -> {
                    peliculas.clear();
                    peliculas.addAll(listaPeliculas);

                    for (Pelicula peli : peliculas) {
                        if (peli.getNombre().equals(nombre)) {
                            actores.addAll(peli.getActores());
                            break;
                        }
                    }

                    adapter.notifyDataSetChanged();
                });
            }

            @Override
            public void onError(Exception e) {
                if (this != null) {
                    VistaPelicula.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(), "Error al cargar las películas", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }*/
}