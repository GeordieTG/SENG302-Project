function getRole(event) {
    let table = document.getElementById("team_roles_body")
    let values = table.getElementsByTagName("select")
    let manager_count = 0
    for (let i = 0; i < values.length; i++) {
        if (values.item(i).value === "Manager") {
            manager_count++
        }
    }
    if (manager_count > 3) {
        let toast = document.getElementById("tooManyManagerToast")
        let bootstrapToast = new bootstrap.Toast(toast)
        bootstrapToast.show()
    } else if (manager_count === 0) {
        let toast = document.getElementById("NoEnoughManager")
        let bootstrapToast = new bootstrap.Toast(toast)
        bootstrapToast.show()
    } else {
        event.form.submit()
    }
}

function checkIfChanged() {
    try {
        let check = document.getElementById("rolesHaveChanged").value
        if (check == "true") {
            let toast = document.getElementById("rolesHaveChangedToast")
            let bootstrapToast = new bootstrap.Toast(toast)
            bootstrapToast.show()
        }
    } catch (error) {
    }
}

function validateSelect(selectedElement) {
    let table = document.getElementById("team_roles_body")
    let values = table.getElementsByTagName("select")
    let manager_count = 0
    for (let i = 0; i < values.length; i++) {
        if (values.item(i).value === "Manager") {
            manager_count++
            if (manager_count > 3) {
                values.item(i).classList.add("is-invalid");
                values.item(i).classList.remove("is-valid");
            } else {
                values.item(i).classList.remove("is-invalid");
                values.item(i).classList.add("is-valid");
            }
        }
    }
    for (let i = 0; i < values.length; i++) {
        if (values.item(i).value !== "Manager") {
            values.item(i).classList.remove("is-invalid");
            values.item(i).classList.remove("is-valid");
        }
    }
    if (selectedElement.value !== "Manager" && manager_count === 0) {
        selectedElement.classList.add("is-invalid");
    }
}