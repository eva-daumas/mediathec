function getCsrfToken() {
    let tokenInput = document.querySelector('input[name="_csrf"]');
    return tokenInput ? tokenInput.value : '';
}

function borrowMedia(mediaId) {
    if (!confirm('Confirmer l\'emprunt de ce média ?')) return;

    const memberId = document.getElementById('currentMemberId')?.innerText || null;

    fetch('/api/loans', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': getCsrfToken()
        },
        body: JSON.stringify({
            memberId: memberId,
            bookId: mediaId
        })
    })
        .then(response => {
            if (response.ok) {
                alert(' Emprunt effectué avec succès !');
                localStorage.setItem('loanUpdated', 'true');
                localStorage.setItem('loanAction', 'borrow');
                localStorage.setItem('loanBookId', mediaId);
                window.location.reload();
            } else {
                response.text().then(text => alert(' Erreur: ' + text));
            }
        })
        .catch(error => {
            console.error('Erreur:', error);
            alert(' Erreur lors de l\'emprunt');
        });
}

function returnMedia(mediaId) {
    if (!confirm('Confirmer le retour de ce média ?')) return;

    const csrfToken = getCsrfToken();

    fetch('/api/loans/return/' + mediaId, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': csrfToken
        }
    })
        .then(response => {
            if (response.ok) {
                alert(' Retour effectué avec succès !');
                localStorage.setItem('loanUpdated', 'true');
                localStorage.setItem('loanAction', 'return');
                localStorage.setItem('loanBookId', mediaId);
                window.location.reload();
            } else {
                response.text().then(text => alert(' Erreur: ' + text));
            }
        })
        .catch(error => {
            console.error('Erreur:', error);
            alert(' Erreur lors du retour');
        });
}