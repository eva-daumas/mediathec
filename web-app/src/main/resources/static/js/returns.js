// ============================================
// RETURNS.JS - Gestion des retours depuis la page des retours
// ============================================

function getCsrfToken() {
    let tokenInput = document.getElementById('csrfToken');
    if (tokenInput) {
        return tokenInput.value;
    }
    let csrf = document.querySelector('input[name="_csrf"]');
    return csrf ? csrf.value : '';
}

function returnLoan(bookId) {
    if (confirm('Confirmer le retour du livre ID: ' + bookId + ' ?')) {
        let csrfToken = getCsrfToken();

        fetch('/api/loans/return/' + bookId, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': csrfToken
            }
        })
            .then(function (response) {
                if (response.ok) {
                    alert('Retour effectué avec succès !');
                    location.reload();
                } else {
                    return response.text().then(function (text) {
                        alert('Erreur: ' + text);
                    });
                }
            })
            .catch(function (error) {
                alert('Erreur de connexion: ' + error.message);
            });
    }
}

// ============================================
// INITIALISATION - ÉCOUTEURS D'ÉVÉNEMENTS
// ============================================

document.addEventListener('DOMContentLoaded', function () {

    // Tous les boutons "Retourner"
    document.querySelectorAll('.return-btn').forEach(function (btn) {
        btn.addEventListener('click', function () {
            let id = this.getAttribute('data-id');
            returnLoan(id);
        });
    });

});