package com.juanboris.springboot.backend.listaCompra.controllers;

import com.amazonaws.util.StringUtils;
import com.juanboris.springboot.backend.listaCompra.metodosAux.GeneralConstants;
import com.juanboris.springboot.backend.listaCompra.metodosAux.MetodosAux;
import com.juanboris.springboot.backend.listaCompra.metodosAux.NegativeNumberException;
import com.juanboris.springboot.backend.listaCompra.metodosAux.ProductoNoEncontradoException;
import com.juanboris.springboot.backend.listaCompra.models.DTO.ListComLigeraProductsDetailedDTO;
import com.juanboris.springboot.backend.listaCompra.models.DTO.ListaComLigeraDTO;
import com.juanboris.springboot.backend.listaCompra.models.DTO.ListaComProductoIds;
import com.juanboris.springboot.backend.listaCompra.models.DTO.ListaComProductsDetailedDTO;
import com.juanboris.springboot.backend.listaCompra.models.entity.*;
import com.juanboris.springboot.backend.listaCompra.models.services.IUsuarioService;
import com.juanboris.springboot.backend.listaCompra.models.services.ListaCompServiceImpl;
import com.juanboris.springboot.backend.listaCompra.models.services.ProdPrecioHistService;
import com.juanboris.springboot.backend.listaCompra.models.services.ProductoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = { GeneralConstants.URL_CONNECTION })
@RestController
@RequestMapping("/api")
public class ListaCompController {

	@Autowired
	private ListaCompServiceImpl iListaCompService;

	@Autowired
	private ProductoServiceImpl iProductoService;

	@Autowired
	private IUsuarioService iUsuarioService;

	@Autowired
	private ProdPrecioHistService prodPrecioHistService;

	/* Método GET normal. Filtra por usuario en la propia consulta (ver findAllByUsuarioId) */
	@GetMapping(GeneralConstants.LISTA_COMP_RUTA)
	public List<ListaComLigeraDTO> index(@RequestHeader(name = "Authorization") String token) {
		String username = MetodosAux.obtenerUsername(token);
		return iListaCompService.findAllByUsuarioId(obtenerUsuario(username).getId());
	}

