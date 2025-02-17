package com.juanboris.springboot.backend.listaCompra.metodosAux;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.juanboris.springboot.backend.listaCompra.models.DTO.ListaComLigeraDTO;
import org.json.JSONObject;
import com.juanboris.springboot.backend.listaCompra.models.entity.ListaCom;
import com.juanboris.springboot.backend.listaCompra.models.entity.Receta;
import com.juanboris.springboot.backend.listaCompra.models.entity.RecetaCalendario;
import com.juanboris.springboot.backend.listaCompra.models.entity.Usuario;

public class MetodosAux {

  public static Date crearFecha() throws ParseException {
    Date fecha = new Date();
    String fechaString;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss", new Locale("es", "ES"));
    fechaString = sdf.format(fecha);
    fecha = sdf.parse(fechaString);

    return fecha;
  }

  //Hacer sequences
  public static <T> Long obtenerProximoId(List<?> coleccion, String tipo) {
    Long idUltimo = 0L;

    switch (tipo) {
      case "Receta":
        List<Receta> recetas = (List<Receta>) coleccion;
        idUltimo = recetas.get(recetas.size() - 1).getRecetaId();
        break;
      case "ListaComp":
        List<ListaComLigeraDTO> listas = (List<ListaComLigeraDTO>) coleccion;
        idUltimo = listas.get(listas.size() - 1).getId();
        break;
      case "RecetaCalendario":
        List<RecetaCalendario> recetasCalendarios = (List<RecetaCalendario>) coleccion;
        idUltimo = recetasCalendarios.get(recetasCalendarios.size() - 1).getId();
        break;
      case "Usuario":
        List<Usuario> usuarios = (List<Usuario>) coleccion;
        idUltimo = usuarios.get(usuarios.size() - 1).getId();
        break;
      default:
        break;
    }
    idUltimo++;
    return idUltimo;
  }

  public static String obtenerUsername(String token) {
    String tokenString = token.split("Bearer")[1].split("\\.")[1];
    String decoded = new String(Base64.getDecoder().decode(tokenString));
    JSONObject obj = new JSONObject(decoded);
    String username = obj.getString("user_name");

    return username;
  }

}
