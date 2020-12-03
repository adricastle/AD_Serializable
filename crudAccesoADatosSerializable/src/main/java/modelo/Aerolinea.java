/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author adria
 */
public class Aerolinea implements Serializable {
    
    private String CIF;
    private String nombre;
    private String n_empleados;
    private String sede;
    private String N_departamentos;

    public Aerolinea(String CIF, String nombre, String n_empleados, String sede, String N_departamentos) {
        this.CIF = CIF;
        this.nombre = nombre;
        this.n_empleados = n_empleados;
        this.sede = sede;
        this.N_departamentos = N_departamentos;
    }

    public Aerolinea() {
    }
    
    

    public String getCIF() {
        return CIF;
    }

    public void setCIF(String CIF) {
        this.CIF = CIF;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getN_empleados() {
        return n_empleados;
    }

    public void setN_empleados(String n_empleados) {
        this.n_empleados = n_empleados;
    }

    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    public String getN_departamentos() {
        return N_departamentos;
    }

    public void setN_departamentos(String N_departamentos) {
        this.N_departamentos = N_departamentos;
    }
    
    public ArrayList obtenerCampos(){
        ArrayList<String> myCampos = new ArrayList<>();
       
        myCampos.add("CIF");
        myCampos.add("Nombre");
        myCampos.add("Nº_empleados");
//        myCampos.add("Sede");
//        myCampos.add("N_departamentos");
        
        return myCampos;
    }
    
    public ArrayList obtenerTuplasEjemplo(){
        ArrayList<Aerolinea> myAerolinea = new ArrayList<>();
        
        Aerolinea ryanair = new Aerolinea("45GG","Ryanair","45","MAdrid","4");
        Aerolinea vueling = new Aerolinea("65FT","Vueling","653","Barcelona","6");
        Aerolinea pegasus = new Aerolinea("12HY","Pegasus","539","Ancara","7");
        
        myAerolinea.add(ryanair);
        myAerolinea.add(vueling);
        myAerolinea.add(pegasus);
        
        return myAerolinea;
    }
}
