package Logica.services;

import Logica.entities.cursos.Instituto;
import Logica.repositories.InstitutoRepository;

import java.util.List;

public class InstitutoService {
    private final InstitutoRepository institutoRepository;

    public InstitutoService(InstitutoRepository institutoRepository) {
        this.institutoRepository = institutoRepository;
    }

    public List<String> obtenerNombres() {
        return institutoRepository.obtenerNombres();
    }

    /**
     * Caso de uso "Alta de Instituto". El nombre es la @Id: no puede
     * estar vacio ni repetirse.
     */
    public void registrar(String nombre) throws Exception {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Exception("El nombre del instituto no puede estar vacío.");
        }

        Instituto existente = institutoRepository.buscarPorNombre(nombre.trim());
        if (existente != null) {
            throw new Exception("Ya existe un instituto registrado con el nombre: " + nombre);
        }

        institutoRepository.guardar(nombre.trim());
    }
}
