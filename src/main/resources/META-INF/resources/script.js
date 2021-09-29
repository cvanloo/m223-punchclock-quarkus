const URL = 'http://localhost:8080';
const JWT = localStorage.getItem("jwt"); // read jwt from session storage
let entries = [];
let mode = 'create';
let selectedEntry = null;

const dateAndTimeToDate = (dateString, timeString) => {
    return new Date(`${dateString}T${timeString}`).toISOString();
};

const splitDateAndTime = (datetime) => {
    return datetime.split("T");
};

const reset = () => {
    mode = 'create';
    selectedEntry = null;
    document.getElementById("btnSubmit").value = "Create";
}

const createEntry = (entry) => {
    fetch(`${URL}/entries`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + JWT
        },
        body: JSON.stringify(entry)
    }).then((result) => {
        result.json().then((entry) => {
            entries.push(entry);
            renderEntries();
        });
    });
};

const saveEdit = (entry) => {
    fetch(`${URL}/entries`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + JWT
        },
        body: JSON.stringify(entry)
    }).then((result) => {
        indexEntries();
        reset();
    });
}

const submit = (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    const entry = {};
    entry['checkIn'] = dateAndTimeToDate(formData.get('checkInDate'), formData.get('checkInTime'));
    entry['checkOut'] = dateAndTimeToDate(formData.get('checkOutDate'), formData.get('checkOutTime'));
    entry['user'] = null; // user is set by the REST API based on the JWT Token.
    entry['category'] = null; // hasn't been implemented by the user interface yet.
    entry['facility'] = null; // hasn't been implemented by the user interface yet.

    if (mode === 'create') {
        createEntry(entry);
    } else if (mode === 'edit') {
        entry['id'] = selectedEntry['id'];
        saveEdit(entry);
    }
};

const editEntry = (entry) => {
    mode = 'edit';
    selectedEntry = entry;

    document.getElementById("btnSubmit").value = "Save changes";

    const datetimeIn = splitDateAndTime(entry['checkIn']);
    const datetimeOut = splitDateAndTime(entry['checkOut']);

    document.getElementById("checkInDate").value = datetimeIn[0];
    document.getElementById("checkInTime").value = datetimeIn[1];
    document.getElementById("checkOutDate").value = datetimeOut[0];
    document.getElementById("checkOutTime").value = datetimeOut[1];
};

const deleteEntry = (entryId) => {
    fetch(`${URL}/entries/${entryId}`, {
        method: 'DELETE',
        headers: {
            'Authorization': 'Bearer ' + JWT
        }
    }).then(() => {
        indexEntries();
    });
};

const indexEntries = () => {
    fetch(`${URL}/entries`, {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + JWT
        }
    }).then((result) => {
        result.json().then((result) => {
            entries = result;
            renderEntries();
        });
    });
};

const createCell = (text) => {
    const cell = document.createElement('td');
    cell.innerText = text;
    return cell;
};

// Generate the buttons for each table entry
const createActions = (entry) => {
    const cell = document.createElement('td');

    const deleteButton = document.createElement('button');
    deleteButton.innerText = 'Delete';
    deleteButton.addEventListener('click', () => deleteEntry(entry.id));
    cell.appendChild(deleteButton);

    const editButton = document.createElement('button');
    editButton.innerText = 'Edit';
    editButton.addEventListener('click', () => editEntry(entry));
    cell.appendChild(editButton);

    return cell;
}

// Build the table
const renderEntries = () => {
    const display = document.querySelector('#entryDisplay');
    display.innerHTML = '';
    entries.forEach((entry) => {
        const row = document.createElement('tr');
        row.appendChild(createCell(entry.id));
        row.appendChild(createCell(new Date(entry.checkIn).toLocaleString()));
        row.appendChild(createCell(new Date(entry.checkOut).toLocaleString()));
        row.appendChild(createActions(entry));
        display.appendChild(row);
    });
};

document.addEventListener('DOMContentLoaded', function(){
    const createEntryForm = document.querySelector('#createEntryForm');
    createEntryForm.addEventListener('submit', submit);
    createEntryForm.addEventListener('reset', reset);
    indexEntries();
});