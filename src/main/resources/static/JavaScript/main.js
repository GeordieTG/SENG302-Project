/**
 * Checks to see if the user has inputted matching passwords
 * @return boolean true if the passwords match, false otherwise
 */
function checkPass() {
    let passValue = document.getElementById("password").value;
    let confpassValue = document.getElementById("confirmPassword").value;
    let pass = document.getElementById("password");
    let confPass = document.getElementById("confirmPassword");
    if (passValue !== confpassValue) {
        confPass.style.borderColor = "red";
        confPass.style.outlineColor = "red";
        pass.style.borderColor = "red";
        pass.style.outlineColor = "red";
        return false;
    }
    confPass.style.borderColor = "black";
    confPass.style.outlineColor = "black";
    pass.style.borderColor = "black";
    pass.style.outlineColor = "black";
    return true;
}

/**
 * Switches between visible password input and hidden input
 */
function passwordVis() {
    let pwd = document.getElementById('password');
    let confirm = document.getElementById('confirmPassword');
    if (pwd.type === "password") {
        pwd.type = "text";
        confirm.type = "text";
    } else {
        pwd.type = "password";
        confirm.type = "password";
    }
}

function tokenShow() {
    let token = document.getElementById('invitation-token-section')
    if (token.type === "password") {
        token.type = "text";
    } else {
        token.type = "password";
    }
}

function nextPage() {
    document.getElementById('page-1').style.display = 'none';
    document.getElementById('page-2').style.display = 'block';
}

function previousPage() {
    document.getElementById('page-1').style.display = 'block';
    document.getElementById('page-2').style.display = 'none';
}


/**
 * Checks to see if the user has uploaded a valid image type
 * @param event the file upload event
 */
async function handleChange(event) {
    let pfp = document.getElementById("profilePicture");
    let pfpLabel = document.getElementById("profilePictureLabel");
    let allowed = ["image/png", "image/jpeg"];
    const file = event.target.files[0];
    if (!allowed.includes(file.type)) {
        pfpLabel.style.borderColor = "red";
        pfpLabel.style.outlineColor = "red";
    } else {
        pfpLabel.style.borderColor = "black";
        pfpLabel.style.outlineColor = "black";
    }
}

/**
 * Checks to see if the user has chosen a strong enough password
 */
function StrengthChecker() {
    let password = document.getElementById('password');
    let strength = document.getElementById('strength');
    let firstName = document.getElementById("firstName").value;
    let lastName = document.getElementById("lastName").value;
    let email = document.getElementById("email").value;
    let dob = document.getElementById("DOB").value;
    let lowercaseRequire = document.getElementById("lowercaseRequirement");
    let uppercaseRequire = document.getElementById("uppercaseRequirement");
    let numberRequire = document.getElementById("numberRequirement");
    let specialRequire = document.getElementById("specialRequirement");
    let lengthRequire = document.getElementById("lengthRequirement");
    let formDetails = document.getElementById("formDetails");
    let strongPassword = new RegExp("(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*=_+?><~`])[a-zA-Z-0-9!@#$%^&*=_+?><~`]{8,}");
    let containLowerCase = new RegExp("(?=.*[a-z])");
    let containUpperCase = new RegExp("(?=.*[A-Z])");
    let containNumber = new RegExp("(?=.*[0-9])");
    let containSpecial = new RegExp("(?=.*[!@#$%^&*=_+?><~`])");
    let minLength = new RegExp(".{8,}");
    let noFormValues;

    if (containLowerCase.test(password.value)) {
        lowercaseRequire.style.display = "none";
    } else {
        lowercaseRequire.style.display = "block";
    }

    if (containUpperCase.test(password.value)) {
        uppercaseRequire.style.display = "none";
    } else {
        uppercaseRequire.style.display = "block";
    }

    if (containNumber.test(password.value)) {
        numberRequire.style.display = "none";
    } else {
        numberRequire.style.display = "block";
    }

    if (containSpecial.test(password.value)) {
        specialRequire.style.display = "none";
    } else {
        specialRequire.style.display = "block";
    }

    if (minLength.test(password.value)) {
        lengthRequire.style.display = "none";
    } else {
        lengthRequire.style.display = "block";
    }
    if (password.value.includes(firstName) && firstName.length != 0 || password.value.includes(lastName) && lastName.length != 0 || password.value.includes(email) && email.length != 0 || password.value.includes(dob) && dob.length != 0) {
        formDetails.style.display = "block";
        noFormValues = false;
    } else {
        formDetails.style.display = "none";
        noFormValues = true;
    }
    if (strongPassword.test(password.value) && noFormValues) {
        strength.innerText = 'Strong';
        strength.style.color = 'green';
    } else {
        strength.innerText = 'Weak';
        strength.style.color = 'red';
    }
}

