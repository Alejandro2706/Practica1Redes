/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfazGrafica;

import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import practica1redes2.Cliente;

/**
 *
 * @author HP
 */
public class HiloProgressBar extends SwingWorker{
    private Cliente c;
    private JProgressBar barra;
    public HiloProgressBar(Cliente c, JProgressBar barra)
    {        
        this.c=c;
        this.barra=barra;
    }
    
    @Override
    protected Object doInBackground() throws Exception {
        int porcentaje=0;
        do{
           porcentaje=(int) c.getPorcentaje();
           barra.setValue((int)c.getPorcentaje());
        }while(porcentaje<100);
        return null;  
    }
}