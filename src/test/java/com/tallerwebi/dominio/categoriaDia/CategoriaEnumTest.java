package com.tallerwebi.dominio.categoriaDia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class CategoriaEnumTest {

    @Test
    public void debeContenerTodasLasCategorias() {

        CategoriaEnum[] categorias = CategoriaEnum.values();

        assertEquals(6, categorias.length);

        assertEquals(CategoriaEnum.HISTORIA, categorias[0]);
        assertEquals(CategoriaEnum.CIENCIA, categorias[1]);
        assertEquals(CategoriaEnum.GEOGRAFIA, categorias[2]);
        assertEquals(CategoriaEnum.DEPORTES, categorias[3]);
        assertEquals(CategoriaEnum.ARTE, categorias[4]);
        assertEquals(CategoriaEnum.ENTRETENIMIENTO, categorias[5]);
    }

    @Test
    public void debeObtenerCategoriaPorNombre() {

        CategoriaEnum categoria = CategoriaEnum.valueOf("CIENCIA");

        assertEquals(CategoriaEnum.CIENCIA, categoria);
    }

    @Test
    public void debeLanzarExcepcionSiLaCategoriaNoExiste() {

        assertThrows(
            IllegalArgumentException.class,
            () -> CategoriaEnum.valueOf("MUSICA")
        );
    }
}