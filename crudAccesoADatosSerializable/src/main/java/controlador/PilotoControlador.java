/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.Piloto;
import vista.Vista;

/**
 *
 * @author adria
 */
public class PilotoControlador implements ActionListener{
    Vista vistaPrincipal;
    public static DefaultTableModel modeloPiloto = new DefaultTableModel();
    public static ArrayList<Piloto> listaPilotos = new ArrayList<>();
   
    private AvionControlador avionController;
    
    boolean insertar=false;
    boolean modificar=false;
    boolean eliminar=false;
    boolean pkRepetida = false;
    boolean noEliminar = false;
    
    public PilotoControlador(Vista v){
        this.vistaPrincipal=v;
        
        this.vistaPrincipal.jButtonInsertarPiloto.addActionListener(this);
        this.vistaPrincipal.jButtonAceptarPiloto.addActionListener(this);
        this.vistaPrincipal.jButtonModificarPiloto.addActionListener(this);
        this.vistaPrincipal.jButtonEliminarPiloto.addActionListener(this);
        this.vistaPrincipal.jButtonCancelarPiloto.addActionListener(this);
        
    }

    public void insertarPilotosEnTabla() throws IOException, FileNotFoundException, ClassNotFoundException{
        
        this.vistaPrincipal.cambiarVisibilidadtxfPiloto(false);
        this.vistaPrincipal.cambiarVisibilidadPilotoAC(false);
        
        this.vistaPrincipal.jTablePiloto.setModel(this.modeloPiloto);
        
        Piloto piloto = new Piloto();
        //genero archivo
        this.generarAchivo();
        //leo el archivo
        this.leerDesdeFichero();
//        this.listaPilotos = piloto.obtenerTuplasEjemplo();
        
        ArrayList<String> arrayColumnas = piloto.obtenerCampos();
        for (int i=0; i< arrayColumnas.size();i++){
            modeloPiloto.addColumn(arrayColumnas.get(i));
        }
        
        
        Object[] vectorObjetos = new Object[5];
        for(int i=0; i < listaPilotos.size(); i++){
            vectorObjetos[0] = listaPilotos.get(i).getDNI();
            vectorObjetos[1] = listaPilotos.get(i).getNombre();
            vectorObjetos[2] = listaPilotos.get(i).getApellido();
            vectorObjetos[3] = listaPilotos.get(i).getNombre();
            vectorObjetos[4] = listaPilotos.get(i).getCIF_Aerolineas();
            modeloPiloto.addRow(vectorObjetos);
            
        }
        
        this.actualizarComboBox();
        this.resetTextField();
        
        
    
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(e.getSource() == this.vistaPrincipal.jButtonInsertarPiloto){
            this.insertar=true;
            this.vistaPrincipal.cambiarVisibilidadPilotoAC(true);
            this.vistaPrincipal.cambiarVisibilidadtxfPiloto(true);
            this.vistaPrincipal.jButtonModificarPiloto.setEnabled(false);
            this.vistaPrincipal.jButtonEliminarPiloto.setEnabled(false);
        }
        else if(e.getSource() == this.vistaPrincipal.jButtonAceptarPiloto){
            
            try {
                this.aceptar();
            } catch (IOException ex) {
                Logger.getLogger(PilotoControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try {
                
                    this.escribirEnArchivo();
                    this.actualizarComboBox();
                    this.resetTextField();
                
                
            } catch (IOException ex) {
                Logger.getLogger(PilotoControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        else if(e.getSource() == this.vistaPrincipal.jButtonModificarPiloto){
            this.modificar=true;
            this.vistaPrincipal.cambiarVisibilidadPilotoAC(true);
            this.vistaPrincipal.cambiarVisibilidadtxfPiloto(true);
            this.vistaPrincipal.jButtonInsertarPiloto.setEnabled(false);
            this.vistaPrincipal.jButtonEliminarPiloto.setEnabled(false);
           
        }
        else if(e.getSource() == this.vistaPrincipal.jButtonEliminarPiloto){
            this.eliminar = true;
            this.vistaPrincipal.cambiarVisibilidadPilotoAC(true);
            this.vistaPrincipal.jButtonModificarPiloto.setEnabled(false);
            this.vistaPrincipal.jButtonInsertarPiloto.setEnabled(false);
           
        }
        else if(e.getSource() == this.vistaPrincipal.jButtonCancelarPiloto){
            this.cancelar();
        }
        
    }
    
    public void aceptar() throws IOException{
        if(insertar==true){
            this.insertar();
            
        }
        else if(modificar==true){
            this.modificar();
        }
        else if(eliminar==true){
            this.eliminar();
        }
        
        this.vistaPrincipal.cambiarVisibilidadPilotoAC(false);
        this.vistaPrincipal.cambiarVisibilidadtxfPiloto(false);
        //reinicio los valores
        insertar=false;
        modificar=false;
        eliminar=false;
        pkRepetida=false;
        
        this.resetTextField();
        this.setEnable();
        
        
        if(avionController!=null){
            //actualizo comboBox de Aviones
            avionController.actualizarComboBox();
            avionController.resetTextField();
        }
        noEliminar=false;
    }
    
    public void rellenarEInsertarTextField() {
//        if((((this.vistaPrincipal.txtCIF.getText() == null || this.vistaPrincipal.txtDNI.getText() == null) || this.vistaPrincipal.txtID_piloto.getText() == null) || this.vistaPrincipal.txtSede.getText() == null) || this.vistaPrincipal.txtCiudad.getText() == null ){
//            JOptionPane.showMessageDialog(null, "No puede haber Campos Vacíos");
//        }
        
        for(int i=0; i<PilotoControlador.listaPilotos.size();i++){
                if(this.vistaPrincipal.txtDNI.getText().equals(PilotoControlador.listaPilotos.get(i).getDNI())){
                    JOptionPane.showMessageDialog(null, "No puede haber PKs repetidas");
                    pkRepetida=true;
                }
               
        }

        if(pkRepetida==false){
            Piloto myPiloto = new Piloto(
                this.vistaPrincipal.txtDNI.getText(),
                this.vistaPrincipal.txtNombrePiloto.getText(),
                this.vistaPrincipal.txtApellido.getText(),
                this.vistaPrincipal.txtEdad.getText(), 
                this.vistaPrincipal.txtCIF_aerolinea.getText()
            );

            this.listaPilotos.add(myPiloto);
        }
        
    }
    
    public void resetTextField(){
        this.vistaPrincipal.txtDNI.setText("");
        this.vistaPrincipal.txtNombrePiloto.setText("");
        this.vistaPrincipal.txtApellido.setText("");
        this.vistaPrincipal.txtEdad.setText("");
        this.vistaPrincipal.txtCIF_aerolinea.setText("");  
    }
    public void setEnable(){
        this.vistaPrincipal.jButtonInsertarPiloto.setEnabled(true);
        this.vistaPrincipal.jButtonModificarPiloto.setEnabled(true);
        this.vistaPrincipal.jButtonEliminarPiloto.setEnabled(true);
    }
    
    public void insertar(){
        this.actualizarComboBox();
        this.rellenarEInsertarTextField();   
        
        modeloPiloto.setRowCount(0);
        Object[] vectorObjetos = new Object[5];
        for(int i=0; i < listaPilotos.size(); i++){
            vectorObjetos[0] = listaPilotos.get(i).getDNI();
            vectorObjetos[1] = listaPilotos.get(i).getNombre();
            vectorObjetos[2] = listaPilotos.get(i).getApellido();
            vectorObjetos[3] = listaPilotos.get(i).getEdad();
            vectorObjetos[4] = listaPilotos.get(i).getCIF_Aerolineas();
            

            modeloPiloto.addRow(vectorObjetos);
        }
        if(pkRepetida==false)
            JOptionPane.showMessageDialog(null, "Nueva Fila Insertada");
    }
    public void modificar() {
        
        this.vistaPrincipal.setFila(this.vistaPrincipal.jTablePiloto.getSelectedRow());
        
        //filtro si no ha seleccionado ninguna fila
        if(this.vistaPrincipal.getFila()<0){
            JOptionPane.showMessageDialog(null, "No has seleccionado ninguna Fila");
        }
        else{
         
            //creo una Aerolinea
            Piloto myPiloto = new Piloto(
                PilotoControlador.listaPilotos.get(this.vistaPrincipal.getFila()).getDNI(),
                this.vistaPrincipal.txtNombrePiloto.getText(),
                this.vistaPrincipal.txtApellido.getText(),
                this.vistaPrincipal.txtEdad.getText(),
                this.vistaPrincipal.txtCIF_aerolinea.getText()     
            );
            //actualizo arrayList
            this.listaPilotos.set(this.vistaPrincipal.getFila(), myPiloto);

            //incorporo a model
            this.modeloPiloto.setValueAt(this.vistaPrincipal.txtNombrePiloto.getText(), this.vistaPrincipal.getFila(), 1);
            this.modeloPiloto.setValueAt(this.vistaPrincipal.txtApellido.getText(), this.vistaPrincipal.getFila(), 2);
//            this.modeloPiloto.setValueAt(this.vistaPrincipal.txtApellido.getText(), this.vistaPrincipal.getFila(), 3);
//            this.modeloPiloto.setValueAt(this.vistaPrincipal.txtEdad.getText(), this.vistaPrincipal.getFila(), 4);

        }
    
    }
    
    public void eliminar() throws IOException{
        this.vistaPrincipal.setFila(this.vistaPrincipal.jTablePiloto.getSelectedRow());
        
        if(this.vistaPrincipal.getFila()<0){
            JOptionPane.showMessageDialog(null, "No has seleccionado ninguna Fila");
        }
        else{
            //si hay tuplas puedo eliminar
            if(this.listaPilotos.size()>0){
                int i = this.vistaPrincipal.getFila();
                //compruebo que no está asociado a ningun avion
                for(int j=0; j<AvionControlador.listaAviones.size()&&noEliminar==false;j++){
                    if(AvionControlador.listaAviones.get(j).getDNI_piloto().equals(PilotoControlador.listaPilotos.get(i).getDNI())){
                        JOptionPane.showMessageDialog(null, "Éste Piloto está asociado a uno o varios Aviones");
                        noEliminar = true;
                    }
                }
                if(noEliminar==false){
                    //elimino el Piloto del array
                    PilotoControlador.listaPilotos.remove(this.vistaPrincipal.getFila());
                    this.modeloPiloto.removeRow(this.vistaPrincipal.getFila());
                    JOptionPane.showMessageDialog(null, "Fila Eliminada");
                    
                }
            }
            else{
                JOptionPane.showMessageDialog(null, "No hay Filas a Eliminar");
            }
        }
        
    }
    
    public void cancelar(){
        insertar=false;
        modificar=false;
        eliminar=false;
        pkRepetida=false;
        noEliminar=false;
        this.setEnable();
        this.vistaPrincipal.cambiarVisibilidadtxfPiloto(false);
        this.vistaPrincipal.cambiarVisibilidadPilotoAC(false);
        this.resetTextField();
        
    }
    
    
    public void escribirEnArchivo() throws FileNotFoundException, IOException{
        
        File fichero = new File("Piloto.dat");//declara el fichero
         //conecta el flujo de bytes al flujo de datos
        ObjectOutputStream dataOS = new ObjectOutputStream(new FileOutputStream(fichero));  
  
        for (int i=0;i<this.listaPilotos.size(); i++){ //recorro los arrays    
            
            dataOS.writeObject(this.listaPilotos.get(i)); //escribo la persona en el fichero
            System.out.println("GRABO LOS DATOS DEL ArrayPiloto.");  
        }     
        dataOS.close();  //cerrar stream de salida    
    }
    
    public void generarAchivo() throws FileNotFoundException, IOException{
        
        File fichero = new File("Piloto.dat");//declara el fichero
        FileOutputStream fileout = new FileOutputStream(fichero,true);  //crea el flujo de salida
         //conecta el flujo de bytes al flujo de datos
        ObjectOutputStream dataOS = new ObjectOutputStream(fileout);  
  
        for (int i=0;i<this.listaPilotos.size(); i++){ //recorro los arrays    
            
            dataOS.writeObject(this.listaPilotos.get(i)); //escribo el objeto en el fichero
            System.out.println("GRABO LOS DATOS DEL ArrayPiloto.");  
        }     
        dataOS.close();  //cerrar stream de salida    
    }
    public void leerDesdeFichero() throws FileNotFoundException, IOException, ClassNotFoundException{
        
        ObjectInputStream dataIS = new ObjectInputStream(new FileInputStream("Piloto.dat"));
        
        try {
            while (true) { // lectura del fichero
               Piloto PilotoExtraido = new Piloto();
               
               PilotoExtraido = (Piloto) dataIS.readObject(); // leer un objeto
           
                this.listaPilotos.add(PilotoExtraido);
                
            }
        } catch (EOFException eo) {
                System.out.println("FIN DE LECTURA.");
        } catch (StreamCorruptedException x) {
        }

        dataIS.close(); // cerrar stream de entrada
    }
    
    public void obtenerAvionControlador(AvionControlador avion){
        this.avionController = avion;
    }
   
    
    public void actualizarComboBox(){
        //si el combobox está vacío no elimina
        if(this.vistaPrincipal.jCombPiloto.getItemCount() == 0){
            for(int i=0; i<AerolineasControlador.listaAerolineas.size();i++){
            this.vistaPrincipal.jCombPiloto.addItem(AerolineasControlador.listaAerolineas.get(i).getCIF());
            }
        }
        else{
            this.vistaPrincipal.jCombPiloto.removeAllItems();
            for(int i=0; i<AerolineasControlador.listaAerolineas.size();i++){
                this.vistaPrincipal.jCombPiloto.addItem(AerolineasControlador.listaAerolineas.get(i).getCIF());
            }
        }
        
    }
}
