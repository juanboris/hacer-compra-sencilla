package com.juanboris.springboot.backend.listaCompra.controllers;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.juanboris.springboot.backend.listaCompra.metodosAux.GeneralConstants;
import com.juanboris.springboot.backend.listaCompra.metodosAux.MetodosAux;
import com.juanboris.springboot.backend.listaCompra.models.DTO.CalendarioRecetaDTO;
import com.juanboris.springboot.backend.listaCompra.models.entity.ListaCom;
import com.juanboris.springboot.backend.listaCompra.models.entity.ListaCompProducto;
import com.juanboris.springboot.backend.listaCompra.models.entity.ListaCompProductoId;
import com.juanboris.springboot.backend.listaCompra.models.entity.Producto;
import com.juanboris.springboot.backend.listaCompra.models.entity.ProductoReceta;
import com.juanboris.springboot.backend.listaCompra.models.entity.Receta;
import com.juanboris.springboot.backend.listaCompra.models.entity.RecetaCalendario;
import com.juanboris.springboot.backend.listaCompra.models.entity.Usuario;
import com.juanboris.springboot.backend.listaCompra.models.services.ICalendarioService;
import com.juanboris.springboot.backend.listaCompra.models.services.IListaCompService;
import com.juanboris.springboot.backend.listaCompra.models.services.IProductoService;
import com.juanboris.springboot.backend.listaCompra.models.services.IRecetaService;
import com.juanboris.springboot.backend.listaCompra.models.services.IUsuarioService;

@CrossOrigin(origins = {GeneralConstants.URL_CONNECTION})
@RestController
@RequestMapping("/api")
@Lazy
public class CalendarioRestController {

  @Autowired
  private ICalendarioService iCalendarioService;

  @Autowired
  private IRecetaService iRecetaService;

  @Autowired
  private IListaCompService iListaCompService;

  @Autowired
  private IProductoService iProductoService;

  @Autowired
  private IUsuarioService iUsuarioService;

  /* Método GET normal */
  @GetMapping("/calendario")
  public List<CalendarioRecetaDTO> index(@RequestHeader(name = "Authorization") String token) {
    List<RecetaCalendario> calendarios = null;
    List<CalendarioRecetaDTO> calendariosRecetas = new ArrayList<CalendarioRecetaDTO>();

    String username = MetodosAux.obtenerUsername(token);
    calendarios = iCalendarioService.findAll().stream()
        .filter(calendario -> calendario.getUsuario().getId() == obtenerUsuario(username).getId())
        .collect(Collectors.toList());

    for (RecetaCalendario recetaCalendario : calendarios) {
      CalendarioRecetaDTO calendarioRecetaDTO = new CalendarioRecetaDTO(recetaCalendario.getId(),
          recetaCalendario.getReceta().getNombre(), recetaCalendario.getCreated());
      calendariosRecetas.add(calendarioRecetaDTO);
    }

    return calendariosRecetas;
  }

