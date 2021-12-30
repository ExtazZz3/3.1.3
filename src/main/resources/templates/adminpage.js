const URLusers = 'http://localhost:8080/admin/user/'
const URLroles = 'http://localhost:8080/admin/role/'
const URLuser = 'http://localhost:8080/user/'
const addNewUser = document.getElementById("new-user-form");
const deleteUser = document.getElementById("delete-form");
const editUser = document.getElementById("edit-form");
const editIdField = document.getElementById('editid');
const newNameField = document.getElementById('newname');
const editNameField = document.getElementById('editname');
const newPassField = document.getElementById('newpass');
const editPassField = document.getElementById('editpass');
const newHighEduField = document.getElementById('newhighEdu');
const editHighEduField = document.getElementById('edithighEdu');
const newBDField = document.getElementById('newbd');
const editBDField = document.getElementById('editbd');
const newRolesSelectorField = document.getElementById('newRolesSelector');
const delRolesSelectorField = document.getElementById('delRolesSelector');
const editRolesSelectorField = document.getElementById('editRolesSelector');
const modalEdit = new bootstrap.Modal(document.getElementById('modal-edit'))
const modalDelete = new bootstrap.Modal(document.getElementById('modal-delete'))
const highEduDel = document.getElementById("delhighEdu");
const highEduEdit = document.getElementById("edithighEdu");
const userTab = document.getElementById('list-profile-list');
let idForm;

getUserList();
getUserInfo();
getRolesList(newRolesSelectorField);
getRolesList(delRolesSelectorField);
getRolesList(editRolesSelectorField);

async function getUserList() {
    let response = await fetch(URLusers);
    if (response.ok) {
        let datajson = await response.json();
        let temp = '';
        datajson.forEach((u) => {
            temp += '<tr>';
            temp += '<td>' + u.id + '</td>';
            temp += '<td>' + u.name + '</td>';
            temp += '<td>' + u.dateOfBirth + '</td>';
            temp += '<td>' + u.highEdu + '</td>';
            temp += '<td>' + u.roles.map(r => r.role) + '</td>';
            temp += editButton();
            temp += deleteButton();
            temp += '</tr>';
        })
        document.getElementById("userList").innerHTML = temp;
    } else {
        alert("Error receiving userList data" + response.status);
    }
}

async function getUserInfo() {
    let response = await fetch(URLuser);
    if (response.ok) {
        let datajson = await response.json();
        let temp = '';
        temp += '<tr>';
        temp += '<td>' + datajson.id + '</td>';
        temp += '<td>' + datajson.name + '</td>';
        temp += '<td>' + datajson.dateOfBirth + '</td>';
        temp += '<td>' + datajson.highEdu + '</td>';
        temp += '<td>' + datajson.roles.map(r => r.role) + '</td>';
        temp += '</tr>';
        document.getElementById("currentUserInfo").innerHTML = temp;
        document.getElementById("currentUserName").innerText = datajson.name;
        let ur = datajson.roles.map(r => r.role).toString();
        document.getElementById("currentUserRoles").innerText = ur;
        if (!ur.includes('ADMIN')) {
            new bootstrap.Tab(userTab).show();
        }
    } else {
        alert("Error receiving userlist data" + response.status);
    }
}

async function getRolesList(place) {
    let response = await fetch(URLroles);
    if (response.ok) {
        let datajson = await response.json();
        datajson.forEach((r) => {
            place.options[place.options.length] = new Option(r.role, r.id);
        })
    } else {
        alert("Error receiving rolesList data" + response.status);
    }
}

function editButton() {
    let cell = document.createElement('td');
    return cell.innerHTML = '<td><button type="button" class="btnEdit btn btn-warning" data-bs-toggle="modal">Edit</button></td>'
}

function deleteButton() {
    let cell = document.createElement('td');
    return cell.innerHTML = '<td><button type="button" class="btnDelete btn btn-danger" data-bs-toggle="modal">Delete</button></td>'
}

const modalSolver = function (element, event, selector, handler) {
    element.addEventListener(event, e => {
        if (e.target.closest(selector)) {
            handler(e)
        }
    })
}

modalSolver(document, 'click', '.btnDelete', e => {
    const getElement = e.target.parentNode.parentNode
    idForm = getElement.firstElementChild.innerHTML
    getUserById();

    async function getUserById() {
        let response = await fetch(URLusers + idForm);
        if (response.ok) {
            let datajson = await response.json();
            delid.value = datajson.id;
            delname.value = datajson.name;
            delpass.value = datajson.password;
            delbd.value = datajson.dateOfBirth;
            highEduDel.checked = !!datajson.highEdu;
        } else {
            alert("Error receiving user data" + response.status);
        }
    }
    modalDelete.show();
})

modalSolver(document, 'click', '.btnEdit', e => {
    const getElement = e.target.parentNode.parentNode
    idForm = getElement.firstElementChild.innerHTML
    getUserById();

    async function getUserById() {
        let response = await fetch(URLusers + idForm);
        if (response.ok) {
            let datajson = await response.json();
            editid.value = datajson.id;
            editname.value = datajson.name;
            editpass.value = datajson.password;
            editbd.value = datajson.dateOfBirth;
            highEduEdit.checked = !!datajson.highEdu;
            editroles.value = datajson.roles;
        } else {
            alert("Error receiving user data" + response.status);
        }
    }
    modalEdit.show();
})

addNewUser.addEventListener('submit', (e) => {
    e.preventDefault()
    let a = new Promise(function (resolve) {
            resolve(
                fetch(URLusers, {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({
                            name: newNameField.value,
                            password: newPassField.value,
                            highEdu: highEduCheckBoxSolver(newHighEduField),
                            dateOfBirth: newBDField.value,
                            roles: RolesSelectorSolver(newRolesSelectorField)
                        })
                    }
                )
            )
        }
    )
    a.then(function () {
        getUserList();
        let listHome = document.getElementById('nav-home-tab');
        new bootstrap.Tab(listHome).show();
    })
})

deleteUser.addEventListener('submit', (e) => {
    e.preventDefault()
    let a = new Promise(function (resolve) {
        resolve(fetch(URLusers + idForm, {method: 'DELETE'}))
    })
    a.then(function () {
        getUserList();
        modalDelete.hide();
    })
})

editUser.addEventListener('submit', (e) => {
    e.preventDefault()
    let a = new Promise(function (resolve) {
            resolve(
                fetch(URLusers, {
                        method: 'PUT',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({
                            id: editIdField.value,
                            name: editNameField.value,
                            password: editPassField.value,
                            highEdu: highEduCheckBoxSolver(editHighEduField),
                            dateOfBirth: editBDField.value,
                            roles: RolesSelectorSolver(editRolesSelectorField)
                        })
                    }
                )
            )
        }
    )
    a.then(function () {
        getUserList();
        modalEdit.hide();
    })
})

function highEduCheckBoxSolver(place) {
    return !!place.checked;
}

function RolesSelectorSolver(place) {
    let JSONArr = [];
    for (let i = 0; i < place.options.length; i++) {
        if (place.options[i].selected) {
            JSONArr.push({
                id: place.options[i].value,
                role: place.options[i].text
            })
        }
    }
    return JSONArr;
}