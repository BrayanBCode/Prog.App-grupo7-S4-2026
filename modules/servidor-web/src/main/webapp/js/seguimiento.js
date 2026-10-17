/*
 * Seguir / Dejar de seguir a un usuario con Ajax (pedido del docente): el boton del perfil manda un POST con
 * fetch y la pagina NO se recarga. El servidor contesta JSON:
 *   {"ok": true, "siguiendo": true, "seguidores": 5}  o  {"ok": false, "error": "mensaje para el usuario"}
 * Con la respuesta se cambia el boton (Seguir <-> Dejar de seguir), se actualizan los contadores de
 * seguidores y se agrega o se saca al usuario actual de la lista. Mientras espera, el boton queda deshabilitado.
 */
(function () {
    'use strict';

    var boton = document.getElementById('btn-seguir');
    var mensaje = document.getElementById('seguimiento-mensaje');
    if (!boton) {
        return; // sin sesion o es el propio perfil
    }
    var lista = document.getElementById('lista-seguidores');
    var sinSeguidores = document.getElementById('sin-seguidores');

    function pintarBoton(siguiendo) {
        boton.dataset.siguiendo = String(siguiendo);
        boton.textContent = siguiendo ? 'Dejar de seguir' : 'Seguir';
        boton.className = 'btn btn-sm ' + (siguiendo ? 'btn-outline-secondary' : 'btn-primary');
    }

    function actualizarSeguidores(total, siguiendo) {
        document.querySelectorAll('[data-contador-seguidores]').forEach(function (c) {
            c.textContent = total;
        });
        var yo = boton.dataset.yo;
        var item = lista.querySelector('[data-nick="' + CSS.escape(yo) + '"]');
        if (siguiendo && !item) {
            item = document.createElement('a');
            item.className = 'list-group-item list-group-item-action';
            item.href = boton.dataset.url.replace(/\/seguimiento$/, '') + '?nick=' + encodeURIComponent(yo);
            item.dataset.nick = yo;
            item.textContent = yo;
            lista.appendChild(item);
        } else if (!siguiendo && item) {
            item.remove();
        }
        sinSeguidores.classList.toggle('d-none', total > 0);
    }

    boton.addEventListener('click', function () {
        var siguiendo = boton.dataset.siguiendo === 'true';
        boton.disabled = true;
        mensaje.textContent = '';

        fetch(boton.dataset.url, {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'},
            body: new URLSearchParams({nick: boton.dataset.nick, accion: siguiendo ? 'dejar' : 'seguir'}),
            credentials: 'same-origin'
        })
            .then(function (respuesta) {
                return respuesta.json();
            })
            .then(function (datos) {
                if (datos.ok) {
                    pintarBoton(datos.siguiendo);
                    actualizarSeguidores(datos.seguidores, datos.siguiendo);
                } else {
                    mensaje.className = 'small mt-1 text-danger';
                    mensaje.textContent = datos.error;
                }
            })
            .catch(function () {
                mensaje.className = 'small mt-1 text-danger';
                mensaje.textContent = 'No se pudo conectar con el servidor. Intentá nuevamente.';
            })
            .finally(function () {
                boton.disabled = false;
            });
    });
})();
