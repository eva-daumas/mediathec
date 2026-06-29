// ============================================
// HOME.JS - Gestion des emprunts depuis la page d'accueil
// ============================================

function getCsrfToken() {
    let tokenInput = document.getElementById('csrfToken');
    if (tokenInput) {
        return tokenInput.value;
    }
    let csrf = document.querySelector('input[name="_csrf"]');
    return csrf ? csrf.value : '';
}

function borrowMedia(bookId) {
    let memberId = document.getElementById('currentMemberId');
    if (!memberId || !memberId.textContent) {
        alert('Veuillez vous connecter pour emprunter un média.');
        return;
    }

    let loanData = {
        bookId: bookId,
        memberId: parseInt(memberId.textContent),
        loanDate: new Date().toISOString().split('T')[0],
        status: 'BORROWED'
    };

    let csrfToken = getCsrfToken();

    fetch('/api/loans', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': csrfToken
        },
        body: JSON.stringify(loanData)
    })
        .then(function (response) {
            if (response.ok) {
                alert('Emprunt effectué avec succès !');
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

// ============================================
// INITIALISATION - ÉCOUTEURS D'ÉVÉNEMENTS
// ============================================

document.addEventListener('DOMContentLoaded', function () {

    // Tous les boutons "Emprunter"
    document.querySelectorAll('.borrow-btn').forEach(function (btn) {
        btn.addEventListener('click', function () {
            let id = this.getAttribute('data-id');
            borrowMedia(id);
        });
    });

});