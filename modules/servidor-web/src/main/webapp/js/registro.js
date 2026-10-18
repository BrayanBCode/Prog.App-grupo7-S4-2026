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

    /*
     * Aviso Ajax (pedido del docente): al salir del campo se pregunta al servidor si el nickname o el correo ya
     * estan en uso, sin enviar el formulario. Si lo estan, el campo queda en rojo con el mensaje del servidor
     * y no se puede enviar hasta corregirlo. El alta lo vuelve a controlar en el servidor igual.
     */
    function avisarSiEstaEnUso(campo, parametro) {
        var aviso = campo.parentElement.querySelector('.invalid-feedback');
        var textoOriginal = aviso.textContent;

        function limpiar() {
            campo.setCustomValidity('');
            campo.classList.remove('is-invalid');
            aviso.textContent = textoOriginal;
        }

        campo.addEventListener('input', limpiar);
        campo.addEventListener('blur', function () {
            limpiar();
            if (!campo.value.trim() || !campo.checkValidity()) {
                return; // vacio o con formato invalido: ya lo marca la validacion de Bootstrap al enviar
            }
            var valor = campo.value.trim();
            fetch(form.dataset.disponible + '?' + new URLSearchParams([[parametro, valor]]), {credentials: 'same-origin'})
                .then(function (respuesta) {
                    return respuesta.json();
                })
                .then(function (datos) {
                    if (datos.ok && !datos.disponible && campo.value.trim() === valor) {
                        campo.setCustomValidity('en uso');
                        aviso.textContent = datos.mensaje;
                        campo.classList.add('is-invalid');
                    }
                })
                .catch(function () {
                    // sin conexion: no se avisa; el alta lo controla igual al enviar
                });
        });
    }

    avisarSiEstaEnUso(document.getElementById('nickname'), 'nickname');
    avisarSiEstaEnUso(document.getElementById('mail'), 'mail');

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
