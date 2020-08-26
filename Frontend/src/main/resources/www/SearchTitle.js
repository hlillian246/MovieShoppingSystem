$('form[id="titleForm"]').submit(function (event) {

    event.preventDefault(); // Prevent the default form submit event, using ajax instead
    let title = $("#title").val() // Extract data from search input box to be the title argument

    let offset = parseInt($.cookie('offset'));
    let limit = parseInt($.cookie('limit'));
    console.log(title);		    console.log(title);
    console.log(offset);
    console.log(limit);
    var delay = $.cookie("delay")
    $.ajax({
        method: "GET", // Declare request type
        headers: {
            "Content-Type": "application/json",
            "SessionID": $.cookie("sessionID"),
            "email": $.cookie("email")
        },
        url: GW_ENDPOINT + "/movies/search?title=" + title + "?offset=" + offset + "?limit=" + limit
    }).done(function (data, status, xhr) {

        console.log(data);
        console.log(status);
        console.log(xhr);


        if (xhr.status === 200) {
            let message = data ? data.message : "Oops! Something went wrong";

            document.getElementById("searchStatus").style.color = "red";
            document.getElementById("searchStatus").innerHTML = message;
            return false;
        }

        if (xhr.status !== 204) {
            let message = data ? data.message : "Oops! This is not expected";

            document.getElementById("searchStatus").style.color = "red";
            document.getElementById("searchStatus").innerHTML = message;
            return false;
        }

        console.log(delay);
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

            }).done(function (data,status) {

                if (status !== "success") {
                    return false;
                }


                if (data) {

                    if(data.resultCode !== 210)
                    {
                        document.getElementById("searchStatus").innerHTML = data.message;
                        return false;
                    }

                    console.log(data);
                    console.log(data.movies[0]);

                    var result = "<h2>Movie Search Results</h2>";


                    result += '<table border="1" cellpadding="5"><tr><td><b>Movie ID</b></td>';
                    result += '<td><b>Title</b></td>';
                    result += '<td><b>Director</b></td>';
                    result += '<td><b>Year</b></td>';
                    result +=  '<td><b>Ratings</b></td>';


                    for (let i = 0; i < data.movies.length; ++i) {
                        let movieObject = data.movies[i];
                        result += "<tr>";
                        result += "<td>" + movieObject["movieId"] + "</td>";
                        result += "<td>" + movieObject["title"] + "</td>";
                        result += "<td>" + movieObject["director"] + "</td>";
                        result += "<td>" + movieObject["year"] + "</td>";
                        var rating = movieObject.hasOwnProperty("rating") ? movieObject["rating"] : "";
                        result += "<td>" + rating + "</td>";
                        result += "</tr>";
                    }
                    result += "</table>";

                    var offset = parseInt($.cookie('offset'));
                    var limit = parseInt($.cookie('limit'));
                    var current = offset / limit;
                    console.log(offset)
                    console.log(limit)
                    console.log(current)


                    result += '<br><br>';

                    if (current === 0) {
                        result += '<table border="0" cellpadding="5"><tr><td>' + current +
                            '</td><td><a href="#next" onclick="searchNext()">Next</a></td></tr></table>';
                    }
                    else
                    {
                        result += '<table border="0" cellpadding="5"><tr><td>' +
                            '<a href="#prev" onclick="searchPrev()">Prev</a></td>' +
                            '<td>' + current + '</td>' +
                            '<td><a href="#next" onclick="searchNext()">Next</a></td></tr></table>';
                    }

                    clearMessages();

                    document.getElementById("movieListPage").innerHTML = result;
                    activateTab("movieListPage");

                } else {
                    message = "No response data received"
                }

            })
            .fail(function (xhr, exception, data) {
                console.log(xhr);
                console.log(exception);
                console.log(data);
                var message = xhr.responseJSON ? xhr.responseJSON.message : "Oops! Something went wrong!";
                document.getElementById("searchStatus").style.color = "red";
                document.getElementById("searchStatus").innerHTML = message;
            });
        }, $.cookie("delay"))

    })
    .fail(function (xhr, exception, data) {
        console.log(xhr);
        console.log(exception);
        console.log(data);
        var message = xhr.responseJSON ? xhr.responseJSON.message : "Oops! Something went wrong!";
        document.getElementById("searchStatus").style.color = "red";
        document.getElementById("searchStatus").innerHTML = message;
    });
});