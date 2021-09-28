const URL = 'http://localhost:8080';

const login = (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    const authRequest = {};
    authRequest['username'] = formData.get('username');
    authRequest['password'] = formData.get('password');

    fetch(`${URL}/auth/login`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(authRequest)
    }).then((response) => {
        response.text().then((token) => {
            console.log(token);
            localStorage.setItem("jwt", token);
        });
    });
};

document.addEventListener('DOMContentLoaded', function(){
    const createEntryForm = document.querySelector('#loginForm');
    createEntryForm.addEventListener('submit', login);
});