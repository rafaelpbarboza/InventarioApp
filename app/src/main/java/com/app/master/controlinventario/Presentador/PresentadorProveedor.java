package com.app.master.controlinventario.Presentador;

import android.content.Context;
import android.widget.Toast;

import com.app.master.controlinventario.Modelo.Proveedor;
import com.app.master.controlinventario.Modelo.ResFull.Endpoint;
import com.app.master.controlinventario.Modelo.ResFull.RespuestaEndpointIngresarProveedor;
import com.app.master.controlinventario.Modelo.ResFull.RespuestaProveedores;
import com.app.master.controlinventario.Modelo.ResFull.RestApiAdapter;
import com.app.master.controlinventario.Vista.Fragment.iFragmentFormularioProveedores;
import com.app.master.controlinventario.Vista.Proveedores;
import com.app.master.controlinventario.Vista.iProveedores;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rafael p on 24/6/2017.
 */

public class PresentadorProveedor implements iPresentadorProveedor {

    private iProveedores proveedores;
    private iFragmentFormularioProveedores iFragmentFormularioProveedores;
    private Context context;
    private String codigoEstatusProvvedor;
    private ArrayList<Proveedor>Proveedores;

    public PresentadorProveedor(Context context,iProveedores iproveedores){
        this.context=context;
        this.proveedores=iproveedores;
    }
    public PresentadorProveedor(Context context,iFragmentFormularioProveedores iFragmentFormularioProveedores){
        this.context=context;
        this.iFragmentFormularioProveedores=iFragmentFormularioProveedores;
    }


    @Override
    public void IngresarProveedor(String nit,String nombre,String direccion,String telefono) {
        RestApiAdapter conexion=new RestApiAdapter();
        Gson gson=conexion.constriurGsonDeserializadorProveedores();
        Endpoint endpoint=conexion.establecerConexionIngresarProveedor(gson);
        Call<RespuestaEndpointIngresarProveedor>respuesta =endpoint.CrearProveedores(nit,nombre,direccion,telefono);

        respuesta.enqueue(new Callback<RespuestaEndpointIngresarProveedor>() {
            @Override
            public void onResponse(Call<RespuestaEndpointIngresarProveedor> call, Response<RespuestaEndpointIngresarProveedor> response) {
                RespuestaEndpointIngresarProveedor respuestaProveedor=response.body();
                codigoEstatusProvvedor=respuestaProveedor.getCodigoEstatus();
                Toast.makeText(context,codigoEstatusProvvedor.toString(), Toast.LENGTH_SHORT).show();
                estadoDeingresoProveedor();
            }

            @Override
            public void onFailure(Call<RespuestaEndpointIngresarProveedor> call, Throwable t) {
                Toast.makeText(context, "Error en el servido, intente mas tarde", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void estadoDeingresoProveedor() {
        if(codigoEstatusProvvedor.equals("0")){
            Toast.makeText(context, "Proveedor agregado correctamente", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Error al agregar, intente mas tarde", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void listarProveedor() {
        RestApiAdapter conexion=new RestApiAdapter();
        Gson gson=conexion.construirGsonDeserializadorListaProveedor();
        Endpoint endpoint= conexion.establecerConexionIngresarProveedor(gson);
        Call<RespuestaProveedores> respuesta=endpoint.obtenerListaprovedores();
        respuesta.enqueue(new Callback<RespuestaProveedores>() {
            @Override
            public void onResponse(Call<RespuestaProveedores> call, Response<RespuestaProveedores> response) {
               RespuestaProveedores respuestaProveedores=response.body();
                Proveedores=respuestaProveedores.getProveedores();

            }

            @Override
            public void onFailure(Call<RespuestaProveedores> call, Throwable t) {
                Toast.makeText(context, "Error al conectarse al servidor, intente mas tarde", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