/**
 * Takes in the user's details as parameters to make sure the new password they enter doesn't contain those fields, also does password strength checking
 * @param firstName the user's first name
 * @param lastName the user's last name
 * @param email the user's email name
 * @param dob the user's dob
 * @constructor
 */
function StrengthCheckerChangePassword(firstName, lastName, email, dob) {
    let password = document.getElementById('password');
    let strength = document.getElementById('strength');
    let lowercaseRequire = document.getElementById("lowercaseRequirement");
    let uppercaseRequire = document.getElementById("uppercaseRequirement");
    let numberRequire = document.getElementById("numberRequirement");
    let specialRequire = document.getElementById("specialRequirement");
    let lengthRequire = document.getElementById("lengthRequirement");
    let formDetails = document.getElementById("formDetails");
    let strongPassword = new RegExp("(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*=_+?><~`])[a-zA-Z-0-9!@#$%^&*=_+?><~`]{8,}");
    let containLowerCase = new RegExp("(?=.*[a-z])");
    let containUpperCase = new RegExp("(?=.*[A-Z])");
    let containNumber = new RegExp("(?=.*[0-9])");
    let containSpecial = new RegExp("(?=.*[!@#$%^&*=_+?><~`])");
    let minLength = new RegExp(".{8,}");
    let noFormValues;

    if (containLowerCase.test(password.value)) {
        lowercaseRequire.style.display = "none";
    } else {
        lowercaseRequire.style.display = "block";
    }
    if (containUpperCase.test(password.value)) {
        uppercaseRequire.style.display = "none";
    } else {
        uppercaseRequire.style.display = "block";
    }
    if (containNumber.test(password.value)) {
        numberRequire.style.display = "none";
    } else {
        numberRequire.style.display = "block";
    }
    if (containSpecial.test(password.value)) {
        specialRequire.style.display = "none";
    } else {
        specialRequire.style.display = "block";
    }
    if (minLength.test(password.value)) {
        lengthRequire.style.display = "none";
    } else {
        lengthRequire.style.display = "block";
    }
    if (password.value.includes(firstName) || password.value.includes(lastName) || password.value.includes(email) || password.value.includes(dob)) {
        formDetails.style.display = "block";
        noFormValues = false;
    } else {
        formDetails.style.display = "none";
        noFormValues = true;
    }
    if (strongPassword.test(password.value) && noFormValues) {
        strength.innerText = 'Strong';
        strength.style.color = 'green';
    } else {
        strength.innerText = 'Weak';
        strength.style.color = 'red';
    }
}

function checkFirstName() {

    let nameRegex = new RegExp("^([a-zA-Z\u3040-ヿ㐀-䶿一-\u9FFF豈-\uFAFFｦ-ﾟㄱ-힝Ā-ſ]{1,30}[' ']{0,1})+$");
    let input = document.getElementById("firstName");
    if (nameRegex.test(input.value)) {
        input.style.outlineColor = "black";
        input.style.borderColor = "black";
    } else {
        input.style.outlineColor = "red";
        input.style.borderColor = "red";
    }
}

