// if local
/*
<script>
document.getElementById('sendDummyUser01').addEventListener('click', function() {
    fetch('http://localhost:8000/dummyUser_local_01', {
        method: 'GET',
    })
    .then(response => {
        if(response.ok) {
            return response.text();
        }
        throw new Error('The request failed.');
    })
    .then(data => {
        console.log(data);
    })
    .catch(error => {
        console.error(error);
    });
});
*/

// if not local -->
document.getElementById('sendDummyUser01').addEventListener('click', function() {
    fetch('http://localhost:8000/dummyUser_01', {
        method: 'GET',
    })
    .then(response => {
        if(response.ok) {
            return response.text();
        }
        throw new Error('The request failed.');
    })
    .then(data => {
        console.log(data);
    })
    .catch(error => {
        console.error(error);
    });
});