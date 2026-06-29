// ============================================
// UTILITAIRES
// ============================================

function getCsrfToken() {
    let tokenInput = document.getElementById('csrfToken');
    if (tokenInput) {
        return tokenInput.value;
    }
    let csrf = document.querySelector('input[name="_csrf"]');
    return csrf ? csrf.value : '';
}

// ============================================
// GESTION DES MODALES - ÉDITION
// ============================================

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

// ============================================
// GESTION DES MODALES - SUPPRESSION
// ============================================

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

// ============================================
// ACTIONS CRUD - MEMBRES
// ============================================

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
                alert('Membre supprimé avec succès !');
                closeDeleteModal();
                location.reload();
            } else {
                alert('Erreur lors de la suppression (code: ' + response.status + ')');
            }
        })
        .catch(function (error) {
            alert('Erreur de connexion: ' + error.message);
        });
}

function updateMember(id, data) {
    let csrfToken = getCsrfToken();

    return fetch('/admin/members/update/' + id, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': csrfToken
        },
        body: JSON.stringify(data)
    });
}

// ============================================
// ACTIONS CRUD - EMPRUNTS
// ============================================

function returnLoan(id) {
    if (confirm('Confirmer le retour de l\'emprunt ID: ' + id + ' ?')) {
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
                    alert('Emprunt retourné avec succès !');
                    location.reload();
                } else {
                    alert('Erreur lors du retour (code: ' + response.status + ')');
                }
            })
            .catch(function (error) {
                alert('Erreur de connexion: ' + error.message);
            });
    }
}

function deleteLoan(id) {
    if (confirm('Supprimer l\'emprunt ID: ' + id + ' ?')) {
        let csrfToken = getCsrfToken();

        fetch('/admin/loans/delete/' + id, {
            method: 'DELETE',
            headers: {
                'X-CSRF-TOKEN': csrfToken
            }
        })
            .then(function (response) {
                if (response.ok) {
                    alert('Emprunt supprimé avec succès !');
                    location.reload();
                } else {
                    alert('Erreur lors de la suppression (code: ' + response.status + ')');
                }
            })
            .catch(function (error) {
                alert('Erreur de connexion: ' + error.message);
            });
    }
}

// ============================================
// ACTIONS CRUD - MÉDIAS
// ============================================

function editMedia(id) {
    window.location.href = '/admin/medias/edit/' + id;
}

function deleteMedia(id) {
    if (confirm('Supprimer le média ID: ' + id + ' ?')) {
        let csrfToken = getCsrfToken();

        fetch('/admin/medias/delete/' + id, {
            method: 'DELETE',
            headers: {
                'X-CSRF-TOKEN': csrfToken
            }
        })
            .then(function (response) {
                if (response.ok) {
                    alert('Média supprimé avec succès !');
                    location.reload();
                } else {
                    alert('Erreur lors de la suppression (code: ' + response.status + ')');
                }
            })
            .catch(function (error) {
                alert('Erreur de connexion: ' + error.message);
            });
    }
}

// ============================================
// FILTRE DES EMPRUNTS
// ============================================

function filterLoans(event, filter) {
    let rows = document.querySelectorAll('#loansTable tbody tr:not(.empty-row)');
    rows.forEach(function (row) {
        if (row.cells && row.cells[4]) {
            let status = row.cells[4].innerText.trim();
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

// ============================================
// NAVIGATION PAR ONGLETS
// ============================================

function initTabs() {
    document.querySelectorAll('.admin-nav a[data-tab]').forEach(function (link) {
        link.addEventListener('click', function (e) {
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
                headerTitle.textContent = titles[tabId] || 'Tableau de bord';
            }
        });
    });
}

// ============================================
// INITIALISATION - TOUS LES ÉCOUTEURS D'ÉVÉNEMENTS
// ============================================

document.addEventListener('DOMContentLoaded', function () {

    // ---- Navigation par onglets ----
    initTabs();

    // ---- Formulaire d'édition ----
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

        updateMember(id, data)
            .then(function (response) {
                if (response.ok) {
                    alert('Membre modifié avec succès !');
                    closeEditModal();
                    location.reload();
                } else {
                    alert('Erreur lors de la modification (code: ' + response.status + ')');
                }
            })
            .catch(function (error) {
                alert('Erreur de connexion: ' + error.message);
            });
    });

    // ---- Fermeture modale édition ----
    document.getElementById('closeEditBtn').addEventListener('click', closeEditModal);
    document.getElementById('editModal').addEventListener('click', function (e) {
        if (e.target === this) closeEditModal();
    });

    // ---- Fermeture modale suppression ----
    document.getElementById('closeDeleteBtn').addEventListener('click', closeDeleteModal);
    document.getElementById('deleteModal').addEventListener('click', function (e) {
        if (e.target === this) closeDeleteModal();
    });

    // ---- Confirmation suppression ----
    document.getElementById('confirmDeleteBtn').addEventListener('click', confirmDelete);

    // ---- Boutons d'édition des membres ----
    document.querySelectorAll('.edit-member-btn').forEach(function (btn) {
        btn.addEventListener('click', function () {
            openEditModal(this);
        });
    });

    // ---- Boutons de suppression des membres ----
    document.querySelectorAll('.delete-member-btn').forEach(function (btn) {
        btn.addEventListener('click', function () {
            openDeleteModal(this);
        });
    });

    // ---- Boutons de retour d'emprunt ----
    document.querySelectorAll('.return-loan-btn').forEach(function (btn) {
        btn.addEventListener('click', function () {
            let id = this.getAttribute('data-id');
            returnLoan(id);
        });
    });

    // ---- Boutons de suppression d'emprunt ----
    document.querySelectorAll('.delete-loan-btn').forEach(function (btn) {
        btn.addEventListener('click', function () {
            let id = this.getAttribute('data-id');
            deleteLoan(id);
        });
    });

    // ---- Boutons d'édition des médias ----
    document.querySelectorAll('.edit-media-btn').forEach(function (btn) {
        btn.addEventListener('click', function () {
            let id = this.getAttribute('data-id');
            editMedia(id);
        });
    });

    // ---- Boutons de suppression des médias ----
    document.querySelectorAll('.delete-media-btn').forEach(function (btn) {
        btn.addEventListener('click', function () {
            let id = this.getAttribute('data-id');
            deleteMedia(id);
        });
    });

});