package com.juanboris.springboot.backend.listaCompra.models.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.juanboris.springboot.backend.listaCompra.models.dao.IProdPrecioHistDAO;
import com.juanboris.springboot.backend.listaCompra.models.entity.ProducHistoricosFecha;
import com.juanboris.springboot.backend.listaCompra.models.entity.Producto;

@Service
public class ProdPrecioHistServiceImpl implements ProdPrecioHistService {

  @Autowired
  private IProdPrecioHistDAO prodPrecioHistDAO;

  @Override
  public void save(ProducHistoricosFecha prodPrecioHist) {
    prodPrecioHistDAO.save(prodPrecioHist);
  }

  @Override
  public List<ProducHistoricosFecha> findByProducto(Producto id) {
    return prodPrecioHistDAO.findByProducto(id);
  }

}
