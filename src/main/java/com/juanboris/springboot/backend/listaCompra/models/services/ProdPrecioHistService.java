package com.juanboris.springboot.backend.listaCompra.models.services;

import java.util.List;

import com.juanboris.springboot.backend.listaCompra.models.entity.ProducHistoricosFecha;
import com.juanboris.springboot.backend.listaCompra.models.entity.Producto;

public interface ProdPrecioHistService {
	public void save(ProducHistoricosFecha prodPrecioHist);
	
	public List<ProducHistoricosFecha> findByProducto (Producto id);
}
