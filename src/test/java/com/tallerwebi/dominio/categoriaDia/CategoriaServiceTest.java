package com.tallerwebi.dominio.categoriaDia;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.tallerwebi.infraestructura.categoriaDia.CategoriaRepository;

import java.time.LocalDate;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CategoriaServiceTest {

    private CategoriaRepository repositorioMock;
    private CategoriaService categoriaService;

    @BeforeEach
    public void init() {
        repositorioMock = mock(CategoriaRepository.class);
        categoriaService = new CategoriaService(repositorioMock);
    }

    @Test
    public void obtenerCategoriaActivaDeberiaRetornarNombreDeUltimaCategoria() {

        CategoriaHistorial categoria = new CategoriaHistorial();
        categoria.setNombre("Deportes");

        when(repositorioMock.findUltima()).thenReturn(categoria);

        String resultado = categoriaService.obtenerCategoriaActiva();

        assertThat(
            resultado,
            equalToIgnoringCase("Deportes")
        );
        verify(
            repositorioMock, 
            times(1)
        ).findUltima();
    }

    @Test
    public void obtenerCategoriaActivaDeberiaRetornarSinCategoriaSiNoHayDatos() {

        when(repositorioMock.findUltima()).thenReturn(null);
        String resultado = categoriaService.obtenerCategoriaActiva();

        assertThat(
            resultado,
            equalToIgnoringCase("Sin categoría")
        );
        verify(repositorioMock).findUltima();
    }

    @Test
    public void obtenerHistorialDeberiaRetornarListaDelRepositorio() {

        CategoriaHistorial categoria = new CategoriaHistorial();

        List<CategoriaHistorial> historial = singletonList(categoria);
        when(repositorioMock.findAll()).thenReturn(historial);

        List<CategoriaHistorial> resultado = categoriaService.obtenerHistorial();
        assertEquals(historial, resultado);

        verify(
            repositorioMock, 
            times(1)
        ).findAll();
    }

    @Test
    public void guardarNuevaCategoriaDeberiaGuardarCategoriaEnRepositorio() {

        categoriaService.guardarNuevaCategoria("Historia");

        verify(
            repositorioMock, 
            times(1)
        ).save(any(CategoriaHistorial.class));
    }

    @Test
    public void guardarNuevaCategoriaDeberiaGuardarNombreCorrecto() {
        categoriaService.guardarNuevaCategoria("Ciencia");

        verify(repositorioMock).save(
            argThat(categoria->categoria.getNombre().equals("Ciencia"))
        );
    }

    @Test
    public void guardarNuevaCategoriaDeberiaGuardarFechaActual() {
        categoriaService.guardarNuevaCategoria("Historia");

        verify(repositorioMock).save(
            argThat(categoria->categoria.getFecha().equals(LocalDate.now()))
        );
    }
}