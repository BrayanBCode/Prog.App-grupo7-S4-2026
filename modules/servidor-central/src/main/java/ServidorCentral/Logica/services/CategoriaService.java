package ServidorCentral.Logica.services;

import ServidorCentral.Logica.repositories.CategoriaRepository;

import java.util.List;

public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    /**
     * Caso de uso "Alta de Categoria". El nombre es unico: si ya existe se avisa
     * para que el administrador pueda corregirlo o cancelar (letra 6.1).
     */
    public void registrar(String nombre) throws Exception {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Exception("El nombre de la categoría no puede estar vacío.");
        }
        String limpio = nombre.trim();

        if (categoriaRepository.buscarPorNombre(limpio) != null) {
            throw new Exception("Ya existe una categoría registrada con el nombre: " + limpio);
        }
        categoriaRepository.guardar(limpio);
    }

    public List<String> obtenerNombres() {
        return categoriaRepository.obtenerNombres();
    }
}
