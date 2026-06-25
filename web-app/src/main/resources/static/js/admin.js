function getCsrfToken() {
    let tokenInput = document.getElementById('csrfToken');
    if (tokenInput) {
        return tokenInput.value;
    }
    let csrf = document.querySelector('input[name="_csrf"]');
    return csrf ? csrf.value : '';
}

function openEditModal(button) {
    let id = button.getAttribute('data-id');
    let username = button.getAttribute('data-username');
    let email = button.getAttribute('data-email');
    let role = button.getAttribute('data-role');

    document.getElementById('editId').value = id;
    document.getElementById('editUsername').value = username || '';
    document.getElementById('editEmail').value = email || '';
    document.getElementById('editRole').value = role || 'USER';
    document.getElementById('editPassword').value = '';

    document.getElementById('editModal').classList.add('active');
}

function closeEditModal() {
    document.getElementById('editModal').classList.remove('active');
}

document.getElementById('editForm').addEventListener('submit', function (e) {
    e.preventDefault();
    let id = document.getElementById('editId').value;
    let username = document.getElementById('editUsername').value;
    let email = document.getElementById('editEmail').value;
    let role = document.getElementById('editRole').value;
    let password = document.getElementById('editPassword').value;

    let data = {
        username: username,
        email: email,
        role: role
    };
    if (password) {
        data.password = password;
    }

    let csrfToken = getCsrfToken();

    fetch('/admin/members/update/' + id, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': csrfToken
        },
        body: JSON.stringify(data)
    })
        .then(function (response) {
            if (response.ok) {
                alert(' Membre modifié avec succès !');
                closeEditModal();
                location.reload();
            } else {
                alert(' Erreur lors de la modification (code: ' + response.status + ')');
            }
        })
        .catch(function (error) {
            alert(' Erreur de connexion: ' + error.message);
        });
});

document.getElementById('editModal').addEventListener('click', function (e) {
    if (e.target === this) closeEditModal();
});

function openDeleteModal(button) {
    let id = button.getAttribute('data-id');
    let username = button.getAttribute('data-username');

    document.getElementById('deleteId').value = id;
    document.getElementById('deleteName').textContent = username || 'ID ' + id;

    document.getElementById('deleteModal').classList.add('active');
}

function closeDeleteModal() {
    document.getElementById('deleteModal').classList.remove('active');
}

function confirmDelete() {
    let id = document.getElementById('deleteId').value;
    let csrfToken = getCsrfToken();

    fetch('/admin/members/delete/' + id, {
        method: 'DELETE',
        headers: {
            'X-CSRF-TOKEN': csrfToken
        }
    })
        .then(function (response) {
            if (response.ok) {
                alert(' Membre supprimé avec succès !');
                closeDeleteModal();
                location.reload();
            } else {
                alert(' Erreur lors de la suppression (code: ' + response.status + ')');
            }
        })
        .catch(function (error) {
            alert(' Erreur de connexion: ' + error.message);
        });
}

document.getElementById('deleteModal').addEventListener('click', function (e) {
    if (e.target === this) closeDeleteModal();
});

function returnLoan(id) {
    if (confirm(' Confirmer le retour de l\'emprunt ID: ' + id + ' ?')) {
        let csrfToken = getCsrfToken();

        fetch('/admin/loans/return/' + id, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': csrfToken
            }
        })
            .then(function (response) {
                if (response.ok) {
                    alert(' Emprunt retourné avec succès !');
                    location.reload();
                } else {
                    alert(' Erreur lors du retour (code: ' + response.status + ')');
                }
            })
            .catch(function (error) {
                alert(' Erreur de connexion: ' + error.message);
            });
    }
}

function deleteLoan(id) {
    if (confirm(' Supprimer l\'emprunt ID: ' + id + ' ?')) {
        let csrfToken = getCsrfToken();

        fetch('/admin/loans/delete/' + id, {
            method: 'DELETE',
            headers: {
                'X-CSRF-TOKEN': csrfToken
            }
        })
            .then(function (response) {
                if (response.ok) {
                    alert(' Emprunt supprimé avec succès !');
                    location.reload();
                } else {
                    alert(' Erreur lors de la suppression (code: ' + response.status + ')');
                }
            })
            .catch(function (error) {
                alert(' Erreur de connexion: ' + error.message);
            });
    }
}

function editMedia(id) {
    window.location.href = '/admin/medias/edit/' + id;
}

function deleteMedia(id) {
    if (confirm(' Supprimer le média ID: ' + id + ' ?')) {
        let csrfToken = getCsrfToken();

        fetch('/admin/medias/delete/' + id, {
            method: 'DELETE',
            headers: {
                'X-CSRF-TOKEN': csrfToken
            }
        })
            .then(function (response) {
                if (response.ok) {
                    alert(' Média supprimé avec succès !');
                    location.reload();
                } else {
                    alert(' Erreur lors de la suppression (code: ' + response.status + ')');
                }
            })
            .catch(function (error) {
                alert(' Erreur de connexion: ' + error.message);
            });
    }
}

document.querySelectorAll('.admin-nav a').forEach(function (link) {
    link.addEventListener('click', function (e) {
        if (!this.getAttribute('data-tab')) return;

        e.preventDefault();
        let tabId = this.getAttribute('data-tab');

        document.querySelectorAll('.admin-nav a').forEach(function (l) {
            l.classList.remove('active');
        });
        this.classList.add('active');

        document.querySelectorAll('.admin-section').forEach(function (section) {
            section.classList.remove('active');
        });
        document.getElementById(tabId).classList.add('active');

        let titles = {
            'dashboard': 'Tableau de bord',
            'members': 'Gestion des membres',
            'loans': 'Gestion des emprunts',
            'medias': 'Catalogue des médias',
            'stats': 'Statistiques'
        };
        let headerTitle = document.querySelector('.admin-main > .admin-header h1');
        if (headerTitle) {
            headerTitle.textContent = titles[tabId];
        }
    });
});

function filterLoans(event, filter) {
    let rows = document.querySelectorAll('#loansTable tbody tr');
    rows.forEach(function (row) {
        if (row.cells && row.cells[4]) {
            let status = row.cells[4].innerText;
            if (filter === 'all') {
                row.style.display = '';
            } else if (filter === 'current' && status === 'BORROWED') {
                row.style.display = '';
            } else if (filter === 'late' && status === 'EN_RETARD') {
                row.style.display = '';
            } else if (filter === 'returned' && status === 'RETOURNE') {
                row.style.display = '';
            } else {
                row.style.display = 'none';
            }
        }
    });

    let btns = document.querySelectorAll('.tab-btn');
    btns.forEach(function (btn) {
        btn.classList.remove('active');
    });
    if (event && event.target) {
        event.target.classList.add('active');
    }
}