function checkLastName() {

    let nameRegex = new RegExp("^([a-zA-Z\u3040-ヿ㐀-䶿一-\u9FFF豈-\uFAFFｦ-ﾟㄱ-힝Ā-ſ]{1,30}[' ']{0,1})+$");
    let input = document.getElementById("lastName");
    if (nameRegex.test(input.value)) {
        input.style.outlineColor = "black";
        input.style.borderColor = "black";
    } else {
        input.style.outlineColor = "red";
        input.style.borderColor = "red";
    }
}

/**
 * Checks to see if the user has chosen a strong enough password
 */
function NewPasswordStrengthChecker() {
    let password = document.getElementById('password');
    let strength = document.getElementById('strength');
    let lowercaseRequire = document.getElementById("lowercaseRequirement");
    let uppercaseRequire = document.getElementById("uppercaseRequirement");
    let numberRequire = document.getElementById("numberRequirement");
    let specialRequire = document.getElementById("specialRequirement");
    let lengthRequire = document.getElementById("lengthRequirement");
    let strongPassword = new RegExp("(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*=_+?><~`])[a-zA-Z-0-9!@#$%^&*=_+?><~`]{8,}");
    let containLowerCase = new RegExp("(?=.*[a-z])");
    let containUpperCase = new RegExp("(?=.*[A-Z])");
    let containNumber = new RegExp("(?=.*[0-9])");
    let containSpecial = new RegExp("(?=.*[!@#$%^&*=_+?><~`])");
    let minLength = new RegExp(".{8,}");

    if (containLowerCase.test(password.value)) {
        lowercaseRequire.style.display = "none";
    } else {
        lowercaseRequire.style.display = "block";
    }

    if (containUpperCase.test(password.value)) {
        uppercaseRequire.style.display = "none";
    } else {
        uppercaseRequire.style.display = "block";
    }

    if (containNumber.test(password.value)) {
        numberRequire.style.display = "none";
    } else {
        numberRequire.style.display = "block";
    }

    if (containSpecial.test(password.value)) {
        specialRequire.style.display = "none";
    } else {
        specialRequire.style.display = "block";
    }

    if (minLength.test(password.value)) {
        lengthRequire.style.display = "none";
    } else {
        lengthRequire.style.display = "block";
    }
    if (strongPassword.test(password.value)) {
        strength.innerText = 'Strong';
        strength.style.color = 'green';
    } else {
        strength.innerText = 'Weak';
        strength.style.color = 'red';
    }
}

function checkCity() {
    let nameRegex = new RegExp("^([a-zA-Z\u3040-ヿ㐀-䶿一-\u9FFF豈-\uFAFFｦ-ﾟㄱ-힝Ā-ſ]{1,30}[' ']{0,1})+$");
    let input = document.getElementById("city");
    if (nameRegex.test(input.value)) {
        input.style.outlineColor = "black";
        input.style.borderColor = "black";
    } else {
        input.style.outlineColor = "red";
        input.style.borderColor = "red";
    }
}

function checkCountry() {
    let nameRegex = new RegExp("^([a-zA-Z\u3040-ヿ㐀-䶿一-\u9FFF豈-\uFAFFｦ-ﾟㄱ-힝Ā-ſ][' ']{0,1}){4,48}$");
    let input = document.getElementById("country");
    if (nameRegex.test(input.value)) {
        input.style.outlineColor = "black";
        input.style.borderColor = "black";
    } else {
        input.style.outlineColor = "red";
        input.style.borderColor = "red";
    }
}

function checkEmail() {

    let emailRegex = new RegExp("^[a-zA-Z0-9_+&*-]+(?:.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+[.])+[a-zA-Z]{2,7}$");
    let input = document.getElementById("email");
    if (emailRegex.test(input.value)) {
        input.style.outlineColor = "black";
        input.style.borderColor = "black";
    } else {
        input.style.outlineColor = "red";
        input.style.borderColor = "red";
    }
}

function getUrl() {
    document.getElementById("url").value = window.location.href
}

/**
 * Checks to see if the users age is valid
 * @return boolean, true if it's a valid age, false otherwise
 */
