/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica1redes2;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HP
 */
public class Servidor extends Thread{
    private ServerSocket servidor=null;
    private final int PUERTO=3000;
    private BufferedReader inbound=null;
    private OutputStream outbound=null;
    private Socket cliente=null;
    private int tamanoArch;
    public float porcentaje;
    private int TAMBUFFER=100;
    public Servidor(){
    }
    @Override
    public void run(){
        try {
            escucha();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void escucha() throws ClassNotFoundException
    {
        BufferedOutputStream bos=null;
        ObjectInputStream entradaDatos=null;
        ObjectOutputStream salidaDatos=null;
        byte[] dato=null;
        String nombreFile;
        boolean bandera=true;
        //Se inicia un servidor
        try {this.servidor= new ServerSocket(PUERTO);} catch (IOException ex) {System.out.println("no se pudo crear servidor");}
        while(true)
        {
            try
            {
                int contadorDato=0;
                bandera=true;
                System.out.println("esperando una conexion");
                /*se mantiene a la espera de una conexión, cuando haya una conexion 
                el servidor la aceptará y empezará el desmadre*/
                this.cliente=servidor.accept();
                System.out.println("cliente acptado");
                //se obtiene el flujo de datos que llega al servidor
                entradaDatos= new ObjectInputStream(cliente.getInputStream());
                salidaDatos=new ObjectOutputStream(cliente.getOutputStream());
                //Se cumple mientras haya datos que leer
                while(bandera)
                {
                    //leemmos lo que envia el cliente
                    Object entrada=entradaDatos.readObject();
                    //Si es una cadena, se trata del nombre del archivo y del tamaño del archivo
                    if(entrada instanceof String)
                    {
                        nombreFile=(String)entrada;
                        String[] partes=nombreFile.split("`");
                        //Se inicia un array del mismo tamaño del archivo para guardar los bytes en la carga
                        this.tamanoArch=Integer.parseInt(partes[1]);
                        dato=new byte[Integer.parseInt(partes[1])];
                        //respuesta al cliente
                        salidaDatos.writeObject("lo recibí\n");
                        //aca se crea el archivo, ya se crea con extension, falta escribirle datos
                        bos=new BufferedOutputStream(new FileOutputStream(partes[0]));
                    }
                    //Si entra aca, significa que es un byte, procedemos a guardarlo en dato
                    else{
                        //System.out.println(new String((byte[])entrada));
                        byte[] bEntrada=(byte[])entrada;
                        for(int i=0; i<bEntrada.length; i++)
                        {
                            dato[contadorDato]=bEntrada[i];
                            contadorDato++;
                        }
                        porcentaje=((contadorDato+1)*100)/(this.tamanoArch);
                        salidaDatos.writeObject("lo recibí\n");
                        System.out.println("porcentaje: " + porcentaje + "%");
                    }
                }
            //Entra a esta excepcion cuando el cliente acabe su conexion, aca se escribe en el fichero    
            }catch(IOException ex){
                try
                {
                    //escribimos el arreglo de bytes en el fichero que se creo anteriormente
                    bos.write(dato);
                    System.out.println("conexión con cliente terminada "+ex);
                    //Se cierran flujos
                    if(entradaDatos!=null)
                        entradaDatos.close();
                    if(salidaDatos!=null)
                        salidaDatos.close();
                    if(bos!=null)
                        bos.close();
                    //Se abre a la verga el cliente para que entre otro
                    cliente.close();
                }catch(IOException ex1){
                    System.out.println("error al cerrar los flujos");
                }
            }
        } 
    } 
    public void ImprimirFichero(byte[] arrayByte){
        int cont=0;
        for(int i=0;i<arrayByte.length;i++)
        {
            cont++;
            if(cont==10)
            {
                System.out.print(arrayByte[i]+" ");
                System.out.println("");
                cont=0;
            }
            else
            {
                System.out.print(arrayByte[i]+" ");
            }
        }
    }
    public static void main(String args[]) throws ClassNotFoundException
    {
        Servidor serv=new Servidor();
        serv.escucha();
    }
}
