/*
 * Alta de Usuario: validacion en el navegador con los estilos de Bootstrap.
 * Cada campo mal ingresado queda en rojo con su mensaje debajo, y el foco va al primero.
 * Las reglas de negocio (nick o mail repetidos, etc.) las sigue validando el servidor.
 */
(function () {
    'use strict';

    var form = document.getElementById('registro-form');
    var tipo = document.getElementById('tipo');
    var grupo = document.getElementById('grupo-instituto');
    var instituto = document.getElementById('instituto');
    var password = document.getElementById('password');
    var confirmar = document.getElementById('confirmar');
    var fechaNac = document.getElementById('fechaNac');
    var imagen = document.getElementById('imagen');

    var MAX_IMAGEN = 2 * 1024 * 1024;

    // La fecha de nacimiento tiene que ser anterior a hoy (fecha local, no UTC)
    var ayer = new Date();
    ayer.setDate(ayer.getDate() - 1);
    fechaNac.max = ayer.getFullYear() + '-' + String(ayer.getMonth() + 1).padStart(2, '0')
            + '-' + String(ayer.getDate()).padStart(2, '0');

    function actualizarInstituto() {
        var docente = tipo.value === 'DOCENTE';
        grupo.classList.toggle('d-none', !docente);
        instituto.required = docente;
    }

    function validarConfirmacion() {
        confirmar.setCustomValidity(password.value === confirmar.value ? '' : 'no coinciden');
    }

    function validarImagen() {
        var archivo = imagen.files[0];
        var ok = !archivo || (/\.(jpe?g|png)$/i.test(archivo.name) && archivo.size <= MAX_IMAGEN);
        imagen.setCustomValidity(ok ? '' : 'imagen invalida');
    }

    tipo.addEventListener('change', actualizarInstituto);
    password.addEventListener('input', validarConfirmacion);
    confirmar.addEventListener('input', validarConfirmacion);
    imagen.addEventListener('change', validarImagen);
    actualizarInstituto();

    form.addEventListener('submit', function (e) {
        validarConfirmacion();
        validarImagen();
        if (!form.checkValidity()) {
            e.preventDefault();
            var primero = form.querySelector(':invalid');
            if (primero) {
                primero.focus();
            }
        }
        form.classList.add('was-validated');
    });
})();
