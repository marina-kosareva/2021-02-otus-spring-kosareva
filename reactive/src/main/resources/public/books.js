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
                        <td>${book.genre}</td>
                        <td>${book.author}</td>
                        <td><button onclick="updateBook('${book.id}', ${book.version},'${book.genre}','${book.author}' )">
                        <i class="fas fa-edit"></i></button></td>
                        <td><button onclick="deleteBook('${book.id}')"><i class="far fa-trash-alt"></i></button></td>
                    </tr>
                `)
        });
    })
}

function addBook() {
    let book = {};
    book.title = $('#addTitle').val();
    book.genre = $('#addGenre').val();
    book.author = $('#addAuthor').val();
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

function updateBook(id, version, genre, author) {
    let updateBookRequest = {};
    updateBookRequest.author = author;
    updateBookRequest.genre = genre;
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
    $('#addGenre').val('');
    $('#addAuthor').val('');
}
