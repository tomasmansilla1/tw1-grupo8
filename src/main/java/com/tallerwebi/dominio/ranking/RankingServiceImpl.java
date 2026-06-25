package com.tallerwebi.dominio.ranking;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tallerwebi.dominio.usuario.RepositoryUsuario;
import com.tallerwebi.dominio.usuario.Usuario;

@Service
@Transactional
public class RankingServiceImpl implements RankingService {

  private static final int RESPUESTAS_PARA_BONUS = 3;
  private static final int BONUS_RACHA = 200;

  private final RepositoryUsuario repositoryUsuario;

  @Autowired
  public RankingServiceImpl(RepositoryUsuario repositoryUsuario) {
    this.repositoryUsuario = repositoryUsuario;
  }


  @Override
  public Integer calcularPuntaje(Usuario usuario,Integer puntajeBase) {

    int nuevoPuntaje = puntajeBase;

    if (usuario.getPartidasGanadasSeguidas() >= RESPUESTAS_PARA_BONUS) {
      nuevoPuntaje += BONUS_RACHA;
      usuario.setPartidasGanadasSeguidas(puntajeBase);
      System.out.println("DEBUG: Entró al IF, racha reseteada a: " + usuario.getPartidasGanadasSeguidas());
    }

    return nuevoPuntaje;
  }

  @Override
  public List<Usuario> obtenerTop10() {
    return repositoryUsuario.obtenerTopUsuarios();
  }

}