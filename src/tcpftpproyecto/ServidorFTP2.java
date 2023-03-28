/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpftpproyecto;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class ServidorFTP2 implements Runnable{
    
   private DataInputStream flujoDeEntrada;
        private DataOutputStream flujoDeSalida;
        private String cadenaRecibe=null;
        private String cadenaEnvia=null;
        private Socket socketCliente;
        EstructuraArchivosIO estructuraArchivos;
        private Thread sc = new Thread(this);
        private String rutaInicial;
        private KeyPair kp;
        private ArrayList<DataOutputStream> ArrayS;
        
        ServerSocket socketServidor;
    
    public ServidorFTP2 (int Puerto) throws NoSuchAlgorithmException{
            
         try {        
             
             KeyPairGenerator kpg=KeyPairGenerator.getInstance("RSA");
             kpg.initialize(1024);
             kp=kpg.generateKeyPair();

             
             socketServidor = new ServerSocket (Puerto);
            
            System.out.println("Servidor conectado esperando clientes");
         
            JFileChooser jfc = new JFileChooser();
            jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            jfc.setCurrentDirectory(new File("c:\\"));
            jfc.setDialogTitle("Selecciona el directorio que vas a compartir");

            if ((jfc.showDialog(jfc, "Seleccionar") == JFileChooser.CANCEL_OPTION)) {
                       JOptionPane.showMessageDialog(null, "El servidor de archivos no ha arrancado");
            } else {
                       File archivo = jfc.getSelectedFile();
                       rutaInicial = archivo.getAbsolutePath();
                       estructuraArchivos = new EstructuraArchivosIO(rutaInicial);
           }

            
            sc.start();
            
            
        } catch (    IOException  ex) {
            System.out.println("Error de conexión");
        }
    }
    
     public static void main(String[] args) throws NoSuchAlgorithmException
     {
                new ServidorFTP2(5000);
                

                
                
                
        }

    @Override
    public void run() 
    {
        
        ArrayS = new ArrayList<DataOutputStream>();
     
        while (true) 
        {            
            
       
               
            try {
                socketCliente = socketServidor.accept();
                
                System.out.println("Comunicación establecida");
                
                ObjectOutputStream ou  =  new ObjectOutputStream(socketCliente.getOutputStream());
                
                ou.writeUnshared(estructuraArchivos.getModel());
                
                ou.writeUnshared(kp.getPublic());
                
                ClienteConexion2 CC = new ClienteConexion2(socketCliente.getOutputStream(), socketCliente.getInputStream(), estructuraArchivos,rutaInicial,kp.getPrivate());
                
                  CC.start();
                
                
                

              
                
                
                
            } catch (IOException ex) {
                Logger.getLogger(ServidorFTP2.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(ServidorFTP2.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
         }
        
        
    }
    
    
      

}