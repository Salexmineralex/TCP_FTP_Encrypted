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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author Usuario
 */
public class ClienteFTP2 extends javax.swing.JFrame implements Runnable{

     private DataOutputStream flujoDeSalida;
     private DataInputStream flujoDeEntrada;
     private Socket socketCliente;
     
     String Host = "localhost";
     
     int Puerto = 5000;
     
     Thread ec = new Thread(this);
     
     private PublicKey pbC;

     private PrivateKey PK;

     
     private SecretKey secretam;
    
     private Key secretae;
     
     ObjectInputStream is;

     
    public ClienteFTP2() throws IOException {
         try {
             initComponents();
             
             
             socketCliente = new Socket (Host, Puerto);
             //Con el método writeBytes estamos escribiendo un texto en un objeto del tipo
             //DataOutputStream, que a su vez se sustenta en un objeto del tipo OutputStream, 
             //que se encuentra asociado a un SOCKET
             flujoDeSalida= new DataOutputStream(socketCliente.getOutputStream());
             
             KeyGenerator kg=KeyGenerator.getInstance("DES");
        
             secretam=kg.generateKey();

             
             
             ec.start();
             
             flujoDeSalida.writeUTF("CLAVE");
             
             ObjectOutputStream as = new ObjectOutputStream(socketCliente.getOutputStream());
             
             KeyPairGenerator kpg=KeyPairGenerator.getInstance("RSA");
             kpg.initialize(1024);
             KeyPair par=kpg.generateKeyPair();
             
             PK = par.getPrivate();
             
             as.writeUnshared(par.getPublic());
             
             
             jTree1.removeAll();
             
             
         } catch (NoSuchAlgorithmException ex) {
             Logger.getLogger(ClienteFTP2.class.getName()).log(Level.SEVERE, null, ex);
         }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setViewportView(jTree1);

        jButton1.setText("Download");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        jButton2.setText("Upload");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addContainerGap(180, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
         try {
             // TODO add your handling code here:

             flujoDeSalida.writeUTF("BAJAR");
             
             
             flujoDeSalida.writeUTF(getSelectedPath());
         } catch (IOException ex) {
             Logger.getLogger(ClienteFTP2.class.getName()).log(Level.SEVERE, null, ex);
         }
        
    }//GEN-LAST:event_jButton1MouseClicked

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
         try {
             // TODO add your handling code here:
             
             flujoDeSalida.writeUTF("SUBIR");
             
             flujoDeSalida.writeUTF(getSelectedPath());
             
              JFileChooser jfc = new JFileChooser();
                        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                        jfc.setCurrentDirectory(new File("c:\\"));
                        jfc.setDialogTitle("Selecciona el fichero que vas a subir");

                        if ((jfc.showDialog(jfc, "Seleccionar") == JFileChooser.CANCEL_OPTION))
                        {
                                   JOptionPane.showMessageDialog(null, "El servidor de archivos no ha arrancado");
                        } else 
                        {
                            
                                   File archivo = jfc.getSelectedFile();
                                   String rutaInicial = archivo.getAbsolutePath();
                                   
                                   ObjectOutputStream as = new ObjectOutputStream(socketCliente.getOutputStream());
                                   
                                   byte []bufferArchivo = Files.readAllBytes( Paths.get(rutaInicial));
                                   
                                   Cipher c=Cipher.getInstance("DES");
                                    c.init(Cipher.ENCRYPT_MODE,secretam );
                                    byte[] mensajeCifrado= c.doFinal(bufferArchivo);
                                   
                                   as.writeUnshared(mensajeCifrado);
                                   
                                   flujoDeSalida.writeUTF(archivo.getName());
                       }
                        
           
             
             
             
         } catch (IOException ex) {
             Logger.getLogger(ClienteFTP2.class.getName()).log(Level.SEVERE, null, ex);
         } catch (NoSuchAlgorithmException ex) {
             Logger.getLogger(ClienteFTP2.class.getName()).log(Level.SEVERE, null, ex);
         } catch (NoSuchPaddingException ex) {
             Logger.getLogger(ClienteFTP2.class.getName()).log(Level.SEVERE, null, ex);
         } catch (InvalidKeyException ex) {
             Logger.getLogger(ClienteFTP2.class.getName()).log(Level.SEVERE, null, ex);
         } catch (IllegalBlockSizeException ex) {
             Logger.getLogger(ClienteFTP2.class.getName()).log(Level.SEVERE, null, ex);
         } catch (BadPaddingException ex) {
             Logger.getLogger(ClienteFTP2.class.getName()).log(Level.SEVERE, null, ex);
         }
             
         
        
    }//GEN-LAST:event_jButton2MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ClienteFTP2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClienteFTP2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClienteFTP2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClienteFTP2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new ClienteFTP2().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(ClienteFTP2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    
    @Override
    public void run() 
    {
        
          String mensaje = "";
          
            try {
             flujoDeEntrada= new DataInputStream(socketCliente.getInputStream());
         } catch (IOException ex) {
             Logger.getLogger(ClienteFTP2.class.getName()).log(Level.SEVERE, null, ex);
         }
        
         try {
            
             
             is = new ObjectInputStream(socketCliente.getInputStream());
             
             DefaultTreeModel model = (DefaultTreeModel) is.readObject();
             
             jTree1.setModel(model);

             pbC = (PublicKey) is.readObject();
             
         } catch (IOException ex) {
             Logger.getLogger(ClienteFTP2.class.getName()).log(Level.SEVERE, null, ex);
         } catch (ClassNotFoundException ex) {
             Logger.getLogger(ClienteFTP2.class.getName()).log(Level.SEVERE, null, ex);
         }
         
        
        
        while(true) 
        {            
            
            
            
         try {
           
             
             
              mensaje = (String) flujoDeEntrada.readUTF();
           
             if(mensaje.equals("BAJAR"))
             {
                 
                    ObjectInputStream as = new ObjectInputStream(socketCliente.getInputStream());
                 
                    Cipher c=Cipher.getInstance("DES");
                    c.init(Cipher.DECRYPT_MODE, secretae);
                    byte[]desencriptado = c.doFinal((byte[]) as.readObject());
                    
                 
                 
                    byte[] AB = desencriptado;
                    
                    System.out.println(AB.length);        
                 
                     JFileChooser jfc = new JFileChooser();
                        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                        jfc.setCurrentDirectory(new File("c:\\"));
                        jfc.setDialogTitle("Selecciona como y donde lo quieres guardar");

                        if ((jfc.showDialog(jfc, "Seleccionar") == JFileChooser.CANCEL_OPTION))
                        {
                                   JOptionPane.showMessageDialog(null, "El servidor de archivos no ha arrancado");
                        } else 
                        {
                                   File archivo = jfc.getSelectedFile();
                                   String rutaInicial = archivo.getAbsolutePath();

                                   Files.write(Paths.get(rutaInicial), AB);
                       }
                    
                   
               
                 
                 
             }else if(mensaje.equals("CLAVESIMETRICA"))
             {
                 
                    ObjectInputStream as = new ObjectInputStream(socketCliente.getInputStream());
                   
                    Cipher c;
                    c = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                    c.init(Cipher.UNWRAP_MODE, PK);
                    byte[] ab = (byte[]) as.readObject();
                    System.out.println(ab);
                    secretae= (SecretKey) c.unwrap(ab, "DES", Cipher.SECRET_KEY);
                    
                    flujoDeSalida.writeUTF("CLAVEP");
                    
                    
                  
                    ObjectOutputStream oos = new ObjectOutputStream(socketCliente.getOutputStream());
                                  
                 
                     c = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                     c.init(Cipher.WRAP_MODE, pbC);
                     
                     byte [] claveProtegida=c.wrap(secretam); 
                     
                  
  
                                 
                     oos.writeObject(claveProtegida);


                 
             }else if(mensaje.equals("TREE"))
             {
                   ObjectInputStream es = new ObjectInputStream(socketCliente.getInputStream());
             
                   DefaultTreeModel model = (DefaultTreeModel) es.readUnshared();
             
                   jTree1.setModel(model);
             }
             
             
            
             
         } catch (Exception ex) {
             ex.printStackTrace();
             System.out.println("HOLA");
         }
         
         
         }
        
        
    }
        
    
    private String getSelectedPath() 
    {
        String jTreeVarSelectedPath = "";
        Object[] paths = jTree1.getSelectionPath().getPath();
        for (int i = 0; i < paths.length; i++) {
            jTreeVarSelectedPath += paths[i];
            jTreeVarSelectedPath += "\\";
        }
        return jTreeVarSelectedPath;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTree jTree1;
    // End of variables declaration//GEN-END:variables
}
