/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpftpproyecto;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 *
 * @author Usuario
 */
public class ClienteConexion2 extends Thread
{
    
    private OutputStream DOA;
    
    private InputStream DIA;
    
    private EstructuraArchivosIO EAIO;
    
    private String raiz;
    
    private PublicKey pbC;
    
    private PrivateKey PK;
    
    private SecretKey secretam;
    
    private Key secretae;

    
    public ClienteConexion2(OutputStream DOA,InputStream DIA,EstructuraArchivosIO EAIO,String raiz,PrivateKey pk) throws NoSuchAlgorithmException
    {
        
        this.DOA = DOA;
                
        this.DIA = DIA;
        
        this.EAIO = EAIO;
        
        this.raiz = raiz;
        
        this.PK = pk;
        
        KeyGenerator kg=KeyGenerator.getInstance("DES");
        
        secretam=kg.generateKey();

        
    }

    @Override
    public void run() 
    {
        String mensaje = "";
        DataInputStream dial = new DataInputStream(DIA);
       
        while (true) 
        {
            
            try {
                 mensaje = dial.readUTF();
                
                System.out.println(mensaje);
                
                 if(mensaje.equals("BAJAR"))
                 {
                     
                     String ruta = dial.readUTF();

                     System.out.println(ruta);
                     
                     byte []bufferArchivo = Files.readAllBytes( Paths.get(ruta));
                     
                      System.out.println(bufferArchivo.length);
                      
                     DataOutputStream DOAL = new DataOutputStream(DOA);
                     
                     DOAL.writeUTF("BAJAR");
                     
                     ObjectOutputStream oos = new ObjectOutputStream(DOA);
                     
                     Cipher c=Cipher.getInstance("DES");
                     c.init(Cipher.ENCRYPT_MODE,secretam );
                     byte[] mensajeCifrado= c.doFinal(bufferArchivo);

                     
                     oos.writeUnshared(mensajeCifrado);

                     
                 }else if(mensaje.equals("CLAVE"))
                 {
                     
                     ObjectInputStream ois = new ObjectInputStream(DIA);
                     
                     pbC = (PublicKey) ois.readObject();
         
                     
                     Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                     c.init(Cipher.WRAP_MODE, pbC);
                     
                     byte [] claveProtegida=c.wrap(secretam); 
                     
               
                     
                     DataOutputStream doas = new DataOutputStream(DOA);
                     
                 
                     
                     doas.writeUTF("CLAVESIMETRICA");
                     
                     
                                 
                     ObjectOutputStream oos = new ObjectOutputStream(DOA);

                     
                     oos.writeUnshared(claveProtegida);
                    
                             
                     
                     
                     
                 }
                 else if(mensaje.equals("SUBIR"))
                 {
                     
                     String ruta = dial.readUTF();
                     
                     ObjectInputStream ois = new ObjectInputStream(DIA);
                     
                    
                     
                     Cipher c=Cipher.getInstance("DES");
                     c.init(Cipher.DECRYPT_MODE, secretae);
                     byte[]desencriptado = c.doFinal((byte[]) ois.readObject());
             
                     
               
                   
          
                     Files.write(Paths.get(ruta,dial.readUTF()), desencriptado);
                     
                      DataOutputStream doas = new DataOutputStream(DOA);
                     
                 
                     
                      doas.writeUTF("TREE");       
                     
                     ObjectOutputStream oos = new ObjectOutputStream(DOA);
                     EAIO = new EstructuraArchivosIO(raiz);     
                     oos.writeUnshared(EAIO.getModel());
          
                 }else if(mensaje.equals("CLAVEP"))
                 {
                     
                    ObjectInputStream as = new ObjectInputStream(DIA);
                   
                    Cipher c;
                    c = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                    c.init(Cipher.UNWRAP_MODE, PK);
                    byte[] ab = (byte[]) as.readObject();
                    System.out.println(ab);
                    
                    secretae= (SecretKey) c.unwrap(ab, "DES", Cipher.SECRET_KEY);
                    
                    
                     
                     
                 }
        }   catch (IOException ex) {
                Logger.getLogger(ClienteConexion2.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClienteConexion2.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(ClienteConexion2.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchPaddingException ex) {
                Logger.getLogger(ClienteConexion2.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidKeyException ex) {
                Logger.getLogger(ClienteConexion2.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalBlockSizeException ex) {
                Logger.getLogger(ClienteConexion2.class.getName()).log(Level.SEVERE, null, ex);
            } catch (BadPaddingException ex) {
                Logger.getLogger(ClienteConexion2.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        }
       
        
        
        
    }
}
    
    
