package com.juanboris.springboot.backend.listaCompra.controllers;

import com.juanboris.springboot.backend.listaCompra.metodosAux.GeneralConstants;
import com.juanboris.springboot.backend.listaCompra.metodosAux.MetodosAux;
import com.juanboris.springboot.backend.listaCompra.metodosAux.NegativeNumberException;
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

	/* Método GET normal */
	@GetMapping(GeneralConstants.LISTA_COMP_RUTA)
	public List<ListaComLigeraDTO> index(@RequestHeader(name = "Authorization") String token) {
		String username = MetodosAux.obtenerUsername(token);
		return iListaCompService.findAll().stream()
				.filter(listaCom -> listaCom.getUsuario().getId() == obtenerUsuario(username).getId())
				.collect(Collectors.toList());
	}

	@GetMapping("/listaComp/{id}")
	public ResponseEntity<?> listaCompById(@PathVariable Long id) {
		ListComLigeraProductsDetailedDTO listComLigeraProductsDetailedDTO = null;
		//ListaCom listaComp = null;
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
		else
		{
			Set<ListaComProductsDetailedDTO> productosOrdenados = new TreeSet<>(new Comparator<ListaComProductsDetailedDTO>() {
				@Override
				public int compare(ListaComProductsDetailedDTO o1, ListaComProductsDetailedDTO o2) {
					// Ordenamos por la propiedad booleana, primero true (1) y luego false (0)
					Boolean p1 = o1.getComprado() != null ? o1.getComprado() : false;
					Boolean p2 = o2.getComprado() != null ? o2.getComprado() : false;

					// Ordenamos por la propiedad booleana: primero true (1), luego false (0)
					int result = Boolean.compare(p1, p2);

					if (result != 0) {
						// Si son iguales en la propiedad 'comprado', comparamos por ID para garantizar unicidad
						return result;
					}

					String tipo1 = (o1.getProducto() != null && o1.getProducto().getTipo() != null) ? o1.getProducto().getTipo() : "";
					String tipo2 = (o2.getProducto() != null && o2.getProducto().getTipo() != null) ? o2.getProducto().getTipo() : "";
					result = tipo1.compareTo(tipo2);
					if (result != 0)
					{
						return result;
					}
					return o1.getId().compareTo(o2.getId());
				}
			});
			productosOrdenados.addAll(listComLigeraProductsDetailedDTO.getProductos());
			listComLigeraProductsDetailedDTO.setProductos(productosOrdenados);
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
		listaCom.setId(iListaCompService.findAll().size() > 0
				? MetodosAux.obtenerProximoId(iListaCompService.findAll(), GeneralConstants.LISTA_COMP_STRING)
				: 1L);
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
					producto.setPrecio(prod.getProducto().getUltimoPrecio());
					producto.setMediaPrecio(guardarPrecioHistorico(producto));
					iProductoService.save(producto);
				}
			});
		}

		try {
			currentLista.setProductos(mapProductosThinToProductos(listaComp.getProductos()));
			if (currentLista.getProductos().size() > 0) {
				currentLista.getProductos().stream().forEach(prod -> {
					listaComp.getProductos().stream().forEach(prod2 -> {
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
				ListaCompProducto listaCompProducto = new ListaCompProducto();
				listaCompProducto.setCantidad(prod.getCantidad());
				listaCompProducto.setComprado(prod.getComprado());
				listaCompProducto.setId(new ListaCompProductoId(prod.getId().getListaCompId(), prod.getId().getProductoId()));
				listaCompProducto.setProducto(iProductoService.findById(prod.getId().getProductoId()));
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
