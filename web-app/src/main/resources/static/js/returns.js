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

function returnLoan(mediaId) {
    // ✅ Vérifier que mediaId n'est pas "null" ou null
    if (!mediaId || mediaId === 'null' || mediaId === 'undefined' || mediaId === '') {
        alert('❌ Erreur: ID du média non valide');
        return;
    }

    const id = Number(mediaId);
    if (isNaN(id) || id <= 0) {
        alert('❌ Erreur: ID du média invalide');
        return;
    }

    if (!confirm('Confirmer le retour du média ID: ' + id + ' ?')) {
        return;
    }

    let csrfToken = getCsrfToken();

    fetch('/api/loans/return/' + id, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': csrfToken
        }
    })
        .then(function (response) {
            if (response.ok) {
                alert('✅ Retour effectué avec succès !');
                location.reload();
            } else {
                return response.text().then(function (text) {
                    alert('❌ Erreur: ' + text);
                });
            }
        })
        .catch(function (error) {
            alert('❌ Erreur de connexion: ' + error.message);
        });
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