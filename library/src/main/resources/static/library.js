$(document).ready(function () {
    getBooks();
});

function getBooks() {
    $('#books tbody').empty();
    $.get('/book').done(function (books) {
        books.forEach(function (book) {
            $('#books tbody').append(`
                    <tr>
                        <td>${book.id}</td>
                        <td>${book.version}</td>
                        <td><input id = 'upd_title_${book.id}' type="text" value='${book.title}'></td>
                        <td>${book.genreTitle}</td>
                        <td>${book.authorFullName}</td>
                        <td><button onclick="updateBook('${book.id}', ${book.version})"><i class="fas fa-edit"></i></button></td>
                        <td><button onclick="deleteBook('${book.id}')"><i class="far fa-trash-alt"></i></button></td>
                    </tr>
                `)
        });
    })
}

function addBook() {
    let book = {};
    book.title = $('#addTitle').val();
    book.genreId = $('#genre').val();
    book.authorId = $('#author').val();
    $.ajax({
        url: "/book",
        method: 'POST',
        data: JSON.stringify(book),
        contentType: 'application/json; charset=utf-8',
        success: function () {
            getBooks();
            clear();
        },
        error: function (error) {
            alert(error.responseText);
        }
    })
}

function updateBook(id, version) {
    let updateBookRequest = {};
    updateBookRequest.title = document.getElementById('upd_title_' + id).value;
    updateBookRequest.version = version;

    $.ajax({
        url: "/book/" + id,
        method: 'PUT',
        data: JSON.stringify(updateBookRequest),
        contentType: 'application/json; charset=utf-8',
        success: function () {
            getBooks();
        },
        error: function (error) {
            alert(error.responseText);
            getBooks();
        }
    })
}

function deleteBook(id) {
    $.ajax({
        url: '/book/' + id,
        method: 'DELETE',
        success: function () {
            getBooks();
        },
        error: function (error) {
            alert(error.responseText);
            getBooks();
        }
    })
}

function clear() {
    $('#addTitle').val('');
}
