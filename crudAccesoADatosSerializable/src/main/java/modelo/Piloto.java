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
public class Piloto implements Serializable{
    
    private String DNI;
    private String nombre;
    private String apellido;
    private String edad;
    private String DNI_avion;
    

    public Piloto() {
    }

    
    public Piloto(String DNI,String nombre, String apellido, String edad, String DNI_avion) {
        this.DNI = DNI;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.DNI_avion = DNI_avion;
    }
    
    
    public String getDNI() {
        return DNI;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    public String getDNI_avion() {
        return DNI_avion;
    }

    public void setDNI_avion(String DNI_avion) {
        this.DNI_avion = DNI_avion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }
    
     public ArrayList obtenerCampos(){
        ArrayList<String> myCampos2 = new ArrayList<>();
       
        myCampos2.add("DNI");
        myCampos2.add("Nombre");
        myCampos2.add("Apellido");
//        myCampos2.add("Apellido");
//        myCampos2.add("edad");
        
        return myCampos2;
    }
    
    public ArrayList obtenerTuplasEjemplo(){
        ArrayList<Piloto> myPiloto = new ArrayList<>();
        
        Piloto pedro = new Piloto("74663392K","Pedro","Aguado","32","7466OK");
        Piloto pedro2 = new Piloto("74663392K","Pedro","Aguado","32","43758U");
        Piloto adri = new Piloto("48556698H","Adrian","Castillo","31","58634L");
        
        myPiloto.add(pedro);
        myPiloto.add(pedro2);
        myPiloto.add(adri);
        
        return myPiloto;
    }
    
}

