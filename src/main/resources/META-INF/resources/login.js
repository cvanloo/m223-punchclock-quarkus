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
    }).then((result) => {
        result.json().then((token) => {
            console.log(token);
        });
    });

}

document.addEventListener('DOMContentLoaded', function(){
    const createEntryForm = document.querySelector('#loginForm');
    createEntryForm.addEventListener('submit', login);
});