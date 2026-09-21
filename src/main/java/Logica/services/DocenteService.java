package Logica.services;

import Logica.entities.usuarios.Docente;
import Logica.repositories.DocenteRepository;

import java.util.ArrayList;
import java.util.List;

public class DocenteService {

    private final DocenteRepository docenteRepository;

    public DocenteService(DocenteRepository docenteRepository) {
        this.docenteRepository = docenteRepository;
    }

    public List<String[]> listarDocentesTabla() {
        return aTabla(docenteRepository.listarTodos());
    }

    public List<String[]> listarPorInstituto(String nombreInstituto) {
        return aTabla(docenteRepository.listarPorInstituto(nombreInstituto));
    }

    /**
     * Caso de uso "Consulta de Docente": institutos, cursos, ediciones y
     * programas vinculados a ese docente.
     */
    public List<String> obtenerData(String nickname) {
        List<String> institutos = docenteRepository.nombresInstitutos(nickname);
        List<String> cursos = docenteRepository.nombresCursos(nickname);
        List<String> ediciones = docenteRepository.nombresEdiciones(nickname);
        List<String> programas = docenteRepository.nombresProgramas(nickname);

        // TODO: ESTE FORMATEO VA EN LA CAPA DE PRESENTACION
        List<String> resultado = new ArrayList<>();

        resultado.add("--- INSTITUTOS ---");
        if (institutos.isEmpty()) resultado.add("(Sin institutos vinculados)");
        else institutos.forEach(i -> resultado.add("- " + i));

        resultado.add("\n--- CURSOS ---");
        if (cursos.isEmpty()) resultado.add("(Sin cursos registrados)");
        else cursos.forEach(c -> resultado.add("- " + c));

        resultado.add("\n--- EDICIONES DE CURSOS ---");
        if (ediciones.isEmpty()) resultado.add("(Sin ediciones asignadas)");
        else ediciones.forEach(e -> resultado.add("- " + e));

        resultado.add("\n--- PROGRAMAS DE FORMACIÓN ---");
        if (programas.isEmpty()) resultado.add("(Sin programas vinculados)");
        else programas.forEach(p -> resultado.add("- " + p));

        return resultado;
    }

    /**
     * {0}=nickname (identificador), {1}=texto a mostrar en la lista.
     */
    private List<String[]> aTabla(List<Docente> lista) {
        List<String[]> resultado = new ArrayList<>();
        for (Docente d : lista) {
            resultado.add(new String[]{
                    d.getNickname(),
                    d.getNombreU() + " " + d.getApellido() + " (" + d.getNickname() + ")"
            });
        }
        return resultado;
    }
}
