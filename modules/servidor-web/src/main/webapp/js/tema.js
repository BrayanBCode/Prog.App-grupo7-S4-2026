/*
 * Modo oscuro (submenu del avatar). El tema se guarda en localStorage y se aplica en el <head>
 * (inicio-pagina.jspf) antes de pintar; aca solo se alterna y se actualiza el texto de la opcion.
 */
(function () {
    var raiz = document.documentElement;

    function oscuro() {
        return raiz.getAttribute('data-bs-theme') === 'dark';
    }

    function actualizar() {
        document.querySelectorAll('[data-tema-alternar]').forEach(function (b) {
            b.textContent = oscuro() ? 'Modo claro' : 'Modo oscuro';
        });
    }

    document.addEventListener('click', function (ev) {
        if (!ev.target.closest('[data-tema-alternar]')) {
            return;
        }
        var tema = oscuro() ? 'light' : 'dark';
        raiz.setAttribute('data-bs-theme', tema);
        try { localStorage.setItem('tema', tema); } catch (e) { /* sin almacenamiento: dura hasta recargar */ }
        actualizar();
    });

    actualizar();
})();
