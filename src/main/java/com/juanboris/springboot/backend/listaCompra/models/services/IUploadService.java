package com.juanboris.springboot.backend.listaCompra.models.services;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.juanboris.springboot.backend.listaCompra.models.entity.Receta;

public class IUploadService {
  public static void borrarImagen(Receta receta) {
    String nombreAnterior = receta.getImagen();
    if (nombreAnterior != null && nombreAnterior.length() > 0) {
      Path rutaArchivoAnterior = Paths.get("uploads").resolve(nombreAnterior).toAbsolutePath();
      File archivoImagenoAnterior = rutaArchivoAnterior.toFile();
      if (archivoImagenoAnterior.exists() && archivoImagenoAnterior.canRead()) {
        archivoImagenoAnterior.delete();
      }
    }
  }
}
