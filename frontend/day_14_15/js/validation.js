function login() {
    let username = document.getElementById("loginUsername").value;
    let password = document.getElementById("loginPassword").value;
    // // You can perform login validation and authentication here
    // if (/\w/.test(username)) {
    //     // alert("Username can't be empty or contain special character");
    //     return;
    // }
    // else if (/^(?=.*[A-Z])(?=.*\d).{8,}$/.test(password)) {
    //     // alert("Invalid password");
    //     return;
    // }
    // // For simplicity, let's just display an alert
    console.log(`Login clicked. Username: ${username}, Password: ${password}`);
}

function register() {
    let registerName = document.getElementById("registerName").value;
    let registerEmail = document.getElementById("registerEmail").value;
    let registerUsername = document.getElementById("registerUsername").value;
    let registerPassword = document.getElementById("registerPassword").value;
    // Frontend validation for registration form
    // Validate email format
    // if (!/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(registerEmail)) {
    //     alert("Email invalid");
    // }
    
    // Validate username (no special characters)
    // if (/\w/.test(username)) {
    //     // alert("Username can't be empty or contain special character");
    //     return;
    // }

    // Validate password (at least 8 characters, one capital letter, and one numeric)
    // if (/^(?=.*[A-Z])(?=.*\d).{8,}$/.test(password)) {
    //     // alert("Invalid password");
    //     return;
    // }
    console.log(`Register clicked. Name: ${registerName}, Email: ${registerEmail}, Username: ${registerUsername}, Password: ${registerPassword}`);
}

module.exports = { login, register };
