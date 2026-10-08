const tipo = document.getElementById('tipo');
const grupo = document.getElementById('grupo-instituto');
const instituto = document.getElementById('instituto');

function actualizarInstituto() {
    const docente = tipo.value === 'DOCENTE';
    grupo.classList.toggle('hidden', !docente);
    instituto.required = docente;
}
tipo.addEventListener('change', actualizarInstituto);
actualizarInstituto();

document.getElementById('registro-form').addEventListener('submit', (e) => {
    const msg = document.getElementById('error-cliente');
    if (document.getElementById('password').value !== document.getElementById('confirmar').value) {
        e.preventDefault();
        msg.textContent = 'La contraseña y su confirmación no coinciden.';
        msg.classList.remove('hidden');
    }
});