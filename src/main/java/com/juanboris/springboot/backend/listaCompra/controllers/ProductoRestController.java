package com.juanboris.springboot.backend.listaCompra.controllers;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import com.juanboris.springboot.backend.listaCompra.metodosAux.GeneralConstants;
import com.juanboris.springboot.backend.listaCompra.metodosAux.MetodosAux;
import com.juanboris.springboot.backend.listaCompra.metodosAux.TiposProductoEnum;
import com.juanboris.springboot.backend.listaCompra.models.DTO.ProductoDTO;
import com.juanboris.springboot.backend.listaCompra.models.entity.ProducHistoricosFecha;
import com.juanboris.springboot.backend.listaCompra.models.entity.Producto;
import com.juanboris.springboot.backend.listaCompra.models.entity.Usuario;
import com.juanboris.springboot.backend.listaCompra.models.services.IProductoService;
import com.juanboris.springboot.backend.listaCompra.models.services.IUsuarioService;
import com.juanboris.springboot.backend.listaCompra.models.services.ProdPrecioHistService;

@CrossOrigin(origins = { GeneralConstants.URL_CONNECTION })
@RestController
@RequestMapping("/api")
public class ProductoRestController {

	@Autowired
	private IProductoService iProductoService;

	@Autowired
	private IUsuarioService iUsuarioService;

	@Autowired
	private ProdPrecioHistService prodPrecioHistService;

	/* Método GET normal */
	@GetMapping("/productos")
	public List<Producto> index(@RequestHeader(name = "Authorization") String token) {
		String username = MetodosAux.obtenerUsername(token);
		return iProductoService.findAll().stream()
				.filter(calendario -> calendario.getUsuario().getId() == obtenerUsuario(username).getId())
				.collect(Collectors.toList());
	}

	/* Método GET por paginación */
	@GetMapping("/productos/page/{page}")
	public Page<Producto> index(@PathVariable Integer page, @RequestHeader(name = "Authorization") String token,
			@RequestParam(required = false) String nombre) {
		String username = MetodosAux.obtenerUsername(token);
		return iProductoService.findAllByUsuarioIdEquals(PageRequest.of(page, 20, Sort.by("id")),
				obtenerUsuario(username).getId(), Objects.isNull(nombre) ? "" : nombre);
	}

