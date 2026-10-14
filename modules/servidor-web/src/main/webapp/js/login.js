/*
 * Inicio de Sesion: si el servidor redirige con ?error=true se muestra la advertencia
 * para que el visitante reingrese sus datos (o cancele). Con ?registrado=true se avisa
 * que el alta de usuario termino bien.
 */
(function () {
    'use strict';

    var params = new URLSearchParams(window.location.search);
    var error = document.getElementById('error-message');

    if (params.get('error') === 'true' && error) {
        error.classList.remove('hidden');
        document.getElementById('usuario').focus();
    }

    if (params.get('registrado') === 'true') {
        var ok = document.createElement('div');
        ok.className = 'alert-success';
        ok.setAttribute('role', 'status');
        ok.textContent = 'Te registraste correctamente. Ya podés iniciar sesión.';
        var form = document.getElementById('login-form');
        form.parentNode.insertBefore(ok, form);
    }
})();
