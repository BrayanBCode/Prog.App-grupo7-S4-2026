/*
 * Inscripcion a Edicion de Curso con Ajax (pedido del docente): el boton "Inscribirme" manda un POST con
 * fetch y la pagina NO se recarga. El servidor contesta JSON:
 *   {"ok": true, "estado": "Inscripto"}  o  {"ok": false, "error": "mensaje para el usuario"}
 * Mientras espera, el boton queda deshabilitado (evita el doble clic).
 */
(function () {
    'use strict';

    var boton = document.getElementById('btn-inscribirme');
    var mensaje = document.getElementById('inscripcion-mensaje');
    if (!boton) {
        return; // no hay boton: no es estudiante, ya esta inscripto o la edicion no es la vigente
    }

    function mostrarError(texto) {
        mensaje.className = 'small mt-1 text-danger';
        mensaje.textContent = texto;
    }

    boton.addEventListener('click', function () {
        boton.disabled = true;
        boton.textContent = 'Inscribiendo…';
        mensaje.textContent = '';

        fetch(boton.dataset.url, {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'},
            body: new URLSearchParams({edicion: boton.dataset.edicion}),
            credentials: 'same-origin'
        })
            .then(function (respuesta) {
                return respuesta.json();
            })
            .then(function (datos) {
                if (datos.ok) {
                    // Queda como en la carga de la pagina de un estudiante ya inscripto
                    var estado = document.createElement('span');
                    estado.className = 'badge text-bg-warning';
                    estado.textContent = datos.estado;
                    boton.replaceWith(estado);
                    mensaje.className = 'small mt-1 text-success';
                    mensaje.textContent = 'Te inscribiste a la edición.';
                } else {
                    boton.disabled = false;
                    boton.textContent = 'Inscribirme';
                    mostrarError(datos.error);
                }
            })
            .catch(function () {
                boton.disabled = false;
                boton.textContent = 'Inscribirme';
                mostrarError('No se pudo conectar con el servidor. Intentá nuevamente.');
            });
    });
})();
