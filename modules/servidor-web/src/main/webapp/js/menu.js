/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

/*
 * Menu desplegable del cabezal (flecha arriba a la izquierda).
 *
 * El HTML ya trae el menu con el atributo "hidden": este script solo lo muestra
 * y lo oculta, y mantiene aria-expanded para lectores de pantalla.
 * Se cierra con: click en la flecha, click fuera del menu, o tecla Escape.
 */
(function () {
    'use strict';

    var boton = document.getElementById('menu-boton');
    var menu = document.getElementById('menu-casos');

    if (!boton || !menu) {
        return; // pagina sin cabezal: no hay nada que hacer
    }

    function mostrar(abierto) {
        menu.hidden = !abierto;
        boton.setAttribute('aria-expanded', String(abierto));
        boton.setAttribute('aria-label', abierto ? 'Cerrar menú' : 'Abrir menú');
    }

    boton.addEventListener('click', function () {
        mostrar(menu.hidden); // si estaba oculto, lo abre; si no, lo cierra
    });

    document.addEventListener('click', function (evento) {
        var clickFuera = !menu.contains(evento.target) && !boton.contains(evento.target);
        if (!menu.hidden && clickFuera) {
            mostrar(false);
        }
    });

    document.addEventListener('keydown', function (evento) {
        if (evento.key === 'Escape' && !menu.hidden) {
            mostrar(false);
            boton.focus(); // devuelve el foco a la flecha
        }
    });
})();