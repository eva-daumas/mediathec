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

function borrowMedia(mediaId, mediaType) {
    let memberId = document.getElementById('currentMemberId');
    if (!memberId || !memberId.textContent) {
        alert('Veuillez vous connecter pour emprunter un média.');
        return;
    }

    // Construire l'objet LoanDto
    let loanData = {
        memberId: parseInt(memberId.textContent),
        loanDate: new Date().toISOString().split('T')[0],
        status: 'BORROWED'
    };

    // Ajouter bookId OU gameId selon le type
    if (mediaType === 'book') {
        loanData.bookId = parseInt(mediaId);
        console.log('📚 Emprunt d\'un livre ID:', mediaId);
    } else if (mediaType === 'game') {
        loanData.gameId = parseInt(mediaId);
        console.log('🎮 Emprunt d\'un jeu ID:', mediaId);
    } else {
        // Fallback: essayer bookId
        loanData.bookId = parseInt(mediaId);
        console.log('📚 Emprunt par défaut (bookId):', mediaId);
    }

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
                alert('✅ Emprunt effectué avec succès !');
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

    // Tous les boutons "Emprunter"
    document.querySelectorAll('.borrow-btn').forEach(function (btn) {
        btn.addEventListener('click', function () {
            let mediaId = this.getAttribute('data-id');
            let mediaType = this.getAttribute('data-type'); // 'book' ou 'game'

            // Vérifier que mediaType est défini
            if (!mediaType) {
                // Fallback: si le type n'est pas défini, regarder la catégorie dans un attribut
                console.warn('⚠️ data-type manquant pour le média ID:', mediaId);
                mediaType = 'book'; // Par défaut
            }

            borrowMedia(mediaId, mediaType);
        });
    });

});