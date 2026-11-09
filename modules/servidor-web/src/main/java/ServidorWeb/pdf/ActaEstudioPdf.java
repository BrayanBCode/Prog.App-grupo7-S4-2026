package ServidorWeb.pdf;

import ServidorWeb.ws.central.DTInscripcionEstudiante;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

/**
 * Acta de estudio en PDF (letra 7.4, caso de uso "Tramitar Acta de Estudio"). Variante para grupos de 3: sin
 * programa de formacion, lleva todos los cursos del estudiante con nota en una edicion cerrada, con la nota y la
 * fecha del resultado (cierre de la edicion). Se genera de nuevo en cada pedido; no se guarda en ningun lado.
 */
public final class ActaEstudioPdf {

    private static final DateTimeFormatter DIA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm");
    private static final DeviceRgb GRIS = new DeviceRgb(0x5c, 0x63, 0x6a);
    private static final DeviceRgb FONDO_CABEZAL = new DeviceRgb(0xe2, 0xe6, 0xea);
    private static final DeviceRgb LINEA = new DeviceRgb(0xce, 0xd4, 0xda);

    /** Un renglon del acta. */
    public record Fila(String curso, String edicion, int nota, LocalDate fecha) {
    }

    private ActaEstudioPdf() {
    }

    /**
     * Los renglones del acta a partir de las inscripciones del estudiante: solo las que tienen nota (el central
     * la manda solo con la edicion cerrada), de la mas vieja a la mas nueva y, el mismo dia, por curso.
     */
    public static List<Fila> filas(List<DTInscripcionEstudiante> inscripciones) {
        return inscripciones.stream()
                .filter(i -> i.getNota() != null && i.getFechaCierre() != null)
                .map(i -> new Fila(i.getCurso(), i.getEdicion(), i.getNota(), LocalDate.parse(i.getFechaCierre())))
                .sorted(Comparator.comparing(Fila::fecha).thenComparing(Fila::curso, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    /** El PDF del acta, listo para mandar al navegador. */
    public static byte[] generar(String nombreCompleto, String nickname, String mail, List<Fila> filas,
                                 LocalDateTime emitida) {
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        try (PdfDocument pdf = new PdfDocument(new PdfWriter(salida));
             Document doc = new Document(pdf)) {
            PdfDocumentInfo info = pdf.getDocumentInfo();
            info.setTitle("Acta de estudio - " + nombreCompleto);
            info.setAuthor("edExt");

            PdfFont normal = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            PdfFont negrita = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            doc.setFont(normal).setFontSize(10.5f);

            doc.add(new Paragraph("edExt").setFont(negrita).setFontSize(12).setFontColor(GRIS).setMarginBottom(0));
            doc.add(new Paragraph("Acta de estudio").setFont(negrita).setFontSize(20).setMarginTop(0));

            doc.add(dato(negrita, "Estudiante: ", nombreCompleto + " (" + nickname + ")"));
            doc.add(dato(negrita, "Correo: ", mail));
            doc.add(dato(negrita, "Emitida el: ", emitida.format(DIA)).setMarginBottom(14));

            if (filas.isEmpty()) {
                doc.add(new Paragraph("Todavía no tenés cursos con resultado: el acta lista las ediciones cerradas "
                        + "en las que tenés nota.").setFontColor(GRIS));
            } else {
                Table tabla = new Table(UnitValue.createPercentArray(new float[]{33, 37, 8, 22})).useAllAvailableWidth();
                for (String titulo : new String[]{"Curso", "Edición", "Nota", "Fecha de resultado"}) {
                    tabla.addHeaderCell(celda(titulo).setFont(negrita).setBackgroundColor(FONDO_CABEZAL)
                            .setTextAlignment("Nota".equals(titulo) ? TextAlignment.CENTER : TextAlignment.LEFT));
                }
                for (Fila f : filas) {
                    tabla.addCell(celda(f.curso()));
                    tabla.addCell(celda(f.edicion()));
                    tabla.addCell(celda(String.valueOf(f.nota())).setTextAlignment(TextAlignment.CENTER));
                    tabla.addCell(celda(f.fecha().format(DIA)));
                }
                doc.add(tabla);
                doc.add(new Paragraph(filas.size() == 1 ? "1 curso con resultado. Notas en la escala de 1 a 12."
                        : filas.size() + " cursos con resultado. Notas en la escala de 1 a 12.")
                        .setFontSize(9).setFontColor(GRIS).setMarginTop(6));
            }

            doc.add(new Paragraph("Documento generado por edExt el " + emitida.format(DIA) + " a las "
                    + emitida.format(HORA) + ".").setFontSize(9).setFontColor(GRIS).setMarginTop(24));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return salida.toByteArray();
    }

    private static Paragraph dato(PdfFont negrita, String etiqueta, String valor) {
        return new Paragraph().add(new com.itextpdf.layout.element.Text(etiqueta).setFont(negrita))
                .add(valor == null ? "" : valor).setMargin(0).setMarginBottom(2);
    }

    private static Cell celda(String texto) {
        return new Cell().add(new Paragraph(texto == null ? "" : texto))
                .setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(LINEA, 0.75f))
                .setPadding(5).setFontColor(ColorConstants.BLACK);
    }
}
