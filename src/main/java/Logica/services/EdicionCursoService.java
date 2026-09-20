package Logica.services;

import Logica.repositories.CursoRepository;
import Logica.repositories.DocenteRepository;
import Logica.repositories.EdicionCursoRepository;

public class EdicionCursoService {
    private final EdicionCursoRepository edicionCursoRepository;
    private final CursoRepository cursoRepository;
    private final DocenteRepository docenteRepository;

    public EdicionCursoService(EdicionCursoRepository edicionCursoRepository, CursoRepository cursoRepository, DocenteRepository docenteRepository) {
        this.edicionCursoRepository = edicionCursoRepository;
        this.cursoRepository = cursoRepository;
        this.docenteRepository = docenteRepository;
    }


    
}
