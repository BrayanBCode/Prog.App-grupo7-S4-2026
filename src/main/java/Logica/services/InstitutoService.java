package Logica.services;

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

}
