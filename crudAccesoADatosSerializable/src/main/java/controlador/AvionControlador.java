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
import modelo.Avion;


import vista.Vista;

/**
 *
 * @author adria
 */
public class AvionControlador implements ActionListener {
    
    Vista vistaPrincipal;
    public static DefaultTableModel modeloAvion = new DefaultTableModel();
    public static ArrayList<Avion> listaAviones = new ArrayList<>();
    private PilotoControlador pilotoController;
    
    boolean insertar=false;
    boolean modificar=false;
    boolean eliminar=false;
    boolean pkRepetida = false;

    public AvionControlador(Vista v) {
        this.vistaPrincipal = v;
        
        this.vistaPrincipal.jButtonInsertarAvion.addActionListener(this);
        this.vistaPrincipal.jButtonAceptarAvion.addActionListener(this);
        this.vistaPrincipal.jButtonModificarAvion.addActionListener(this);
        this.vistaPrincipal.jButtonEliminarAvion.addActionListener(this);
        this.vistaPrincipal.jButtonCancelarAvion.addActionListener(this);
        
    }
   
    
    public void insertarAvionesEnTabla() throws IOException, FileNotFoundException, ClassNotFoundException{
        
        this.vistaPrincipal.cambiarVisibilidadtxfAvion(false);
        this.vistaPrincipal.cambiarVisibilidadAvionAC(false);
        
        this.vistaPrincipal.jTableAvion.setModel(this.modeloAvion);
        
        Avion avion = new Avion();
        //genero archivo
        this.generarAchivo();
        //leo el archivo
        this.leerDesdeFichero();
//        this.listaAviones = avion.obtenerTuplasEjemplo();
        
        ArrayList<String> arrayColumnas = avion.obtenerCampos();
        for (int i=0; i< arrayColumnas.size();i++){
            modeloAvion.addColumn(arrayColumnas.get(i));
        }
        
        
        Object[] vectorObjetos = new Object[5];
        for(int i=0; i < listaAviones.size(); i++){
            vectorObjetos[0] = listaAviones.get(i).getId_avion();
            vectorObjetos[1] = listaAviones.get(i).getModelo();
            vectorObjetos[2] = listaAviones.get(i).getN_asientos();
            vectorObjetos[3] = listaAviones.get(i).getFecha_fab();
            vectorObjetos[4] = listaAviones.get(i).getCategoria();
            modeloAvion.addRow(vectorObjetos);
            
        }
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(e.getSource() == this.vistaPrincipal.jButtonInsertarAvion){
            this.insertar=true;
            this.vistaPrincipal.cambiarVisibilidadAvionAC(true);
            this.vistaPrincipal.cambiarVisibilidadtxfAvion(true);
            this.vistaPrincipal.jButtonModificarAvion.setEnabled(false);
            this.vistaPrincipal.jButtonEliminarAvion.setEnabled(false);
        }
        else if(e.getSource() == this.vistaPrincipal.jButtonAceptarAvion){
            
            try {
                this.aceptar();
            } catch (IOException ex) {
                Logger.getLogger(AvionControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try {
                this.escribirEnArchivo();
            } catch (IOException ex) {
                Logger.getLogger(AvionControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if(e.getSource() == this.vistaPrincipal.jButtonModificarAvion){
            this.modificar=true;
            this.vistaPrincipal.cambiarVisibilidadAvionAC(true);
            this.vistaPrincipal.cambiarVisibilidadtxfAvion(true);
            this.vistaPrincipal.jButtonInsertarAvion.setEnabled(false);
            this.vistaPrincipal.jButtonEliminarAvion.setEnabled(false);
           
        }
        else if(e.getSource() == this.vistaPrincipal.jButtonEliminarAvion){
            this.eliminar = true;
            this.vistaPrincipal.cambiarVisibilidadAvionAC(true);
            this.vistaPrincipal.jButtonModificarAvion.setEnabled(false);
            this.vistaPrincipal.jButtonInsertarAvion.setEnabled(false);
           
        }
        else if(e.getSource() == this.vistaPrincipal.jButtonCancelarAvion){
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
        
            
        this.vistaPrincipal.cambiarVisibilidadAvionAC(false);
        this.vistaPrincipal.cambiarVisibilidadtxfAvion(false);
        //reinicio los valores
        insertar=false;
        modificar=false;
        eliminar=false;
        pkRepetida=false;
        this.resetTextField();
        this.setEnable();
        //actualizo comboBox de Pilotos
        pilotoController.actualizarComboBox();
        pilotoController.resetTextField();
        
    }
    
    public void noPkRepetida(){
        for(int i=0; i<AvionControlador.listaAviones.size();i++){
                if(this.vistaPrincipal.txtID_avion.getText().equals(AvionControlador.listaAviones.get(i).getId_avion())){
                    JOptionPane.showMessageDialog(null, "No puede haber PKs repetidas");
                    pkRepetida=true;
                }
               
        }
    }
    
    public void rellenarEInsertarTextField() {
        for(int i=0; i<AvionControlador.listaAviones.size();i++){
                if(this.vistaPrincipal.txtID_avion.getText().equals(AvionControlador.listaAviones.get(i).getId_avion())){
                    JOptionPane.showMessageDialog(null, "No puede haber PKs repetidas");
                    pkRepetida=true;
                }
               
        }
        
        
        if(pkRepetida==false){
            Avion myAvion = new Avion(
                this.vistaPrincipal.txtID_avion.getText(),
                this.vistaPrincipal.txtModelo.getText(),
                this.vistaPrincipal.txtN_asientos.getText(),
                this.vistaPrincipal.txtFecha_fab.getText(),
                this.vistaPrincipal.txtCategoria.getText()  
            );

            this.listaAviones.add(myAvion);
        }
        
    }
    
    public void resetTextField(){
        this.vistaPrincipal.txtID_avion.setText("");
        this.vistaPrincipal.txtModelo.setText("");
        this.vistaPrincipal.txtN_asientos.setText("");
        this.vistaPrincipal.txtFecha_fab.setText("");
        this.vistaPrincipal.txtCategoria.setText("");  
    }
    public void setEnable(){
        this.vistaPrincipal.jButtonInsertarAvion.setEnabled(true);
        this.vistaPrincipal.jButtonModificarAvion.setEnabled(true);
        this.vistaPrincipal.jButtonEliminarAvion.setEnabled(true);
    }
    
    public void insertar(){
        
        this.rellenarEInsertarTextField();   
        
        modeloAvion.setRowCount(0);
        Object[] vectorObjetos = new Object[5];
        for(int i=0; i < listaAviones.size(); i++){
            vectorObjetos[0] = listaAviones.get(i).getId_avion();
            vectorObjetos[1] = listaAviones.get(i).getModelo();
            vectorObjetos[2] = listaAviones.get(i).getN_asientos();
            vectorObjetos[3] = listaAviones.get(i).getFecha_fab();
            vectorObjetos[4] = listaAviones.get(i).getCategoria();

            modeloAvion.addRow(vectorObjetos);
        }
        if(pkRepetida==false)
            JOptionPane.showMessageDialog(null, "Nueva Fila Insertada");
    }
    public void modificar() {
        
        this.vistaPrincipal.setFila(this.vistaPrincipal.jTableAvion.getSelectedRow());
        //filtro si no ha seleccionado ninguna fila
        if(this.vistaPrincipal.getFila()<0){
            JOptionPane.showMessageDialog(null, "No has seleccionado ninguna Fila");
        }
        else{
         
            //creo una Aerolinea
            Avion myAvion = new Avion(
                AvionControlador.listaAviones.get(this.vistaPrincipal.getFila()).getId_avion(),
                this.vistaPrincipal.txtModelo.getText(),
                this.vistaPrincipal.txtN_asientos.getText(),
                this.vistaPrincipal.txtFecha_fab.getText(),
                this.vistaPrincipal.txtCategoria.getText()        
            );
            //actualizo arrayList
            this.listaAviones.set(this.vistaPrincipal.getFila(), myAvion);

            //incorporo a model
            this.modeloAvion.setValueAt(this.vistaPrincipal.txtModelo.getText(), this.vistaPrincipal.getFila(), 1);
            this.modeloAvion.setValueAt(this.vistaPrincipal.txtN_asientos.getText(), this.vistaPrincipal.getFila(), 2);
//            this.modeloAvion.setValueAt(this.vistaPrincipal.txtFecha_fab.getText(), this.vistaPrincipal.getFila(), 3);
//            this.modeloAvion.setValueAt(this.vistaPrincipal.txtCategoria.getText(), this.vistaPrincipal.getFila(), 4);

        }
    
    }
    
    public void eliminar() throws IOException{
        this.vistaPrincipal.setFila(this.vistaPrincipal.jTableAvion.getSelectedRow());
        PilotoControlador myPiloto = new PilotoControlador(this.vistaPrincipal);
        //filtro si no ha seleccionado ninguna fila
        if(this.vistaPrincipal.getFila()<0){
            JOptionPane.showMessageDialog(null, "No has seleccionado ninguna Fila");
        }
        else{
            if(this.listaAviones.size()>0){
                int i = this.vistaPrincipal.getFila();
                //elimino el campo DNI_avion que tiene el Piloto asociado
                for(int j=0; j<PilotoControlador.listaPilotos.size();j++){
                    if(AvionControlador.listaAviones.get(i).getId_avion().equals(PilotoControlador.listaPilotos.get(j).getDNI_avion())){
                        PilotoControlador.listaPilotos.get(j).setDNI_avion(" ");
                        //sobreescribo el archivo modificado
                        myPiloto.escribirEnArchivo();
                    }
                }
                //elimino el avion del array
                this.listaAviones.remove(this.vistaPrincipal.getFila());

                this.modeloAvion.removeRow(this.vistaPrincipal.getFila());
                JOptionPane.showMessageDialog(null, "Fila Eliminada");
                
                
                
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
        this.setEnable();
        this.vistaPrincipal.cambiarVisibilidadtxfAvion(false);
        this.vistaPrincipal.cambiarVisibilidadAvionAC(false);
        this.resetTextField();
        
    }
    
    public void escribirEnArchivo() throws FileNotFoundException, IOException{
        
        File fichero = new File("avion.dat");//declara el fichero
         //conecta el flujo de bytes al flujo de datos
        ObjectOutputStream dataOS = new ObjectOutputStream(new FileOutputStream(fichero));  
  
        for (int i=0;i<this.listaAviones.size(); i++){ //recorro los arrays    
            
            dataOS.writeObject(this.listaAviones.get(i)); //escribo la persona en el fichero
            System.out.println("GRABO LOS DATOS DEL Avion.");  
        }     
        dataOS.close();  //cerrar stream de salida    
    }
    
    public void generarAchivo() throws FileNotFoundException, IOException{
        
        File fichero = new File("avion.dat");//declara el fichero
        FileOutputStream fileout = new FileOutputStream(fichero,true);  //crea el flujo de salida
         //conecta el flujo de bytes al flujo de datos
        ObjectOutputStream dataOS = new ObjectOutputStream(fileout);  
  
        for (int i=0;i<this.listaAviones.size(); i++){ //recorro los arrays    
            
            dataOS.writeObject(this.listaAviones.get(i)); //escribo el objeto en el fichero
            System.out.println("GRABO LOS DATOS DEL Avion.");  
        }     
        dataOS.close();  //cerrar stream de salida    
    }
    public void leerDesdeFichero() throws FileNotFoundException, IOException, ClassNotFoundException{
        
        ObjectInputStream dataIS = new ObjectInputStream(new FileInputStream("Avion.dat"));
        
        try {
            while (true) { // lectura del fichero
               Avion AvionExtraido = new Avion();
               
               AvionExtraido = (Avion) dataIS.readObject(); // leer un objeto
           
                this.listaAviones.add(AvionExtraido);
                
            }
        } catch (EOFException eo) {
                System.out.println("FIN DE LECTURA.");
        } catch (StreamCorruptedException x) {
        }

        dataIS.close(); // cerrar stream de entrada
    }
    public void obtenerPilotoControlador(PilotoControlador piloto){
        this.pilotoController = piloto;
    }
}
