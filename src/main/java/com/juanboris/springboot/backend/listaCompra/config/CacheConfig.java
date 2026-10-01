package com.juanboris.springboot.backend.listaCompra.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.juanboris.springboot.backend.listaCompra.metodosAux.GeneralConstants;

/**
 * Caché en memoria sin dependencias externas (nada de Redis/Ehcache): el servidor tiene pocos
 * recursos y el volumen de datos es pequeño. No tiene TTL propio, así que toda escritura sobre
 * productos o listas debe invalidar las 3 regiones vía @CacheEvict (ver ProductoRestController y
 * ListaCompController) para no servir datos obsoletos.
 */
@Configuration
@EnableCaching
public class CacheConfig {

  @Bean
  public CacheManager cacheManager() {
    return new ConcurrentMapCacheManager(GeneralConstants.CACHE_PRODUCTOS_PAGE,
        GeneralConstants.CACHE_LISTA_COMP_INDEX, GeneralConstants.CACHE_LISTA_COMP_BY_ID);
  }
}

