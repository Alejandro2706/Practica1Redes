/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica1redes2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;

/**
 *
 * @author HP
 */
public class Cliente{
    private final String DIRECCION="localhost";
    private final int PUERTO_DESTINO=3000;
    private Socket cliente=null;
    public final int PARTEMENSAJE=10;
    private Archivos archivo=null; 
    public float porcentaje=0;
    private int TAMBUFFER=100;
    public Cliente() {
    }
    public int EnviarDatos(Archivos dato) throws InterruptedException{
        ObjectOutputStream enviarDatos=null;
        ObjectInputStream recibirDatos=null;
        try {
            /*Se establece un canal de comunicacion al servidor en la ip direccion y puerto 3000
            el puerto debe ser el mismo que en el servidor*/
            cliente=new Socket(DIRECCION,PUERTO_DESTINO);
            System.out.println("cliente "+cliente);
            /*Obtener el canal de entrada y salida del socket
            input es para la respuesta del servidor y output para mandarle datos*/
            enviarDatos=new ObjectOutputStream(cliente.getOutputStream());
            recibirDatos=new ObjectInputStream(cliente.getInputStream());
            //Se le manda el nombre del archivo junto con el tamaño del mismo
            enviarDatos.writeObject(dato.getArchivo().getName()+"`"+dato.getArrayByte().length);
            //respuesta del servidor
            String informacion=(String)recibirDatos.readObject();
            System.out.println(informacion);
            int maximo=(dato.getArrayByte().length)/this.TAMBUFFER;
            int residuo=(dato.getArrayByte().length)%this.TAMBUFFER;
            int contBuffer=0;
            for(int i=0;i<maximo;i++)
            {
                byte[] datoE=new byte[this.TAMBUFFER];
                //Tomamos un bit del array de bytes del archivo y se lo enviamos al servidor
                for(int j=i*this.TAMBUFFER;j<(i*this.TAMBUFFER)+this.TAMBUFFER;j++){
                    datoE[contBuffer]=dato.getArrayByte()[j];
                    contBuffer++;
                }
                contBuffer=0;
                //System.out.println(i+" "+new String(datoE));
                enviarDatos.writeObject(datoE);
                enviarDatos.flush();
                informacion=(String)recibirDatos.readObject();
                //Calculamos el porcentaje de envio
                this.porcentaje=((i+1)*100)/(maximo);
                //this.bar.setValue((int)this.porcentaje);
               
                System.out.println("porcentaje: " + porcentaje + "%");
                //System.out.println(datoE+" porcentaje: " + this.porcentaje + "%");
            }
            if(residuo!=0){
                int contRes=0;
                byte[] datoRes= new byte[residuo];
                for(int j=maximo*this.TAMBUFFER;j<dato.getArrayByte().length;j++)
                {
                    datoRes[contRes]=dato.getArrayByte()[j];
                    contRes++;
                }
                this.porcentaje=((maximo+1)*100)/(maximo);
                enviarDatos.writeObject(datoRes);
                informacion=(String)recibirDatos.readObject();
            }
        } catch (IOException ex) {
            //Si ocurre algun error
            return 0; 
        } catch (ClassNotFoundException ex) {
            //error por un mal casteo de clases 
            return 0;
        }finally{
            try {
                /*Se cierran los flujos cuando se termina la conexion*/
                if(recibirDatos!=null)
                    recibirDatos.close();
                if(enviarDatos!=null)
                    enviarDatos.close();
                cliente.close();
            } catch (IOException ex) {
                //errores al cerrar los flujos
                return 0;
            }
        }
        return 1;
    }
    
    public float getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(float porcentaje) {
        this.porcentaje = porcentaje;
    }
    /*public static void main(String args[]) throws InterruptedException{
        Cliente cl=new Cliente();
        String ruta="C:\\Users\\HP\\Documents\\sujeto1estres.txt";
        Archivos arch=new Archivos(ruta);
        cl.EnviarDatos(arch);
    }*/
}
