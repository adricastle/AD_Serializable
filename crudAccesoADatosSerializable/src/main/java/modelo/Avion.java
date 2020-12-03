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
public class Avion implements Serializable{
    
    private String id_avion;
    private String modelo;
    private String n_asientos;
    private String fecha_fab;
    private String DNI_piloto;

    public Avion(String id_avion, String modelo, String n_asientos, String fecha_fab, String DNI_piloto) {
        this.id_avion = id_avion;
        this.modelo = modelo;
        this.n_asientos = n_asientos;
        this.fecha_fab = fecha_fab;
        this.DNI_piloto = DNI_piloto;
    }

    public Avion() {
    }
    

    public String getId_avion() {
        return id_avion;
    }

    public void setId_avion(String id_avion) {
        this.id_avion = id_avion;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getN_asientos() {
        return n_asientos;
    }

    public void setN_asientos(String n_asientos) {
        this.n_asientos = n_asientos;
    }

    public String getFecha_fab() {
        return fecha_fab;
    }

    public void setFecha_fab(String fecha_fab) {
        this.fecha_fab = fecha_fab;
    }

    public String getDNI_piloto() {
        return DNI_piloto;
    }

    public void setDNI_piloto(String DNI_piloto) {
        this.DNI_piloto = DNI_piloto;
    }
    
     public ArrayList obtenerCampos(){
        ArrayList<String> myCampos = new ArrayList<>();
       
        myCampos.add("Matrícula");
        myCampos.add("Modelo");
        myCampos.add("Nº_Asientos");
//        myCampos.add("Fecha Fabricación");
//        myCampos.add("Categoría");
        
        return myCampos;
    }
    
    public ArrayList obtenerTuplasEjemplo(){
        ArrayList<Avion> myAvion = new ArrayList<>();
        
        Avion boing = new Avion("7466OK","Boing747","100","22/06/1989","74663392K");
        Avion boing2 = new Avion("58634L","Boing357","140","54/07/1966","74663392K");
        Avion boing3 = new Avion("43758U","Boing747","140","86/05/1976","74663392K");
        Avion boing4 = new Avion("78430I","Boing357","100","86/03/1989","74663392K");
        Avion boing5 = new Avion("52789F","Boing747","140","86/01/1998","74663392K");
        
        myAvion.add(boing);
        myAvion.add(boing2);
        myAvion.add(boing3);
        myAvion.add(boing4);
        myAvion.add(boing5);
        
        return myAvion;
    }
}

