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
import modelo.Aerolinea;
import vista.Vista;

/**
 *
 * @author adria
 */
public class AerolineasControlador implements ActionListener {

    private Vista vistaPrincipal;
    private DefaultTableModel modeloAerolineas = new DefaultTableModel();
    public static ArrayList<Aerolinea> listaAerolineas = new ArrayList<>();
    
    boolean insertar=false;
    boolean modificar=false;
    boolean eliminar=false;
    boolean pkRepetida=false;
    boolean noEliminar=false;

    public AerolineasControlador() {
    }
    
    
    public AerolineasControlador(Vista v) {
        
        this.vistaPrincipal=v;
        
        this.vistaPrincipal.jButtonInsertarAerolineas.addActionListener(this);
        this.vistaPrincipal.jButtonAceptarAerolineas.addActionListener(this);
        this.vistaPrincipal.jButtonModificarAerolineas.addActionListener(this);
        this.vistaPrincipal.jButtonEliminarAerolineas.addActionListener(this);
        this.vistaPrincipal.jButtonCancelarAerolineas.addActionListener(this);
        
    }

    public void insertarAerolineasEnTabla() throws IOException, FileNotFoundException, ClassNotFoundException{
        
        //cambio a false para que no se editen los txtfields y los botones aceptar y cancelar
        this.vistaPrincipal.cambiarVisibilidadtxfAerolineas(false);
        this.vistaPrincipal.cambiarVisibilidadAerolineasAC(false);
        
        this.vistaPrincipal.jTableAerolineas.setModel(this.modeloAerolineas);
        
        Aerolinea aerolinea = new Aerolinea("","","","","");
        //genero archivo
        this.generarAchivo();
        //leo el archivo
        this.leerDesdeFichero();
        
        
//        this.listaAerolineas = aerolinea.obtenerTuplasEjemplo();
        
        ArrayList<String> arrayColumnas = aerolinea.obtenerCampos();
        
        for (int i=0; i < arrayColumnas.size(); i++){
            modeloAerolineas.addColumn(arrayColumnas.get(i));
        }
        
        Object[] vectorObjetos = new Object[5];
        for(int i=0; i < listaAerolineas.size(); i++){
            vectorObjetos[0] = listaAerolineas.get(i).getCIF();
            vectorObjetos[1] = listaAerolineas.get(i).getNombre();
            vectorObjetos[2] = listaAerolineas.get(i).getN_empleados();
            vectorObjetos[3] = listaAerolineas.get(i).getSede();
            vectorObjetos[4] = listaAerolineas.get(i).getDNI_piloto();
            modeloAerolineas.addRow(vectorObjetos);
            
        }
    
        this.actualizarComboBox();
        this.resetTextField();
        
    
    }
    

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(e.getSource() == this.vistaPrincipal.jButtonInsertarAerolineas){
            this.insertar=true;
            this.vistaPrincipal.cambiarVisibilidadAerolineasAC(true);
            this.vistaPrincipal.cambiarVisibilidadtxfAerolineas(true);
            this.vistaPrincipal.jButtonModificarAerolineas.setEnabled(false);
            this.vistaPrincipal.jButtonEliminarAerolineas.setEnabled(false);
        }
        else if(e.getSource() == this.vistaPrincipal.jButtonAceptarAerolineas){
            
            try {
                this.aceptar();
            } catch (IOException ex) {
                Logger.getLogger(AerolineasControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try {
                this.escribirEnArchivo();
                this.actualizarComboBox();
                this.resetTextField();
            } catch (IOException ex) {
                Logger.getLogger(AerolineasControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        else if(e.getSource() == this.vistaPrincipal.jButtonModificarAerolineas){
            this.modificar=true;
            this.vistaPrincipal.cambiarVisibilidadAerolineasAC(true);
            this.vistaPrincipal.cambiarVisibilidadtxfAerolineas(true);
            this.vistaPrincipal.jButtonInsertarAerolineas.setEnabled(false);
            this.vistaPrincipal.jButtonEliminarAerolineas.setEnabled(false);
           
        }
        else if(e.getSource() == this.vistaPrincipal.jButtonEliminarAerolineas){
            this.eliminar = true;
            this.vistaPrincipal.cambiarVisibilidadAerolineasAC(true);
            this.vistaPrincipal.jButtonModificarAerolineas.setEnabled(false);
            this.vistaPrincipal.jButtonInsertarAerolineas.setEnabled(false);
           
        }
        else if(e.getSource() == this.vistaPrincipal.jButtonCancelarAerolineas){
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
        
        
        this.vistaPrincipal.cambiarVisibilidadAerolineasAC(false);
        this.vistaPrincipal.cambiarVisibilidadtxfAerolineas(false);
        //reinicio los valores
        insertar=false;
        modificar=false;
        eliminar=false;
        pkRepetida=false;
        noEliminar=false;
        this.resetTextField();
        this.setEnable();
        
        
    }
    
    public void rellenarEInsertarTextField() {
        if((((this.vistaPrincipal.txtCIF.getText() == null || this.vistaPrincipal.txtNombre.getText() == null) || this.vistaPrincipal.txtNEmpleados.getText() == null) || this.vistaPrincipal.txtSede.getText() == null) || this.vistaPrincipal.txtDNI_piloto.getText() == null ){
            JOptionPane.showMessageDialog(null, "No puede haber Campos Vacíos");
        }
        else{
            
            for(int i=0; i<AerolineasControlador.listaAerolineas.size();i++){
                if(this.vistaPrincipal.txtCIF.getText().equals(AerolineasControlador.listaAerolineas.get(i).getCIF())){
                    JOptionPane.showMessageDialog(null, "No puede haber PKs repetidas");
                    pkRepetida=true;
                }
               
            }
            if(pkRepetida==false){
                Aerolinea myAerolinea = new Aerolinea(
                this.vistaPrincipal.txtCIF.getText(),
                this.vistaPrincipal.txtNombre.getText(),
                this.vistaPrincipal.txtNEmpleados.getText(),
                this.vistaPrincipal.txtSede.getText(),
                this.vistaPrincipal.txtDNI_piloto.getText()
                ); 
                AerolineasControlador.listaAerolineas.add(myAerolinea);
            }
          
        }
        
        
    }
    
    public void resetTextField(){
        this.vistaPrincipal.txtCIF.setText("");
        this.vistaPrincipal.txtNombre.setText("");
        this.vistaPrincipal.txtNEmpleados.setText("");
        this.vistaPrincipal.txtSede.setText("");
        this.vistaPrincipal.txtDNI_piloto.setText("");  
    }
    public void setEnable(){
        this.vistaPrincipal.jButtonInsertarAerolineas.setEnabled(true);
        this.vistaPrincipal.jButtonModificarAerolineas.setEnabled(true);
        this.vistaPrincipal.jButtonEliminarAerolineas.setEnabled(true);
    }
    
    public void insertar(){
//        this.actualizarComboBox();
        this.rellenarEInsertarTextField();   
        
        modeloAerolineas.setRowCount(0);
        Object[] vectorObjetos = new Object[5];
        for(int i=0; i < listaAerolineas.size(); i++){
            vectorObjetos[0] = listaAerolineas.get(i).getCIF();
            vectorObjetos[1] = listaAerolineas.get(i).getNombre();
            vectorObjetos[2] = listaAerolineas.get(i).getN_empleados();
            vectorObjetos[3] = listaAerolineas.get(i).getSede();
            vectorObjetos[4] = listaAerolineas.get(i).getDNI_piloto();
            

            modeloAerolineas.addRow(vectorObjetos);
        }
        
        if(pkRepetida==false)
            JOptionPane.showMessageDialog(null, "Nueva Fila Insertada");
    }
    public void modificar() {
        
//        this.actualizarComboBox();
        this.vistaPrincipal.setFila(this.vistaPrincipal.jTableAerolineas.getSelectedRow());
        //filtro si no ha seleccionado ninguna fila
        if(this.vistaPrincipal.getFila()<0){
            JOptionPane.showMessageDialog(null, "No has seleccionado ninguna Fila");
        }
        else{
         
            //creo una Aerolinea
            Aerolinea myAerolinea = new Aerolinea(
                AerolineasControlador.listaAerolineas.get(this.vistaPrincipal.getFila()).getCIF(),
                this.vistaPrincipal.txtNombre.getText(),
                this.vistaPrincipal.txtNEmpleados.getText(),
                this.vistaPrincipal.txtSede.getText(),
                this.vistaPrincipal.txtDNI_piloto.getText()        
            );
            //actualizo arrayList
            this.listaAerolineas.set(this.vistaPrincipal.getFila(), myAerolinea);

            //incorporo a model
            this.modeloAerolineas.setValueAt(this.vistaPrincipal.txtNombre.getText(), this.vistaPrincipal.getFila(), 1);
            this.modeloAerolineas.setValueAt(this.vistaPrincipal.txtNEmpleados.getText(), this.vistaPrincipal.getFila(), 2);
//            this.modeloAerolineas.setValueAt(this.vistaPrincipal.txtSede.getText(), this.vistaPrincipal.getFila(), 3);
//            this.modeloAerolineas.setValueAt(this.vistaPrincipal.txtDNI_piloto.getText(), this.vistaPrincipal.getFila(), 4);

        }
    
    }
    
    public void eliminar() throws IOException{
        
        this.vistaPrincipal.setFila(this.vistaPrincipal.jTableAerolineas.getSelectedRow());
        if(this.vistaPrincipal.getFila()<0){
            JOptionPane.showMessageDialog(null, "No has seleccionado ninguna Fila");
        }
        else{
            if(this.listaAerolineas.size()>0){
                int i = this.vistaPrincipal.getFila();
                //compruebo que no está asociado a ningun Piloto
                for(int j=0; j<PilotoControlador.listaPilotos.size();j++){
                    if(AerolineasControlador.listaAerolineas.get(i).getDNI_piloto().equals(PilotoControlador.listaPilotos.get(j).getDNI())){
                        JOptionPane.showMessageDialog(null, "Ésta Aerolinea está asociada a uno o varios Pilotos");
                        noEliminar = true;
                    }
                }
                if(noEliminar==false){
                    //elimino la Aerolinea del array
                    AerolineasControlador.listaAerolineas.remove(i);
                    this.modeloAerolineas.removeRow(this.vistaPrincipal.getFila());
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
        this.vistaPrincipal.cambiarVisibilidadtxfAerolineas(false);
        this.vistaPrincipal.cambiarVisibilidadAerolineasAC(false);
        this.resetTextField();
        
    }
    public void escribirEnArchivo() throws FileNotFoundException, IOException{
        
        File fichero = new File("aerolineas.dat");//declara el fichero
         //conecta el flujo de bytes al flujo de datos
        ObjectOutputStream dataOS = new ObjectOutputStream(new FileOutputStream(fichero));  
  
        for (int i=0;i<this.listaAerolineas.size(); i++){ //recorro los arrays    
            
            dataOS.writeObject(this.listaAerolineas.get(i)); //escribo la persona en el fichero
            System.out.println("GRABO LOS DATOS DEL ArrayAerolineas.");  
        }     
        dataOS.close();  //cerrar stream de salida    
    }
    
    public void generarAchivo() throws FileNotFoundException, IOException{
        
        File fichero = new File("aerolineas.dat");//declara el fichero
        FileOutputStream fileout = new FileOutputStream(fichero,true);  //crea el flujo de salida
         //conecta el flujo de bytes al flujo de datos
        ObjectOutputStream dataOS = new ObjectOutputStream(fileout);  
  
        for (int i=0;i<this.listaAerolineas.size(); i++){ //recorro los arrays    
            
            dataOS.writeObject(this.listaAerolineas.get(i)); //escribo el objeto en el fichero
            System.out.println("GRABO LOS DATOS DEL ArrayAerolineas.");  
        }     
        dataOS.close();  //cerrar stream de salida    
    }
    public void leerDesdeFichero() throws FileNotFoundException, IOException, ClassNotFoundException{
        
        ObjectInputStream dataIS = new ObjectInputStream(new FileInputStream("aerolineas.dat"));
      
        try {
            while (true) { // lectura del fichero
               Aerolinea aerolineaExtraida = new Aerolinea();
               
               aerolineaExtraida = (Aerolinea) dataIS.readObject(); // leer un objeto
           
                this.listaAerolineas.add(aerolineaExtraida);
                
            }
        } catch (EOFException eo) {
                System.out.println("FIN DE LECTURA.");
        } catch (StreamCorruptedException x) {
        }

        dataIS.close(); // cerrar stream de entrada
    }
    
    public void actualizarComboBox(){
        //si el combobox está vacío no elimina
        if(this.vistaPrincipal.jCombAerolineas.getItemCount() == 0){
            for(int i=0; i<PilotoControlador.listaPilotos.size();i++){
            this.vistaPrincipal.jCombAerolineas.addItem(PilotoControlador.listaPilotos.get(i).getDNI());
            }
        }
        else{
            this.vistaPrincipal.jCombAerolineas.removeAllItems();
            for(int i=0; i<PilotoControlador.listaPilotos.size();i++){
                this.vistaPrincipal.jCombAerolineas.addItem(PilotoControlador.listaPilotos.get(i).getDNI());
            }
        }
    }
    
}
