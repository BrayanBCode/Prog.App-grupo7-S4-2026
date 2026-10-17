/*
 * Alta de Edicion de Curso: validacion en el navegador con los estilos de Bootstrap.
 * Las reglas de negocio (nombre repetido, superposicion con otra edicion, etc.) las valida el servidor.
 */
(function () {
    'use strict';

    var form = document.getElementById('alta-edicion-form');
    var inicio = document.getElementById('fechaInicio');
    var fin = document.getElementById('fechaFin');
    var imagen = document.getElementById('imagen');
    var MAX_IMAGEN = 2 * 1024 * 1024;

    function validarFechas() {
        var mal = inicio.value && fin.value && fin.value < inicio.value;   // yyyy-MM-dd se compara como texto
        fin.setCustomValidity(mal ? 'fin anterior al inicio' : '');
    }

    function validarImagen() {
        var archivo = imagen.files[0];
        var ok = !archivo || (/\.(jpe?g|png)$/i.test(archivo.name) && archivo.size <= MAX_IMAGEN);
        imagen.setCustomValidity(ok ? '' : 'imagen invalida');
    }

    inicio.addEventListener('change', validarFechas);
    fin.addEventListener('change', validarFechas);
    imagen.addEventListener('change', validarImagen);

    form.addEventListener('submit', function (e) {
        validarFechas();
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