function checkAge() {
    let dob = document.getElementById("DOB").value.split("-");
    let date = new Date(dob[0], parseInt(dob[1]) - 1, dob[2]);
    let today = new Date();
    let input = document.getElementById("DOB");
    if (((today.getTime() - date.getTime()) / 31536000000) < 13.01) {
        input.style.outlineColor = "red";
        input.style.borderColor = "red";
    } else {
        input.style.outlineColor = "black";
        input.style.borderColor = "black";
    }
}

function checkSuburb() {

    let suburbRegex = new RegExp("^([a-z-A-Z][' ']{0,1}){1,30}$");
    let input = document.getElementById("suburb");
    if (suburbRegex.test(input.value)) {
        input.style.outlineColor = "black";
        input.style.borderColor = "black";
    } else {
        input.style.outlineColor = "red";
        input.style.borderColor = "red";
    }
}

function checkPostcode() {

    let postcodeRegex = new RegExp("^([a-zA-Z0-9]){0,10}$");
    let input = document.getElementById("postcode");
    if (postcodeRegex.test(input.value)) {
        input.style.outlineColor = "black";
        input.style.borderColor = "black";
    } else {
        input.style.outlineColor = "red";
        input.style.borderColor = "red";
    }
}

function passwordStrengthIndicator() {

    const password = document.getElementById('password');
    const confirmPassword = document.getElementById('confirmPassword');
    const text = document.getElementById('passwordFeedback');
    const weak = document.querySelector(".weak");
    const medium = document.querySelector(".medium");
    const strong = document.querySelector(".strong");

    let strongPassword = new RegExp("(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*=_+?><~`])[a-zA-Z-0-9!@#$%^&*=_+?><~`]{8,}");
    let containLowerCase = new RegExp("(?=.*[a-z])");
    let containUpperCase = new RegExp("(?=.*[A-Z])");
    let containNumber = new RegExp("(?=.*[0-9])");
    let containSpecial = new RegExp("(?=.*[!@#$%^&*=_+?><~`])");

    let passwordFeedback = [];

    // Check the strength of the password
    let strength = 1;
    if (password.value.length >= 5 && ((password.value.match(containLowerCase) && password.value.match(containNumber)) || (password.value.match(containNumber) && password.value.match(containSpecial)) || (password.value.match(containLowerCase) && password.value.match(containSpecial)))) strength = 2;
    if (password.value.match(strongPassword)) strength = 3;
    // Set the bars according to strength
    weak.classList.add("active");
    if (strength == 2) {
        medium.classList.add("active");
    } else {
        medium.classList.remove("active");
    }
    if (strength == 3) {
        password.style.outlineColor = "black";
        password.style.borderColor = "black";
        confirmPassword.style.outlineColor = "black";
        confirmPassword.style.borderColor = "black";
        medium.classList.add("active");
        strong.classList.add("active");
    } else {
        strong.classList.remove("active");
    }

    // Password Feedback Text
    if (!password.value.match(containSpecial)) {
        passwordFeedback.push("special character");
    }
    if (!password.value.match(containUpperCase)) {
        passwordFeedback.push("uppercase letter");
    }
    if (!password.value.match(containLowerCase)) {
        passwordFeedback.push("lowercase letter");
    }
    if (!password.value.match(containNumber)) {
        passwordFeedback.push("number");
    }
    if (password.value.length < 8) {
        passwordFeedback.push(`${8 - password.value.length} more character(s)`);
    }
    text.textContent = passwordFeedback.length === 0 ? "Strong password" : `Missing ${passwordFeedback.join(", ")}`;
}

function changeTab() {
    const tokenChanged = document.getElementById("invite-token-hidden").value
    if (tokenChanged !== '') {
        // Remove the "active" class from the current active tab and tab-pane
        const activeTab = document.getElementById('nav-profile-tab');
        const tabContent = document.getElementById('nav-profile')
        activeTab.classList.remove('active');
        tabContent.classList.remove('show', 'active');

        // Add the "active" class to the nav-invites tab and tab-pane
        const invitesTab = document.getElementById('nav-invites-tab');
        const invitesPane = document.getElementById('nav-invites');
        invitesTab.classList.add('active');
        invitesPane.classList.add('show', 'active');
    }
}