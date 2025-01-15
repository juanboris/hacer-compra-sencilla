package com.juanboris.springboot.backend.listaCompra.controllers;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.juanboris.springboot.backend.listaCompra.metodosAux.GeneralConstants;
import com.juanboris.springboot.backend.listaCompra.metodosAux.IUploadService;
import com.juanboris.springboot.backend.listaCompra.metodosAux.MetodosAux;
import com.juanboris.springboot.backend.listaCompra.metodosAux.NegativeNumberException;
import com.juanboris.springboot.backend.listaCompra.models.DTO.RecetaDTO;
import com.juanboris.springboot.backend.listaCompra.models.entity.ProductoReceta;
import com.juanboris.springboot.backend.listaCompra.models.entity.ProductoRecetaId;
import com.juanboris.springboot.backend.listaCompra.models.entity.Receta;
import com.juanboris.springboot.backend.listaCompra.models.entity.RecetaCalendario;
import com.juanboris.springboot.backend.listaCompra.models.entity.Usuario;
import com.juanboris.springboot.backend.listaCompra.models.services.AmazonS3BucketService;
import com.juanboris.springboot.backend.listaCompra.models.services.ICalendarioService;
import com.juanboris.springboot.backend.listaCompra.models.services.IRecetaService;
import com.juanboris.springboot.backend.listaCompra.models.services.IUsuarioService;

@CrossOrigin(origins = {GeneralConstants.URL_CONNECTION})
@RestController
@RequestMapping("/api")
@Lazy
public class RecetaRestController {
  @Autowired
  private IRecetaService iRecetaService;

  @Autowired
  private IUsuarioService iUsuarioService;

  @Autowired
  private ICalendarioService iCalendarioService;

  @Autowired
  private AmazonS3BucketService amazonS3BucketService;

  @GetMapping("/recetas")
  public List<Receta> index(@RequestHeader(name = "Authorization") String token) {
    String username = MetodosAux.obtenerUsername(token);
    return iRecetaService.findAll().stream()
        .filter(calendario -> calendario.getUsuario().getId() == obtenerIdUsuario(username).getId())
        .collect(Collectors.toList());
  }

