package com.tallerwebi.dominio.categoriaDia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class CategoriaHistorialTest {
    
    @Test
    public void constructorVacioDeberiaCrearObjeto() {
        CategoriaHistorial categoria = new CategoriaHistorial();

        assertNull(categoria.getId());
        assertNull(categoria.getNombre());
        assertNull(categoria.getFecha());
    }

    @Test
    public void constructorConParametrosDeberiaAsignarValores() {

        LocalDate fecha = LocalDate.of(2026, 6, 10);

        CategoriaHistorial categoria = new CategoriaHistorial("Historia", fecha);

        assertEquals("Historia",categoria.getNombre());
        assertEquals(fecha,categoria.getFecha());
    }

    @Test
    public void setIdDeberiaAsignarIdCorrectamente() {

        CategoriaHistorial categoria = new CategoriaHistorial();
        categoria.setId(10L);

        assertEquals(10L,categoria.getId());
    }

    @Test
    public void setNombreDeberiaAsignarNombreCorrectamente() {

        CategoriaHistorial categoria = new CategoriaHistorial();
        categoria.setNombre("Ciencia");

        assertEquals("Ciencia",categoria.getNombre());
    }

    @Test
    public void setFechaDeberiaAsignarFechaCorrectamente() {

        CategoriaHistorial categoria = new CategoriaHistorial();
        LocalDate fecha = LocalDate.now();
        categoria.setFecha(fecha);

        assertEquals(fecha,categoria.getFecha());
    }
}