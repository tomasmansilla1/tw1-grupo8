package com.tallerwebi.presentacion.login;

import com.tallerwebi.dominio.admin.AdminIniciador;
import com.tallerwebi.dominio.login.DatosLogin;
import com.tallerwebi.dominio.login.ServiceLogin;
import com.tallerwebi.dominio.usuario.ServicioUsuario;
import com.tallerwebi.dominio.usuario.Usuario;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControllerLogin {

  private ServiceLogin servicioLogin;
  private ServicioUsuario servicioUsuario;

  @Autowired
  private AdminIniciador adminIniciador;

  @Autowired
  public ControllerLogin(ServiceLogin servicioLogin, ServicioUsuario servicioUsuario) {
    this.servicioLogin = servicioLogin;
    this.servicioUsuario = servicioUsuario;
  }

  @RequestMapping("/login")
  public ModelAndView irALogin() {
    adminIniciador.crearAdmin();

    ModelMap modelo = new ModelMap();
    modelo.put("datosLogin", new DatosLogin());
    return new ModelAndView("login", modelo);
  }

  @RequestMapping(path = "/validar-login", method = RequestMethod.POST)
  public ModelAndView validarLogin(
    @ModelAttribute("datosLogin") DatosLogin datosLogin,
    HttpServletRequest request
  ) {

    Usuario usuarioBuscado = servicioLogin.consultarUsuario(
      datosLogin.getEmail(),
      datosLogin.getPassword()
    );

    if (usuarioBuscado != null) {

      // Si el ban temporal terminó, se desbanea automáticamente
      if (usuarioBuscado.getFechaFinBan() != null && 
        new Date().after(usuarioBuscado.getFechaFinBan())) 
      {
        usuarioBuscado.desbanear();
        servicioUsuario.modificar(usuarioBuscado);
      }

      // Verificar si está baneado
      if (usuarioBuscado.estaBaneado()) {
        ModelMap model = new ModelMap();

        if (usuarioBuscado.getFechaFinBan() == null) {

          model.put(
            "error", 
            "Tu cuenta fue suspendida permanentemente. <br>Motivo: "
            + usuarioBuscado.getMotivoBan()
          );

        } else {
          SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

          model.put(
            "error", 
            "Tu cuenta está suspendida hasta "
            + formato.format(usuarioBuscado.getFechaFinBan())
            + "<br>Motivo: "
            + usuarioBuscado.getMotivoBan()
          );
        }

        model.put("datosLogin", new DatosLogin());

        return new ModelAndView("login", model);
      }

      request.getSession().setAttribute("usuario", usuarioBuscado);
      request.getSession().setAttribute("rol", usuarioBuscado.getRol());

      return new ModelAndView("redirect:/home");
    }

    ModelMap model = new ModelMap();

    model.put("error", "Usuario o clave incorrecta");
    model.put("datosLogin", new DatosLogin());

    return new ModelAndView("login", model);
  }

  @RequestMapping(path = "/home", method = RequestMethod.GET)
  public ModelAndView irAHome(HttpSession session) {

    if (session.getAttribute("usuario") == null) {
      return new ModelAndView("redirect:/login");
    }

    return new ModelAndView("home");
  }

  @RequestMapping(path = "/", method = RequestMethod.GET)
  public ModelAndView inicio() {
    return new ModelAndView("redirect:/login");
  }

  @RequestMapping("/logout")
  public String logout(HttpSession session) {
    session.invalidate();
    return "redirect:/login";
  }
}
