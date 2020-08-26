var movieId;

function loadDetails() {
    clearMessages();
    var url = GW_ENDPOINT + "/movies/get/" + movieId;
    console.log("URL: " + url)

    $.ajax({
        method: "GET", // Declare request type
        headers: {
            "Content-Type": "application/json",
            "SessionID": $.cookie("sessionID"),
            "email": $.cookie("email")
        },
        url: url
    }).done(function (data, status, xhr) {

        console.log(data);
        console.log(status);
        console.log(xhr);

        if (xhr.status !== 204) {
            if (xhr.status == 200)
            {
                if (data)
                {
                    document.getElementById("loginResult").style.color = "red";
                    document.getElementById("loginResult").innerHTML = data.message;
                    activateTab("login");
                }
            }
            else {
                var message = xhr.responseJSON ? xhr.responseJSON.message : "Oops! Something went wrong!";
                document.getElementById("movieDetailStatus").style.color = "red";
                document.getElementById("movieDetailStatus").innerHTML = message;
            }

            return false;
        }

        var transactionID = xhr.getResponseHeader('transactionID');
        $.cookie("transactionID", transactionID);
        console.log("SessionID: " + $.cookie("sessionID"));
        console.log("email: " + $.cookie("email"))

        setTimeout(function () {
            $.ajax({
                method: "GET", // Declare request type
                url: GW_ENDPOINT + "/report",
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "*/*",
                    'email': $.cookie("email"),
                    'transactionID': $.cookie('transactionID'),
                    'sessionID': $.cookie('sessionID')
                }

            }).done(function (data, status, xhr) {

                console.log(data);
                console.log(status);
                console.log(xhr);

                if (status !== "success") {
                    return false;
                }


                if (data) {

                    if (data.resultCode !== 210) {
                        document.getElementById("movieDetails").innerHTML = data.message;
                        return false;
                    }

                    console.log(data);

                    var movie = data.movie;
                    console.log(movie);

                    var result = "<h2>Movie Details</h2>";

                    result += '<table border="0" cellpadding="6">';
                    result += '<tr><td valign="top"><b>Movie ID:</b></td><td valign="top">' + movie.movieId + '</td></tr>';
                    if (movie.title ) result += '<tr><td valign="top"><b>Title:</b></td><td valign="top">' + movie.title + '</td></tr>';
                    if (movie.director) result += '<tr><td valign="top"><b>Director:</b></td><td valign="top">' + movie.director + '</td></tr>';
                    if (movie.year) result += '<tr><td valign="top"><b>Year:</b></td><td valign="top">' + movie.year + '</td></tr>';
                    if (movie.backdrop_path) result += '<tr><td valign="top"><b>Backdrop Path:</b></td><td valign="top">' + movie.backdrop_path + '</td></tr>';
                    if (movie.overview) result += '<tr><td valign="top"><b>Overview:</b></td><td valign="top">' + movie.overview  + '</td></tr>';
                    if (movie.poster_path) result += '<tr><td valign="top"><b>Poster Path:</b></td><td valign="top">' + movie.poster_path  + '</td></tr>';
                    if (movie.rating) result += '<tr><td valign="top"><b>Rating:</b></td><td valign="top">' + movie.rating + '</td></tr>';
                    if (movie.numVotes) result += '<tr><td valign="top"><b>Votes:</b></td><td valign="top">' + movie.numVotes + '</td></tr>';

                    if (movie.genres && movie.genres.length > 0) {
                        var params = [];
                        for (let i = 0; i < movie.genres.length; i++) {
                            params[movie.genres[i].id] = movie.genres[i].name
                        }

                        var genres = Object.keys(params).map(function (key) {
                            return params[key]
                        }).join(', ');

                        result += '<tr><td valign="top"><b>Genres</b></td><td valign="top">' + genres + '</td></tr>';
                    }

                    if (movie.stars && movie.stars.length > 0) {
                        params = [];
                        for (let i = 0; i < movie.stars.length; i++) {
                            params[movie.stars[i].id] = movie.stars[i].name
                        }

                        var stars = Object.keys(params).map(function (key) {
                            return params[key]
                        }).join(', ');

                        result += '<tr><td valign="top"><b>Stars</b></td><td valign="top">' + stars + '</td></tr>';
                    }

                    result += '</table>';

                    var style = document.getElementById("movieDetailStatus").style.color;
                    var old = document.getElementById("movieDetailStatus").innerHTML;

                    // result += "<br>";
                    // result += '<b id="movieDetailStatus"></b>';
                    // result += "<br>";

                    result += '<table class="centerTable5" border="0" cellpadding="2">';
                    result += '<tr><td>';
                    result += '<table border="0" cellpadding="10">';
                    result += '<tr><td><b>Rate Movie:</b></td>';
                    result += '<td><div class="quantity"><input class="quantity" type="number" name="rating" value="10" min="0" max="10" step="0.1"></div><td><button type="submit" class="submitbtn" onclick="rateMovie()">Rate Movie</button></td></tr>';

                    result += '<tr><td><b>Add Cart:</b></td>';
                    result += '<td><div class="quantity"><input class="quantity" type="number" name="quantity" value="1" min="1"></div><td><button type="submit" class="submitbtn" onclick="addToCart()">Add Cart</button></td></tr>';
                    result += '</table>';
                    result += '</td>';
                    result += '<td><button type="goBack" class="submitbtn" onclick="goBackListing()">Go Back</button>';
                    result += '</td></tr></table>';

                    clearMessages();


                    document.getElementById("movieDetails").innerHTML = result;

                    document.getElementById("movieDetailStatus").style.color = style;
                    document.getElementById("movieDetailStatus").innerHTML = old;
                    activateTab("movieDetailPage");

                    console.log("old: " + document.getElementById("movieDetailStatus").innerHTML);

                } else {
                    console.log("No data received");
                    document.getElementById("movieDetails").innerHTML = "No response data received";
                    activateTab("movieDetailPage");
                }
            })
            .fail(function (xhr, exception, data) {
                console.log(xhr);
                console.log(exception);
                console.log(data);
                var message = xhr.responseJSON ? xhr.responseJSON.message : "Oops! Something went wrong!";
                document.getElementById("browseMovieStatus").style.color = "red";
                document.getElementById("browseMovieStatus").innerHTML = message;
            });
        }, $.cookie("delay"))

    })
    .fail(function (xhr, exception, data) {
        console.log(xhr);
        console.log(exception);
        console.log(data);
        var message = xhr.responseJSON ? xhr.responseJSON.message : "Oops! Something went wrong!";
        document.getElementById("browseMovieStatus").style.color = "red";
        document.getElementById("browseMovieStatus").innerHTML = message;
    });
}