	@GetMapping("/listaComp/{id}")
	public ResponseEntity<?> listaCompById(@PathVariable Long id) {
		ListComLigeraProductsDetailedDTO listComLigeraProductsDetailedDTO = null;

		Map<String, Object> response = new HashMap<>();

		try {
			listComLigeraProductsDetailedDTO = iListaCompService.findByIdLazy(id);
		} catch (DataAccessException e) {
			response.put(GeneralConstants.MENSAJE, GeneralConstants.FALLO_GET_BBDD);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (listComLigeraProductsDetailedDTO == null) {
			response.put(GeneralConstants.MENSAJE,
					"La lista de la compra ID: " + id + " no existe en la base de datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<ListComLigeraProductsDetailedDTO>(listComLigeraProductsDetailedDTO, HttpStatus.OK);

	}

	@PostMapping("/listaComp")
	public ResponseEntity<?> createLista(@Valid @RequestBody ListaCom listaCom, BindingResult result,
			@RequestHeader(name = "Authorization") String token) {
		listaCom = new ListaCom();
		ListaCom newListaCom = null;
		Map<String, Object> response = new HashMap<>();

		String username = MetodosAux.obtenerUsername(token);
		listaCom.setUsuario(obtenerUsuario(username));

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(err -> {
				String campo = err.getField().contains("[]") ? err.getField().split("\\.")[1] : err.getField();
				return "El campo '" + campo + "' " + err.getDefaultMessage();
			}).collect(Collectors.toList());
			response.put(GeneralConstants.ERRORS_STRING, errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		try {
			newListaCom = this.iListaCompService.save(listaCom);
		} catch (DataAccessException e) {
			response.put(GeneralConstants.MENSAJE, GeneralConstants.FALLO_INSERT_BBDD);
			response.put(GeneralConstants.ERROR,
					e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put(GeneralConstants.MENSAJE, "¡La lista ha sido creada con éxito!");
		response.put("listaCom", newListaCom);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	/* Servicio put para modificar la lista */
	@PutMapping("/listaComp/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody ListComLigeraProductsDetailedDTO listaComp, BindingResult result,
			@PathVariable Long id) {
		ListaCom currentLista = this.iListaCompService.findById(id);
		ListaCom listaUpdated = null;
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(err -> {
				String campo = err.getField().contains("[]") ? err.getField().split("\\.")[1] : err.getField();
				return "El campo '" + campo + "' " + err.getDefaultMessage();
			}).collect(Collectors.toList());
			response.put(GeneralConstants.ERRORS_STRING, errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		if (currentLista == null) {
			response.put(GeneralConstants.MENSAJE,
					"Error: no se pudo editar, el producto ID: " + id + " no existe en la base de datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		/*
		 * Se busca el producto que se añade en la lista de la compra para insertarle el
		 * precio que se ha establecido en la misma si este no es nulo. De esta forma,
		 * el precio persiste y se hace toda la lógica de preciosHistóricos y
		 * mediaPrecio.
		 */
		if (listaComp.getProductos().size() > 0) {
			listaComp.getProductos().forEach(prod -> {
				if (iProductoService.findById(prod.getId().getProductoId()) != null
						&& prod.getProducto().getUltimoPrecio() != null) {
					Producto producto = iProductoService.findById(prod.getId().getProductoId());
					if (!StringUtils.isNullOrEmpty(prod.getProducto().getUltimoPrecio()) && (producto.getPrecio() == null  || (
							(!new BigDecimal(producto.getPrecio()).equals
                                    (new BigDecimal(prod.getProducto().getUltimoPrecio().contains(",") ?
                                            prod.getProducto().getUltimoPrecio().replace(",", ".") :
                                            prod.getProducto().getUltimoPrecio())))))) {
						producto.setPrecio(prod.getProducto().getUltimoPrecio());
						producto.setMediaPrecio(guardarPrecioHistorico(producto));
					}
					iProductoService.save(producto);
				}
			});
		}

		try {
			currentLista.setProductos(mapProductosThinToProductos(listaComp.getProductos()));
			if (!currentLista.getProductos().isEmpty()) {
				currentLista.getProductos().forEach(prod -> {
					listaComp.getProductos().forEach(prod2 -> {
						if (prod.getId().equals(prod2.getId())) {
							prod.getId().setListaCompId(id);
						}

					});
				});
			} else {
				currentLista.setProductos(null);
			}
			currentLista.getProductos().stream().forEach(prod -> {
				if (prod.getProducto().getPrecio() != null &&(prod.getProducto().getPrecio().isEmpty() || prod.getProducto().getPrecio().isBlank())) {
					prod.getProducto().setPrecio(null);
				}
			});
			listaUpdated = iListaCompService.save(currentLista);

		} catch (DataAccessException e) {
			response.put(GeneralConstants.MENSAJE, "Error al introducir la lista de la compra en la base de datos");
			response.put(GeneralConstants.ERROR,
					e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NegativeNumberException e) {
			response.put(GeneralConstants.MENSAJE, "Error al introducir la lista de la compra en la base de datos");
			response.put(GeneralConstants.ERROR, e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (ProductoNoEncontradoException e) {
			response.put(GeneralConstants.MENSAJE, e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		} catch (NumberFormatException e) {
			response.put(GeneralConstants.MENSAJE, "Has introducido un formato de número incorrecto");
			response.put(GeneralConstants.ERROR, e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		ListComLigeraProductsDetailedDTO listComLigeraProductsDetailedDTO = iListaCompService.mapEntityToDTO(listaUpdated);
		response.put(GeneralConstants.MENSAJE, "¡La lista de la compra ha sido modificada con éxito!");
		response.put("listaCompThin", listComLigeraProductsDetailedDTO);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	private Set<ListaCompProducto> mapProductosThinToProductos(Set<ListaComProductsDetailedDTO> productos) {
		Set<ListaCompProducto> listaCompProductos = new HashSet<>();
		if (productos != null && productos.size() > 0) {
			productos.forEach(prod -> {
				Producto producto = iProductoService.findById(prod.getId().getProductoId());
				if (producto == null) {
					throw new ProductoNoEncontradoException(
							"Error: no se pudo editar, el producto ID: " + prod.getId().getProductoId()
									+ " no existe en la base de datos");
				}
				ListaCompProducto listaCompProducto = new ListaCompProducto();
				listaCompProducto.setCantidad(prod.getCantidad());
				listaCompProducto.setComprado(prod.getComprado());
				listaCompProducto.setId(new ListaCompProductoId(prod.getId().getListaCompId(), prod.getId().getProductoId()));
				listaCompProducto.setProducto(producto);
				listaCompProductos.add(listaCompProducto);
			});
		}
		return listaCompProductos;
	}

	@DeleteMapping("/listaComp/{id}")
	public ResponseEntity<?> delete(@PathVariable long id) {
		Map<String, Object> response = new HashMap<>();
		try {
			ListaCom currentLista = this.iListaCompService.findById(id);
			currentLista.getProductos().forEach(e -> {
				iListaCompService.deleteProducto(e.getId().getProductoId(), currentLista.getId());
			});
			currentLista.setProductos(new HashSet<ListaCompProducto>());

			if (currentLista == null) {
				response.put(GeneralConstants.MENSAJE,
						"Error: no se pudo borrar, la lista de la compra ID: " + id + " no existe en la base de datos");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
			this.iListaCompService.delete(currentLista);
		} catch (DataAccessException e) {
			response.put(GeneralConstants.MENSAJE, "Error al borrar la lista de la compra en la base de datos");
			response.put(GeneralConstants.ERROR,
					e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put(GeneralConstants.MENSAJE, "La lista de la compra se ha borrado correctamente");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@DeleteMapping("/listaComp/{id}/comprados")
	public ResponseEntity<?> deleteProductosComprados(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		try {
			iListaCompService.deleteProductosComprados(id);
		} catch (DataAccessException e) {
			response.put(GeneralConstants.MENSAJE,
					"Error al borrar los productos comprados de la lista de la compra en la base de datos");
			response.put(GeneralConstants.ERROR,
					e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put(GeneralConstants.MENSAJE, "Los productos comprados se han eliminado correctamente de la lista");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	private Usuario obtenerUsuario(String username) {
		return iUsuarioService.findByUsername(username);
	}

	public static <T> Set<T> differenceJava8(final Set<T> setOne, final Set<T> setTwo) {
		Set<T> result = new HashSet<T>(setOne);
		result.removeIf(setTwo::contains);
		return result;
	}

	@PostMapping("/listaComp/deleteProductoLista")
	public ResponseEntity<?> deleteProductoListaEntity(@Valid @RequestBody ListaComProductoIds listaComProductoIds) {
		Map<String, Object> response = new HashMap<>();
		try {
			/*
			 * Borrado a través de una natiquery de aquellos ListaProd que hayan sido
			 * borrados desde el front y que no vengan en la nueva lista
			 */
			if (Objects.nonNull(listaComProductoIds.getIdLista())
					&& Objects.nonNull(listaComProductoIds.getIdProducto())) {
				iListaCompService.deleteProducto(listaComProductoIds.getIdProducto(), listaComProductoIds.getIdLista());
				response.put(GeneralConstants.MENSAJE, "El producto se ha eliminado de la lista correctamente");
			} else {
				response.put(GeneralConstants.MENSAJE,
						"Error al borrar el producto la lista de la compra en la base de datos");
			}
		} catch (DataAccessException e) {
			response.put(GeneralConstants.MENSAJE,
					"Error al borrar el producto la lista de la compra en la base de datos");
			response.put(GeneralConstants.ERROR,
					e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	private BigDecimal guardarPrecioHistorico(Producto producto) {
		BigDecimal media = null;
		if (Objects.nonNull(producto) && Objects.nonNull(producto.getPrecio())) {
			ProducHistoricosFecha producHistoricosFecha = new ProducHistoricosFecha(producto,
					new BigDecimal(producto.getPrecio()).setScale(2, RoundingMode.HALF_UP), Date.from(Instant.now()));
			prodPrecioHistService.save(producHistoricosFecha);
			List<ProducHistoricosFecha> prodPreciosHist = prodPrecioHistService.findByProducto(producto);
			if (prodPreciosHist != null && prodPreciosHist.size() > 0) {
				media = new BigDecimal(prodPreciosHist.stream().mapToDouble(a -> a.getPreciosHistoricos().doubleValue())
						.average().getAsDouble()).setScale(2, RoundingMode.HALF_UP);
			}
		}
		return media;
	}
}