  @PostMapping("/calendario")
  public ResponseEntity<?> crearCalendario(@Valid @RequestBody CalendarioRecetaDTO calendarioDto,
      BindingResult result, @RequestHeader(name = "Authorization") String token) {
    RecetaCalendario newRecetaCalendario = new RecetaCalendario();
    RecetaCalendario recetaRecibida = null;
    CalendarioRecetaDTO calendarioRecetaNuevoDto = null;

    String username = MetodosAux.obtenerUsername(token);
    newRecetaCalendario.setCreated(calendarioDto.getCreated());
    Map<String, Object> response = new HashMap<>();

    List<Receta> recetas = iRecetaService.findAll();
    if (result.hasErrors()) {
      List<String> errors = result.getFieldErrors().stream()
          .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
          .collect(Collectors.toList());
      response.put(GeneralConstants.ERRORS_STRING, errors);
      return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
    }
    try {
      recetas.forEach(rece -> {
        if (rece.getRecetaId() == calendarioDto.getId()) {
          newRecetaCalendario.setReceta(rece);
        }
      });
      newRecetaCalendario.setUsuario(obtenerUsuario(username));
      recetaRecibida = this.iCalendarioService.save(newRecetaCalendario);
      calendarioRecetaNuevoDto = new CalendarioRecetaDTO(recetaRecibida.getId(),
          recetaRecibida.getReceta().getNombre(), recetaRecibida.getCreated());
    } catch (DataAccessException e) {
      response.put(GeneralConstants.MENSAJE, GeneralConstants.FALLO_INSERT_BBDD);
      response.put(GeneralConstants.ERROR,
          e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
      return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    response.put(GeneralConstants.MENSAJE, "¡La receta_calendario ha sido creada con éxito!");
    response.put("recetaCalendario", calendarioRecetaNuevoDto);
    return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
  }

  @DeleteMapping("/calendario/{id}")
  public ResponseEntity<?> delete(@PathVariable long id) {
    Map<String, Object> response = new HashMap<>();

    try {
      RecetaCalendario currentRecetaCalendario = this.iCalendarioService.findById(id);

      if (currentRecetaCalendario == null) {
        response.put(GeneralConstants.MENSAJE, "Error: no se pudo borrar, la receta_calendario ID: "
            + id + " no existe en la base de datos");
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
      }
      this.iCalendarioService.delete(currentRecetaCalendario);
    } catch (DataAccessException e) {
      response.put(GeneralConstants.MENSAJE,
          "Error al borrar la receta_calendario en la base de datos");
      response.put(GeneralConstants.ERROR,
          e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
      return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    response.put(GeneralConstants.MENSAJE, "La receta_calendario se ha borrado correctamente");

    return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
  }

  /*
   * Primero creo una lista de compra, setteo su id y la guardo en la bbdd Luego filtro los
   * receta_calendarios presentes en la bbdd para coger solo los que son de una fecha igual o
   * posterior al día actual. Luego hago varias iteraciones anidadas: la primera recorro todos los
   * productos de la bbdd, dentro de ella los receta_calendario que hemos filtrado y por último los
   * productos_recetas con las que está relacionadas cada producto. Si coincide el recetaId del
   * atributo receta del producto_receta con el recetaId del atributo receta del receta_calendario
   * comienza la lógica para añadir los productos de esta receta a una lista de compra. Primero se
   * crea un objeto listaCom_producto con los datos de producto_receta y de la lista. Cuando se ha
   * creado, se crea una condición para saber si el set donde se van a meter los productos está
   * vacío o no. Si no lo está, se itera sobre la misma para saber si el listaCom_producto que
   * acabamos de crear se encuentra en el set. Si está, se cogen las cantidades del encontrado y con
   * el que se ha comparado para obtener una nueva cantidad. Esta cantidad se setea en el objeto
   * listaComp_producto. Tanto si está como si no está repetido, se añade al set. Si el set está
   * vacío, se creao un objeto listaComp_producto con el producto_receta y se añade al set. Por
   * último, se setea en la lista de compra el set de listaComp_producto y se guarda la lista
   */
  @GetMapping("/calendario/lista")
  public ResponseEntity<?> calendarioLista(@RequestHeader(name = "Authorization") String token) {
    Map<String, Object> response = new HashMap<>();
    ListaCom listaDefecto = new ListaCom();
    ListaCom listaBuena = new ListaCom();
    ListaCom listaBuena2 = new ListaCom();
    Set<ListaCompProducto> prods = new HashSet<ListaCompProducto>();
    List<RecetaCalendario> recetaCalendarios = new ArrayList<RecetaCalendario>();

    String username = MetodosAux.obtenerUsername(token);

    try {
      listaDefecto.setUsuario(obtenerUsuario(username));
      listaBuena = iListaCompService.save(listaDefecto);

    } catch (DataAccessException e) {
      response.put(GeneralConstants.MENSAJE,
          "Error al exportar la receta_calendario a una lista de compra");
      response.put(GeneralConstants.ERROR,
          e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
      return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    Instant instant = Instant.now();
    ZoneId z = ZoneId.of("Europe/Paris");
    ZonedDateTime zdt = instant.atZone(z);
    LocalTime lt = LocalTime.of(00, 00);
    ZonedDateTime zdtAtThreeThirty = zdt.with(lt);
    recetaCalendarios = iCalendarioService.findAll().stream()
        .filter(calendRec -> calendRec.getCreated() != null).filter(calendRec -> calendRec
            .getCreated().toInstant().compareTo(zdtAtThreeThirty.toInstant()) >= 0)
        .collect(Collectors.toList());

    List<Producto> productos = iProductoService.findAll().stream()
        .filter(calendario -> calendario.getUsuario().getId() == obtenerUsuario(username).getId())
        .collect(Collectors.toList());
    for (Producto producto : productos) {
      for (RecetaCalendario recetaCalendario : recetaCalendarios) {
        for (ProductoReceta productoReceta : producto.getRecetas()) {
          if (productoReceta.getReceta().getRecetaId() == recetaCalendario.getReceta()
              .getRecetaId()) {
            ListaCompProductoId listaCompProductoId =
                new ListaCompProductoId(listaBuena.getId(), productoReceta.getId().getProductoId());
            ListaCompProducto listaCompProductoFinal = new ListaCompProducto();
            listaCompProductoFinal.setId(listaCompProductoId);
            listaCompProductoFinal.setListaCom(listaBuena);
            listaCompProductoFinal.setProducto(productoReceta.getProducto());
            listaCompProductoFinal.setCantidad(productoReceta.getCantidad());
            if (prods != null && prods.size() > 0) {
              boolean repetido = false;
              ListaCompProducto listaCompProductoProv = new ListaCompProducto();
              for (ListaCompProducto listaCompProductoProv2 : prods) {
                if (listaCompProductoProv2.getId().getProductoId()
                    .equals(listaCompProductoId.getProductoId())) {
                  repetido = true;
                  listaCompProductoProv = listaCompProductoProv2;
                }
              }
              if (repetido) {
                int cantidad = Integer.parseInt(listaCompProductoFinal.getCantidad())
                    + Integer.parseInt(listaCompProductoProv.getCantidad());
                listaCompProductoFinal.setCantidad(String.valueOf(cantidad));
                prods.remove(listaCompProductoProv);
              }
            }
            prods.add(listaCompProductoFinal);
          }
        }

      }
    }
    listaBuena.setProductos(prods);

    try {
      listaBuena2 = iListaCompService.save(listaBuena);

    } catch (DataAccessException e) {
      response.put(GeneralConstants.MENSAJE,
          "Error al exportar la receta_calendario a una lista de compra");
      response.put(GeneralConstants.ERROR,
          e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
      return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    response.put(GeneralConstants.MENSAJE, "El calendario se ha exportado correctamente");
    response.put("listaCom", listaBuena2);
    return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
  }

  private Usuario obtenerUsuario(String username) {
    return iUsuarioService.findByUsername(username);
  }
}
