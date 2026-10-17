/*
 * Alta de Curso: validacion en el navegador con los estilos de Bootstrap (igual que el registro).
 * Las reglas de negocio (nombre repetido, previas, etc.) las sigue validando el servidor.
 */
(function () {
    'use strict';

    var form = document.getElementById('alta-curso-form');
    var imagen = document.getElementById('imagen');
    var MAX_IMAGEN = 2 * 1024 * 1024;

    function validarImagen() {
        var archivo = imagen.files[0];
        var ok = !archivo || (/\.(jpe?g|png)$/i.test(archivo.name) && archivo.size <= MAX_IMAGEN);
        imagen.setCustomValidity(ok ? '' : 'imagen invalida');
    }

    imagen.addEventListener('change', validarImagen);

    form.addEventListener('submit', function (e) {
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
