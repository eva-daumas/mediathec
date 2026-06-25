function returnLoan(id) {
    if (confirm('Confirmer le retour de cet emprunt ?')) {
        fetch('/api/loans/return/' + id, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': document.querySelector('input[name="_csrf"]')?.value || ''
            }
        })
            .then(response => {
                if (response.ok) {
                    window.location.reload();
                } else {
                    alert('Erreur lors du retour');
                }
            })
            .catch(error => {
                console.error('Erreur:', error);
                alert('Erreur lors du retour');
            });
    }
}