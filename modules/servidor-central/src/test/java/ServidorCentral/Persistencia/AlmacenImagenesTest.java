package ServidorCentral.Persistencia;

import ServidorCentral.Logica.datatypes.DTImagen;
import ServidorCentral.Logica.excepciones.ReglaNegocioException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas SIN base de datos: guardado de imagenes en una carpeta temporal.
 */
class AlmacenImagenesTest {

    private static final byte[] BYTES = {1, 2, 3, 4};

    @TempDir
    Path carpeta;

    /** Otra carpeta, para hacer de imagenes/ del repo o del WAR al sembrar. */
    @TempDir
    Path origen;

    @BeforeEach
    void apuntarACarpetaTemporal() {
        AlmacenImagenes.setCarpetaBase(carpeta);
    }

    @AfterEach
    void restaurarCarpeta() {
        AlmacenImagenes.setCarpetaBase(null);
    }

    @Test
    void sembrarCopiaLasQueFaltanSinPisarLasQueYaEstan() throws IOException {
        Files.createDirectories(origen.resolve("cursos"));
        Files.write(origen.resolve("cursos/A.jpg"), BYTES);
        Files.write(origen.resolve("cursos/B.png"), BYTES);
        Files.createDirectories(carpeta.resolve("cursos"));
        Files.write(carpeta.resolve("cursos/B.png"), new byte[]{9});   // una foto que ya se cambio

        assertEquals(1, AlmacenImagenes.sembrar(origen));

        assertArrayEquals(BYTES, Files.readAllBytes(carpeta.resolve("cursos/A.jpg")));
        assertArrayEquals(new byte[]{9}, Files.readAllBytes(carpeta.resolve("cursos/B.png")));
        assertEquals(0, AlmacenImagenes.sembrar(origen), "la segunda vez no copia nada");
    }

    @Test
    void sembrarSinOrigenOMismaCarpetaNoHaceNada() {
        assertEquals(0, AlmacenImagenes.sembrar(null));
        assertEquals(0, AlmacenImagenes.sembrar(origen.resolve("no-existe")));
        assertEquals(0, AlmacenImagenes.sembrar(carpeta));
    }

    @Test
    void resolverDevuelveElArchivoGuardado() {
        String ruta = AlmacenImagenes.guardar(new DTImagen(BYTES, "png"), AlmacenImagenes.USUARIOS, "nick");

        assertEquals(carpeta.resolve(ruta).normalize(), AlmacenImagenes.resolver(ruta));
        assertEquals(carpeta.resolve(ruta).normalize(), AlmacenImagenes.resolver("/" + ruta), "como viene en la URL");
    }

    @Test
    void resolverNoSaleDeLaCarpeta() throws IOException {
        Files.write(origen.resolve("secreto.png"), BYTES);
        String afuera = "../" + carpeta.relativize(origen.resolve("secreto.png")).toString().replace('\\', '/');

        assertNull(AlmacenImagenes.resolver(afuera));
        assertNull(AlmacenImagenes.resolver("usuarios/../../secreto.png"));
        assertNull(AlmacenImagenes.resolver(origen.resolve("secreto.png").toString()), "ruta absoluta");
    }

    @Test
    void resolverSinRutaOInexistenteDevuelveNull() {
        assertNull(AlmacenImagenes.resolver(null));
        assertNull(AlmacenImagenes.resolver(" "));
        assertNull(AlmacenImagenes.resolver("usuarios/nadie.png"));
        assertNull(AlmacenImagenes.resolver("usuarios"), "una carpeta no es una imagen");
    }

    @Test
    void guardaJpgYDevuelveRutaRelativa() throws IOException {
        String ruta = AlmacenImagenes.guardar(new DTImagen(BYTES, "jpg"), AlmacenImagenes.USUARIOS, "benkenobi");

        assertEquals("usuarios/benkenobi.jpg", ruta);
        assertArrayEquals(BYTES, Files.readAllBytes(carpeta.resolve(ruta)));
    }

    @Test
    void guardaPngTomandoLaExtensionDelNombreOriginal() {
        String ruta = AlmacenImagenes.guardar(DTImagen.deArchivo(BYTES, "Foto.PNG"), AlmacenImagenes.CURSOS, "Robotica");

        assertEquals("cursos/Robotica.png", ruta);
        assertTrue(Files.isRegularFile(carpeta.resolve(ruta)));
    }

    @Test
    void limpiaElNombreDelArchivo() {
        String ruta = AlmacenImagenes.guardar(new DTImagen(BYTES, ".jpeg"), AlmacenImagenes.CURSOS,
                "Taller de robótica educativa.");

        assertEquals("cursos/Taller_de_robotica_educativa.jpeg", ruta);
    }

    @Test
    void nombreSinCaracteresValidosUsaImagen() {
        assertEquals("imagen", AlmacenImagenes.nombreArchivo("../??"));
    }

    @Test
    void sinImagenDevuelveNull() {
        assertNull(AlmacenImagenes.guardar((DTImagen) null, AlmacenImagenes.USUARIOS, "x"));
        assertNull(AlmacenImagenes.guardar(new DTImagen(new byte[0], "jpg"), AlmacenImagenes.USUARIOS, "x"));
        assertNull(AlmacenImagenes.guardar((String) null, AlmacenImagenes.USUARIOS, "x"));
        assertNull(AlmacenImagenes.guardar(carpeta.resolve("no-existe.jpg").toString(), AlmacenImagenes.USUARIOS, "x"));
    }

    @Test
    void rechazaFormatosQueNoSonJpgNiPng() {
        ReglaNegocioException e = assertThrows(ReglaNegocioException.class,
                () -> AlmacenImagenes.guardar(DTImagen.deArchivo(BYTES, "animacion.gif"), AlmacenImagenes.USUARIOS, "x"));
        assertEquals("Formato de imagen no soportado. Use JPG o PNG.", e.getMessage());
        assertThrows(ReglaNegocioException.class,
                () -> AlmacenImagenes.guardar(DTImagen.deArchivo(BYTES, "sin-extension"), AlmacenImagenes.USUARIOS, "x"));
    }

    @Test
    void rechazaImagenesDeMasDe2Mb() {
        byte[] grande = new byte[AlmacenImagenes.TAMANO_MAXIMO + 1];
        ReglaNegocioException e = assertThrows(ReglaNegocioException.class,
                () -> AlmacenImagenes.guardar(new DTImagen(grande, "png"), AlmacenImagenes.USUARIOS, "x"));
        assertEquals("La imagen no puede superar los 2 MB.", e.getMessage());
    }

    @Test
    void versionConRutaCopiaElArchivoLocal() throws IOException {
        Path origen = Files.write(carpeta.resolve("elegida.png"), BYTES);

        String ruta = AlmacenImagenes.guardar(origen.toString(), AlmacenImagenes.EDICIONES, "Edicion 2026");

        assertEquals("ediciones/Edicion_2026.png", ruta);
        assertArrayEquals(BYTES, Files.readAllBytes(carpeta.resolve(ruta)));
    }

    @Test
    void rutaAbsolutaDeUnaImagenGuardada() {
        String ruta = AlmacenImagenes.guardar(new DTImagen(BYTES, "jpg"), AlmacenImagenes.USUARIOS, "nick");

        assertEquals(carpeta.resolve(ruta).normalize().toString(), AlmacenImagenes.rutaAbsoluta(ruta));
        assertNull(AlmacenImagenes.rutaAbsoluta("usuarios/otro.jpg"));
        assertNull(AlmacenImagenes.rutaAbsoluta(null));
    }
}
