function deleteMember(id) {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce membre ?')) {
        fetch('/members/' + id, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': document.querySelector('input[name="_csrf"]')?.value || ''
            }
        })
            .then(response => {
                if (response.ok) {
                    window.location.reload();
                } else {
                    alert('Erreur lors de la suppression du membre');
                }
            })
            .catch(error => {
                console.error('Erreur:', error);
                alert('Erreur lors de la suppression du membre');
            });
    }
}