package com.tallerwebi.dominio.ranking;

import org.springframework.stereotype.Service;

import com.tallerwebi.dominio.usuario.Usuario;
import com.tallerwebi.dominio.usuario.RepositoryUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class RankingServiceImpl implements RankingService {

  private RepositoryUsuario repositorioUsuario;

  private static final int RESPUESTAS_PARA_BONUS = 3;
  private static final int BONUS_RACHA = 200;

  @Autowired
  public RankingServiceImpl(RepositoryUsuario repositorioUsuario) {
    this.repositorioUsuario = repositorioUsuario;
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
    return repositorioUsuario.obtenerTopUsuarios();
  }

}