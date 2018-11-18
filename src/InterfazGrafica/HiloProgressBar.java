/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfazGrafica;

import static InterfazGrafica.CargaDatos.jTextField1;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import practica1redes2.Cliente;

/**
 *
 * @author HP
 */
public class HiloProgressBar extends SwingWorker{
    private Cliente c;
    
    public HiloProgressBar(Cliente c)
    {        
        this.c=c;
    }
    
    @Override
    protected Object doInBackground() throws Exception {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {        
            System.out.println(c.getPorcentaje());
           jTextField1.setText(c.getPorcentaje()+"%");
        }});
        return null;  
    }
}