	@GetMapping("/productos/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		Producto producto = null;
		Map<String, Object> response = new HashMap<>();

		try {
			producto = iProductoService.findById(id);
		} catch (DataAccessException e) {
			response.put(GeneralConstants.MENSAJE, GeneralConstants.FALLO_GET_BBDD);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (producto == null) {
			response.put(GeneralConstants.MENSAJE, "El producto ID: " + id + " no existe en la base de datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Producto>(producto, HttpStatus.OK);
	}

	@PostMapping("/productos")
	public ResponseEntity<?> createProducto(@Valid @RequestBody ProductoDTO producto, BindingResult result,
			@RequestHeader(name = "Authorization") String token) {
		Producto newProducto = null;
		Producto productoBueno = null;

		String username = MetodosAux.obtenerUsername(token);
		Map<String, Object> response = new HashMap<>();
		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put(GeneralConstants.ERRORS_STRING, errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		try {
			productoBueno = new Producto(producto.getNombre(), producto.getPrecio(), producto.getMarca(),
					producto.getTipo(), obtenerUsuario(username));
			newProducto = this.iProductoService.save(productoBueno);
			guardarPrecioHistorico(newProducto);
			if (Objects.nonNull(newProducto)) {

			}
		} catch (DataAccessException e) {
			response.put(GeneralConstants.MENSAJE, GeneralConstants.FALLO_INSERT_BBDD);
			response.put(GeneralConstants.ERROR,
					e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put(GeneralConstants.MENSAJE, "¡El producto ha sido creado con éxito!");
		response.put("producto", newProducto);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	/* Servicio put para modificar el producto */
	@PutMapping("/productos/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Producto producto, BindingResult result,
			@PathVariable Long id) {
		Producto currentProducto = this.iProductoService.findById(id);
		Producto productoUpdated = null;
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put(GeneralConstants.ERRORS_STRING, errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		if (currentProducto == null) {
			response.put(GeneralConstants.MENSAJE,
					"Error: no se pudo editar, el producto ID: " + id + " no existe en la base de datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {
			currentProducto.setNombre(producto.getNombre());
			currentProducto.setPrecio(producto.getPrecio());
			currentProducto.setMarca(producto.getMarca());
			currentProducto.setTipo(producto.getTipo());
			BigDecimal media = guardarPrecioHistorico(currentProducto);
			if (Objects.nonNull(media)) {
				currentProducto.setMediaPrecio(media);
			}
			productoUpdated = iProductoService.save(currentProducto);
		} catch (DataAccessException e) {
			response.put(GeneralConstants.MENSAJE, GeneralConstants.FALLO_UPDATE_BBDD);
			response.put(GeneralConstants.ERROR,
					e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put(GeneralConstants.MENSAJE, "¡El producto ha sido modificado con éxito!");
		response.put("producto", productoUpdated);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("/productos/{id}")
	public ResponseEntity<?> delete(@PathVariable long id) {
		Map<String, Object> response = new HashMap<>();
		try {
			Producto currentProducto = this.iProductoService.findById(id);

			if (currentProducto == null) {
				response.put(GeneralConstants.MENSAJE,
						"Error: no se pudo borrar, el producto ID: " + id + " no existe en la base de datos");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
			this.iProductoService.delete(currentProducto);
		} catch (DataAccessException e) {
			response.put(GeneralConstants.MENSAJE, "Error al borrar el producto en la base de datos");
			response.put(GeneralConstants.ERROR,
					e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put(GeneralConstants.MENSAJE, "El producto se ha borrado correctamente");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	/*
	 * Método que filtra los nombres de los productos por el término que se envía
	 * desde el front
	 */
	@GetMapping("/productos/filtrar-nombre/{term}")
	@ResponseStatus(HttpStatus.OK)
	public List<ProductoDTO> filtrarNombre(@PathVariable String term,
			@RequestHeader(name = "Authorization") String token) {
		String username = MetodosAux.obtenerUsername(token);
		List<Producto> productos = iProductoService.findByNombreAndUsername(term.concat("%"),
				obtenerUsuario(username).getId());
		List<ProductoDTO> productoDTOs = new ArrayList<ProductoDTO>();
		for (Producto prod : productos) {
			ProductoDTO prodDTO = new ProductoDTO(prod.getId(), prod.getNombre(), prod.getPrecio(), prod.getMarca(),
					prod.getTipo());
			productoDTOs.add(prodDTO);
		}
		return productoDTOs;
	}

	/*
	 * Método que filtra los nombres de los productos por el término que se envía
	 * desde el front. En este caso es para el buscador de lista, que necesita el
	 * objeto Producto completo
	 */
	@GetMapping("/productos/filtrar-nombre2/{term}")
	@ResponseStatus(HttpStatus.OK)
	public List<Producto> filtrarNombre2(@PathVariable String term,
			@RequestHeader(name = "Authorization") String token) {
		String username = MetodosAux.obtenerUsername(token);

		return iProductoService.findByNombreAndUsername(term.concat("%"), obtenerUsuario(username).getId());
	}

	/*
	 * Método que devuelve los tipos de productos existentes
	 */
	@GetMapping("/productos/tiposProd")
	public List<String> tiposProductos() {
		List<String> tiposProducto = new ArrayList<String>();
		TiposProductoEnum[] tiposProductoEnums = TiposProductoEnum.values();
		for (TiposProductoEnum tip : tiposProductoEnums) {
			tiposProducto.add(tip.getTipoProducto());
		}

		return tiposProducto;
	}

	private Usuario obtenerUsuario(String username) {
		return iUsuarioService.findByUsername(username);
	}

	private BigDecimal guardarPrecioHistorico(Producto producto) {
		BigDecimal media = null;
		if (Objects.nonNull(producto) && Objects.nonNull(producto.getPrecio())) {
			ProducHistoricosFecha producHistoricosFecha = new ProducHistoricosFecha(producto,
					new BigDecimal(producto.getPrecio()), Date.from(Instant.now()));
			prodPrecioHistService.save(producHistoricosFecha);
			List<ProducHistoricosFecha> prodPreciosHist = prodPrecioHistService.findByProducto(producto);
			if (prodPreciosHist != null && prodPreciosHist.size() > 0) {
				media = new BigDecimal(prodPreciosHist.stream().mapToDouble(a -> a.getPreciosHistoricos().doubleValue())
						.average().getAsDouble());
			}
		}
		return media;
	}
}
