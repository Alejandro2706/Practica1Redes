/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica1redes2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HP
 */
public class Archivos {
    private String RUTA;
    private byte[] arrayByte;
    private File archivo;
    public final int LONGITUD; 

    public Archivos(String ruta) {
        this.RUTA=ruta;
        this.archivo = new File(RUTA);
        this.LONGITUD=(int)this.archivo.length();
        arrayByte=new byte[LONGITUD];
        //aca obtenemos el flujo de bytes del archivo que queremos mandar y se guarda en aarraybyte
        this.CargarDatos();
    }
    private void CargarDatos(){
        try {
            FileInputStream entradaFichero=new FileInputStream(RUTA);
            entradaFichero.read(arrayByte);
        } catch (FileNotFoundException ex) {
            System.out.println("archivo no encontrado: "+ex);
        } catch (IOException ex) {
            System.out.println("excepcion ex: "+ex);
        }
    }
    public void ImprimirFichero(){
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
    public String getRUTA() {
        return RUTA;
    }
    public void setRUTA(String RUTA) {
        this.RUTA = RUTA;
    }
    public byte[] getArrayByte() {
        return arrayByte;
    }
    public void setArrayByte(byte[] arrayByte) {
        this.arrayByte = arrayByte;
    }
    public File getArchivo() {
        return archivo;
    }
    public void setArchivo(File archivo) {
        this.archivo = archivo;
    }
    
}
