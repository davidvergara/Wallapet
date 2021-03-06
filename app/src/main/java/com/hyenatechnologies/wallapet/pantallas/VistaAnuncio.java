package com.hyenatechnologies.wallapet.pantallas;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hyenatechnologies.wallapet.Anuncio;
import com.hyenatechnologies.wallapet.R;
import com.hyenatechnologies.wallapet.conexiones.Conexiones;
import com.hyenatechnologies.wallapet.conexiones.ServerException;

/**
 * Muestra un anuncio. Recibe por "intent" el id del anuncio a mostrar.
 * Si no recibe nada, muestra el 1.
 */
public class VistaAnuncio extends Fragment {

    //Variables
    TextView anuncioId;
    TextView anuncioEmail;
    TextView anuncioEstado;
    TextView anuncioDescripcion;
    TextView anuncioTipo;
    TextView anuncioPrecio;
    TextView anuncioTitulo;
    TextView anuncioEspecie;
    Button botonBorrar;
    Button botonActualizar;
    Anuncio actual;
    EditText texto;
    Button botonVer;
    @Override


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_vista_anuncio, container, false);

        //Estas dos lineas siguientes son para permitir el uso de la red
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);



        //Cargamos cuadro de texto de id de anuncio
        texto = (EditText) rootView.findViewById(R.id.verAnuncio);
        //Cargamos botones
        botonVer = (Button) rootView.findViewById(R.id.verAnuncioBoton);
        //Declaramos cuadros de texto
        anuncioId = (TextView) rootView.findViewById(R.id.anuncioId);
        anuncioEmail = (TextView) rootView.findViewById(R.id.anuncioEmail);
        anuncioEstado = (TextView) rootView.findViewById(R.id.anuncioEstado);
        anuncioDescripcion = (TextView) rootView.findViewById(R.id.anuncioDescripcion);
        anuncioTipo = (TextView) rootView.findViewById(R.id.anuncioTipo);
        anuncioPrecio = (TextView) rootView.findViewById(R.id.anuncioPrecio);
        anuncioTitulo = (TextView) rootView.findViewById(R.id.anuncioTitulo);
        anuncioEspecie = (TextView) rootView.findViewById(R.id.anuncioEspecie);
        botonBorrar = (Button) rootView.findViewById(R.id.botonBorrar);
        botonActualizar = (Button) rootView.findViewById(R.id.botonActualizar);
        //Obtenemos el id de anuncio que nos han pasado desde el intent.
        //Si no nos han pasado nada, ponemos 1.
        Intent mIntent = getActivity().getIntent();
        int idAnuncio = mIntent.getIntExtra("idAnuncio", 26);

        //El id del anuncio es real, obtenemos anuncio del servidor
        Anuncio a;
        try {
            //El anuncio existe
            a = Conexiones.getAnuncioById(idAnuncio);
            actual = a;
            Toast.makeText(getActivity().getApplicationContext(), "Anuncio cargado correctamente",
                    Toast.LENGTH_LONG).show();
            //Mostramos el anuncio
            mostrarAnuncio(a);

        } catch (ServerException ex) {
            //El anuncio no existe
            switch (ex.getCode()) {
                case 404:
                    Toast.makeText(getActivity().getApplicationContext(), "El anuncio indicado no existe",
                            Toast.LENGTH_LONG).show();
                    break;
                case 500:
                    Toast.makeText(getActivity().getApplicationContext(), "Error al contactar con el servidor",
                            Toast.LENGTH_LONG).show();
                    break;
                case 403:
                    Toast.makeText(getActivity().getApplicationContext(), "Error de permisos",
                            Toast.LENGTH_LONG).show();
                    break;

            }
        }

        //Establecemos comportamiento de boton de ver anuncio
        botonVer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Lanzamos la actividad de ver
                Intent i = new Intent(getActivity().getApplicationContext(), VistaAnuncio.class);
                int idAnuncio = 26;
                if (!texto.getText().toString().equals("")) {
                    i.putExtra("idAnuncio", Integer.parseInt(texto.getText().toString()));

                }
            }
        });

        //Accion al pulsar el boton de borrar
        botonBorrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Borramos el anuncio

                try {
                    Conexiones.deleteAnuncio(actual.getIdAnuncio());
                    Toast.makeText(getActivity().getApplicationContext(), "El anuncio se ha borrado con éxito",
                            Toast.LENGTH_LONG).show();
                }
                catch(ServerException ex){
                    switch (ex.getCode()) {
                        case 404:
                            Toast.makeText(getActivity().getApplicationContext(), "El anuncio indicado no existe",
                                    Toast.LENGTH_LONG).show();
                            break;
                        case 500:
                            Toast.makeText(getActivity().getApplicationContext(), "Error al contactar con el servidor",
                                    Toast.LENGTH_LONG).show();
                            break;
                        case 403:
                            Toast.makeText(getActivity().getApplicationContext(), "Error de permisos",
                                    Toast.LENGTH_LONG).show();
                            break;

                    }
                }

            }
        });

        //Accion al pulsar el boton de actualizar
        botonActualizar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Actualizamos el anuncio
                Intent i = new Intent(getActivity().getApplicationContext(), CrearModificarAnuncio.class);

                i.putExtra("anuncio",Anuncio.toJson(actual));
                startActivity(i);

            }
        });
        return rootView;
    }




    /**
     * Muestra el anuncio en pantalla
     */
    public void mostrarAnuncio(Anuncio a) {
        anuncioId.setText("" + a.getIdAnuncio());
        anuncioEmail.setText(a.getEmail());
        anuncioEstado.setText(a.getEstado());
        anuncioDescripcion.setText(a.getDescripcion());
        anuncioTipo.setText(a.getTipoIntercambio());
        anuncioPrecio.setText("" + a.getPrecio() + "€");
        anuncioTitulo.setText(a.getTitulo());
        anuncioEspecie.setText(a.getEspecie());
    }
}