  @GetMapping("/recetas/{id}")
  public ResponseEntity<?> recetaById(@PathVariable Long id) {
    Receta receta = null;
    Map<String, Object> response = new HashMap<>();
    try {
      receta = iRecetaService.findById(id);
    } catch (DataAccessException e) {
      response.put(GeneralConstants.MENSAJE, GeneralConstants.FALLO_GET_BBDD);
      return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    if (receta == null) {
      response.put(GeneralConstants.MENSAJE,
          "La receta ID: " + id + " no existe en la base de datos");
      return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<Receta>(receta, HttpStatus.OK);

  }

  @PostMapping("/recetas")
  public ResponseEntity<?> createReceta(@Valid @RequestBody Receta receta, BindingResult result,
      @RequestHeader(name = "Authorization") String token) {
    Receta newReceta = null;
    Long id;

    String username = MetodosAux.obtenerUsername(token);
    id = iRecetaService.findAll().size() > 0
        ? MetodosAux.obtenerProximoId(iRecetaService.findAll(), "Receta")
        : 1L;
    receta.setRecetaId(id);

    /*
     * Introducción del id del producto y la receta en cada uno de los productos
     */
    receta.getProductos().forEach(pr -> {
      ProductoRecetaId prodProv = new ProductoRecetaId();
      prodProv.setRecetaId(id);
      prodProv.setProductoId(pr.getId().getProductoId());
      pr.setId(prodProv);
    });
    Map<String, Object> response = new HashMap<>();

    try {
      if (receta.getTiempo() < 0) {
        throw new NegativeNumberException("La duración de la receta no puede ser negativa");
      }
      if (result.hasErrors()) {
        List<String> errors = result.getFieldErrors().stream().map(err -> {
          String campo =
              err.getField().contains("[]") ? err.getField().split("\\.")[1] : err.getField();
          return "El campo '" + campo + "' " + err.getDefaultMessage();
        }).collect(Collectors.toList());
        response.put(GeneralConstants.ERRORS_STRING, errors);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
      }
      receta.setUsuario(obtenerIdUsuario(username));
      newReceta = this.iRecetaService.save(receta);
    } catch (DataAccessException e) {
      response.put(GeneralConstants.MENSAJE, GeneralConstants.FALLO_INSERT_BBDD);
      response.put(GeneralConstants.ERROR,
          e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
      return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (NegativeNumberException e) {
      response.put(GeneralConstants.MENSAJE, GeneralConstants.FALLO_INSERT_BBDD);
      response.put(GeneralConstants.ERROR, e.getMessage());
      return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    response.put(GeneralConstants.MENSAJE, "¡La receta ha sido creada con éxito!");
    response.put("receta", newReceta);
    return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
  }

  /* Servicio put para modificar la receta */
  @PutMapping("/recetas/{id}")
  public ResponseEntity<?> update(@Valid @RequestBody Receta receta, BindingResult result,
      @PathVariable Long id) {
    Receta currentReceta = this.iRecetaService.findById(id);
    Receta recetaUpdated = null;
    Map<String, Object> response = new HashMap<>();
    boolean encontrado = false;

    if (result.hasErrors()) {
      List<String> errors = result.getFieldErrors().stream().map(err -> {
        String campo =
            err.getField().contains("[]") ? err.getField().split("\\.")[1] : err.getField();
        return "El campo '" + campo + "' " + err.getDefaultMessage();
      }).collect(Collectors.toList());
      response.put(GeneralConstants.ERRORS_STRING, errors);
      return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
    }

    if (currentReceta == null) {
      response.put(GeneralConstants.MENSAJE,
          "Error: no se pudo editar, la receta ID: " + id + " no existe en la base de datos");
      return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
    }

    try {
      /*
       * Función para revisar si se ha eliminado algún ProductoReceta. Si se ha eliminado, se
       * elimina de su correspondient tabla
       */
      for (ProductoReceta recProd : currentReceta.getProductos()) {
        encontrado = false;
        for (ProductoReceta recProd2 : receta.getProductos()) {
          if (recProd2.getId().equals(recProd.getId())) {
            encontrado = true;
          }
        }
        if (!encontrado) {
          iRecetaService.deleteProducto(recProd.getId().getProductoId(),
              recProd.getId().getRecetaId());
        }
      }
      /*
       * Función que revisa si todos los objetos de la lista de productos de la receta tienen
       * establecido el recetaId. Si no tienen, se lo inserta.
       */
      currentReceta.setProductos(receta.getProductos());
      if (receta.getProductos().size() > 0) {
        currentReceta.getProductos().forEach(prod -> {
          if (prod.getId().getRecetaId() == null) {
            prod.getId().setRecetaId(id);
          }
        });
      } else {
        currentReceta.setProductos(null);
      }
      currentReceta.setNombre(receta.getNombre());
      currentReceta.setDescripcion(receta.getDescripcion());
      currentReceta.setVideoURL(receta.getVideoURL());
      currentReceta.setTiempo(receta.getTiempo());
      currentReceta.setProductos(receta.getProductos());
      recetaUpdated = iRecetaService.save(currentReceta);
    } catch (DataAccessException e) {
      response.put(GeneralConstants.MENSAJE, "Error al actualizar la receta en la base de datos");
      response.put(GeneralConstants.ERROR,
          e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
      return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    response.put(GeneralConstants.MENSAJE, "¡La receta ha sido modificada con éxito!");
    response.put("receta", recetaUpdated);
    return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
  }

  @DeleteMapping("/recetas/{id}")
  public ResponseEntity<?> delete(@PathVariable long id) {
    Map<String, Object> response = new HashMap<>();
    try {
      Receta currentReceta = this.iRecetaService.findById(id);
      IUploadService.borrarImagen(currentReceta);
      if (currentReceta == null) {
        response.put(GeneralConstants.MENSAJE,
            "Error: no se pudo borrar, la receta ID: " + id + " no existe en la base de datos");
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
      }
      for (RecetaCalendario calRec : iCalendarioService.findAll()) {
        if (calRec.getReceta().getRecetaId() == id) {
          iCalendarioService.delete(calRec);
        }
      }
      this.iRecetaService.delete(currentReceta);
    } catch (DataAccessException e) {
      response.put(GeneralConstants.MENSAJE, "Error al borrar la receta en la base de datos");
      response.put(GeneralConstants.ERROR,
          e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
      return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    response.put(GeneralConstants.MENSAJE, "La receta se ha borrado correctamente");

    return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
  }

  /*
   * Subir una imagen. Se establecen dos métodos: uno para local y otro para AWS
   */
  @PostMapping("/recetas/upload")
  public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo,
      @RequestParam("id") Long id) {
    Map<String, Object> response = new HashMap<>();
    String nombreArchivo = "";
    Receta receta = iRecetaService.findById(id);

    if (!archivo.isEmpty()) {
      nombreArchivo =
          UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename().replace(" ", "");

      /* Para guardar el archivo en local */


      /*
       * Path rutaArchivo = Paths.get("uploads").resolve(nombreArchivo).toAbsolutePath(); try {
       * Files.copy(archivo.getInputStream(), rutaArchivo);
       * 
       * } catch (IOException e) { response.put(GeneralConstants.MENSAJE,
       * "Error al subir la imagen " + nombreArchivo); response.put(GeneralConstants.ERROR,
       * e.getMessage().concat(": ").concat(e.getCause().getMessage())); return new
       * ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR); }
       * IUploadService.borrarImagen(receta);
       */

      /* Para guardar el archivo en AWS */

      deleteFile(receta);
      amazonS3BucketService.uploadFile(archivo, nombreArchivo);



      receta.setImagen(nombreArchivo);
      iRecetaService.save(receta);

      response.put("receta", receta);
      response.put(GeneralConstants.MENSAJE, "Ha subido correctamente la imagen: " + nombreArchivo);
    }

    return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
  }

  /**
   * Método para enviar la imagen mantenida en el servidor como un recurso que pueda leer la web.
   * Solo funciona en local o en servidor local
   */
  @GetMapping("/uploads/img/{nombreImagen:.+}")
  public ResponseEntity<Resource> verImagen(@PathVariable String nombreImagen) {
    Path rutaArchivo = Paths.get("uploads").resolve(nombreImagen).toAbsolutePath();
    Resource recurso = null;
    HttpHeaders cabecera = new HttpHeaders();

    try {
      recurso = new UrlResource(rutaArchivo.toUri());
    } catch (MalformedURLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    if (!recurso.exists() && !recurso.isReadable()) {
      Path rutaArchivoImgDEF =
          Paths.get("src/main/resources/static/img").resolve("no-photo.png").toAbsolutePath();
      try {
        recurso = new UrlResource(rutaArchivoImgDEF.toUri());
      } catch (MalformedURLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      System.out.println("Error no se pudo cargar la imagen: " + nombreImagen);
    }

    /* Forzar la descarga */
    cabecera.add(HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=\"" + recurso.getFilename() + "\"");

    return new ResponseEntity<Resource>(recurso, cabecera, HttpStatus.OK);
  }

  /*
   * Método de búsqueda por letra o palabra de las recetas. Se realiza una DTO para mejorar la
   * eficiencia en la comunicación
   */
  @GetMapping("/recetas/filtrar-nombre/{term}")
  @ResponseStatus(HttpStatus.OK)
  public List<RecetaDTO> filtrarNombre(@PathVariable String term,
      @RequestHeader(name = "Authorization") String token) {
    String username = MetodosAux.obtenerUsername(token);
    List<Receta> recetasFiltradas =
        iRecetaService.findByNombreAndUser(term.concat("%"), obtenerIdUsuario(username).getId());
    List<RecetaDTO> recetasFiltrasDtos = new ArrayList<RecetaDTO>();
    for (Receta receta : recetasFiltradas) {
      RecetaDTO recetaDTO =
          new RecetaDTO(receta.getRecetaId(), receta.getNombre(), receta.getProductos());
      recetasFiltrasDtos.add(recetaDTO);
    }
    return recetasFiltrasDtos;

  }

  private Usuario obtenerIdUsuario(String username) {
    return iUsuarioService.findByUsername(username);
  }

  private void deleteFile(Receta receta) {
    String nombreAnterior = receta.getImagen();
    String resultado = "";
    if (nombreAnterior != null && nombreAnterior.length() > 0) {
      resultado = amazonS3BucketService.deleteFileFromBucket(nombreAnterior);
    }
  }
}
