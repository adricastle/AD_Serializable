/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import controlador.AerolineasControlador;
import modelo.Aerolinea;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author adria
 */
public class TestUnitarios {
    AerolineasControlador aerolineaController = new AerolineasControlador();
    
    public TestUnitarios() {
    }

    @org.junit.jupiter.api.BeforeAll
    public static void setUpClass() throws Exception {
    }

    @org.junit.jupiter.api.AfterAll
    public static void tearDownClass() throws Exception {
    }

    @org.junit.jupiter.api.BeforeEach
    public void setUp() throws Exception {
    }

    @org.junit.jupiter.api.AfterEach
    public void tearDown() throws Exception {
    }
    
    

//     TODO add test methods here.
//     The methods must be annotated with annotation @Test. For example:
    
     @Test
     public void insertarAerolineaEnArrayList() {
         int tamanio = AerolineasControlador.listaAerolineas.size();
         Aerolinea aerolinea = new Aerolinea("47FS","Air Europa","70","MAdrid","4672339U");
         AerolineasControlador.listaAerolineas.add(aerolinea);
         assertEquals(tamanio + 1,AerolineasControlador.listaAerolineas.size());
     }
     
     @Test
     public void modificarAerolineaEnArrayList() {
         String codigo="33KK";
         Aerolinea aerolinea = new Aerolinea("47FS","Air Europa","70","MAdrid","4672339U");
         AerolineasControlador.listaAerolineas.add(aerolinea);
         aerolinea.setCIF("33KK");
         AerolineasControlador.listaAerolineas.set(0, aerolinea);
         assertEquals(codigo,AerolineasControlador.listaAerolineas.get(0).getCIF());
     }
     @Test
     public void eliminarAerolineaEnArrayList() {
         int tamanio = AerolineasControlador.listaAerolineas.size();
         AerolineasControlador.listaAerolineas.remove(0);
         assertEquals(tamanio - 1,AerolineasControlador.listaAerolineas.size());
     }
}