function movieDetails() {
    movieId = event.target.innerText;
    console.log(movieId);

    loadDetails(movieId);
}

function rateMovie() {
    var cells = event.target.parentNode.parentNode.getElementsByTagName("td");
    var rating = cells[1].childNodes[0].childNodes[0].value;

    _data = {
        "id": movieId,
        "rating": rating
    };

    console.log(_data);

    $.ajax({
        method: "POST", // Declare request type
        url: GW_ENDPOINT + "/movies/rating",
        headers: {
            "Content-Type": "application/json",
            "sessionID": $.cookie("sessionID"),
            "email": $.cookie("email")
        },
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify(_data)
    }).done(function (data, status, xhr) {
        var transactionID = xhr.getResponseHeader('transactionID');
        var delay = xhr.getResponseHeader('delay');

        $.cookie("transactionID", transactionID);
        $.cookie("delay", delay);

        console.log("TransactionID: " + $.cookie("transactionID"));
        console.log("delay: " + delay);

        setTimeout(function () {
            $.ajax({
                method: "GET", // Declare request type
                url: GW_ENDPOINT + "/report",
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "*/*",
                    'email': $.cookie("email"),
                    'transactionID': $.cookie('transactionID'),
                    'sessionID': $.cookie('sessionID')
                }

            }).done(function (data, status) {
                console.log(data);

                if (status !== "success") {
                    document.getElementById("movieDetailStatus").style.color = "red";
                    document.getElementById("movieDetailStatus").innerHTML = data.message;
                    return false;
                }

                if (data.resultCode = 250)
                {
                    document.getElementById("movieDetailStatus").style.color = "green";
                    document.getElementById("movieDetailStatus").innerHTML = data.message;
                    loadDetails();
                    return;
                }
                else
                {
                    document.getElementById("movieDetailStatus").style.color = "red";
                    document.getElementById("movieDetailStatus").innerHTML = data.message;
                }
            })
        }, $.cookie("delay"))
    })
}

function addToCart() {
    var cells = event.target.parentNode.parentNode.getElementsByTagName("td");
    var quantity = cells[1].childNodes[0].childNodes[0].value;


    _data = {
        "email": $.cookie("email"),
        "movieId": movieId,
        "quantity": quantity
    };

    console.log(_data);

    $.ajax({
        method: "POST", // Declare request type
        url: GW_ENDPOINT + "/billing/cart/insert",
        headers: {
            "Content-Type": "application/json",
            "sessionID": $.cookie("sessionID"),
            "email": $.cookie("email")
        },
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify(_data)
    }).done(function (data, status, xhr) {
        var transactionID = xhr.getResponseHeader('transactionID');
        var delay = xhr.getResponseHeader('delay');

        $.cookie("transactionID", transactionID);
        $.cookie("delay", delay);

        console.log("TransactionID: " + $.cookie("transactionID"));
        console.log("delay: " + delay);

        setTimeout(function () {
            $.ajax({
                method: "GET", // Declare request type
                url: GW_ENDPOINT + "/report",
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "*/*",
                    'email': $.cookie("email"),
                    'transactionID': $.cookie('transactionID'),
                    'sessionID': $.cookie('sessionID')
                }

            }).done(function (data, status) {
                console.log(data);

                if (status !== "success") {
                    document.getElementById("movieDetailStatus").style.color = "red";
                    document.getElementById("movieDetailStatus").innerHTML = data.message;
                    return false;
                }

                if (data.resultCode = 3100)
                {
                    document.getElementById("movieDetailStatus").style.color = "green";
                }
                else
                {
                    document.getElementById("movieDetailStatus").style.color = "red";
                }
                document.getElementById("movieDetailStatus").innerHTML = data.message;
            })
        }, $.cookie("delay"))
    })
}

function goBackListing() {
    activateTab("movieListPage");
}