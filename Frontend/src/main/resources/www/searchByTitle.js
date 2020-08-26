function searchTitle() {
    event.preventDefault(); // Prevent the default form submit event, using ajax instead

    let title = $("#title").val() // Extract data from search input box to be the title argument

    console.log("offset: " + $.cookie('offset'));

    var limit = parseInt($.cookie('limit'));
    var page = parseInt($.cookie('page'));
    var offset = page * limit;

    var orderby = getSearchOrderBy();
    var direction = getSearchDirection();

    console.log(offset);
    console.log(limit);
    console.log(orderby);
    console.log(direction);

    var params = [];
    if (title) params["title"] = title;
    params["offset"] = offset;
    params["limit"] = limit;
    params["orderby"] = orderby;
    params["direction"] = direction;

    var queryString = Object.keys(params).map(function(key) {
        return key + '=' + params[key]
    }).join('&');

    var url = GW_ENDPOINT + "/movies/search?" + queryString;
    console.log("URL: " + url)

    var delay = $.cookie("delay")
    $.ajax({
        method: "GET", // Declare request type
        headers: {
            "Content-Type": "application/json",
            "SessionID": $.cookie("sessionID"),
            "email": $.cookie("email")
        },
        url: url
    }).done(function (data, status, xhr) {
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
                document.getElementById("searchStatus").style.color = "red";
                document.getElementById("searchStatus").innerHTML = message;
            }

            return false;
        }

        var transactionID = xhr.getResponseHeader('transactionID');
        var delay = xhr.getResponseHeader('delay');

        $.cookie("transactionID", transactionID);
        $.cookie("delay", delay);

        console.log("SessionID: " + $.cookie("sessionID"));
        console.log("email: " + $.cookie("email"))
        console.log("delay: " + $.cookie("delay"))

        fetchSearchTitleReport(0);
    })
    .fail(function (xhr, exception, data) {
        console.log(xhr);
        console.log(exception);
        console.log(data);
        var message = xhr.responseJSON ? xhr.responseJSON.message : "Oops! Something went wrong!";
        document.getElementById("searchStatus").style.color = "red";
        document.getElementById("searchStatus").innerHTML = message;
    });
}


function fetchSearchTitleReport(count) {
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

        }).done(function (data,status, xhr) {

            if (xhr.status == 204) {
                if (count >= MAX_REPORT_FETCH_TRIES)
                {
                    document.getElementById("searchStatus").style.color = "red";
                    document.getElementById("searchStatus").innerHTML = "Timeout. Unable to fetch search result.";
                    return false;
                }
                else
                {
                    fetchSearchTitleReport(count+1);
                }
            }
            else if (xhr.status == 200) {
                if (status !== "success")
                {
                    console.log(xhr);

                    var message = xhr.responseJSON ? xhr.responseJSON.message : "Oops! Something went wrong!";
                    document.getElementById("searchStatus").style.color = "red";
                    document.getElementById("searchStatus").innerHTML = message;
                    return false;
                }

                if (data) {
                    console.log(data);

                    if (data.resultCode !== 210) {
                        document.getElementById("searchStatus").style.color = "red";
                        document.getElementById("searchStatus").innerHTML = data.message;
                        return false;
                    }

                    var result = '<div class="divc"><h2>Movie Search Results</h2></div>';

                    result += '<table class="centerTable5" align="center" border="0" cellpadding="2">';
                    result += '<tr><td>';

                    result += '<table class="centerTable3" align="center" border="1" cellpadding="2">';
                    result += '<tr><td><b>Movie ID</b></td>';
                    result += '<td><b>Title</b></td>';
                    result += '<td><b>Director</b></td>';
                    result += '<td><b>Year</b></td>';
                    result += '<td><b>Ratings</b></td></tr>';

                    for (let i = 0; i < data.movies.length; ++i) {
                        let movieObject = data.movies[i];
                        result += "<tr>";
                        result += '<td><a href="#" onclick="movieDetails()">' + movieObject["movieId"] + "</a></td>";
                        result += "<td>" + movieObject["title"] + "</td>";
                        result += "<td>" + movieObject["director"] + "</td>";
                        result += "<td>" + movieObject["year"] + "</td>";
                        var rating = movieObject.hasOwnProperty("rating") ? movieObject["rating"] : "";
                        result += "<td>" + rating + "</td>";
                        result += "</tr>";
                    }

                    result += "</table>";

                    result += '</td></tr><tr><td align="center">';


                    var offset = parseInt($.cookie('offset'));
                    var limit = parseInt($.cookie('limit'));
                    var page = parseInt($.cookie('page'));
//                    var current = offset / limit;

                    // console.log(offset)
                    // console.log(limit)
                    // console.log(current)

                    result += '<table class="centerTable3" align="center" border="0" cellpadding="10">';
                    result += '<tr><td valign="top">';

//                    result += '<br><div class="divc">';
                    result += '<table class="centerTable5" align="center" border="0" cellpadding="10">';

                    if (page === 1) {
                        result += '<tr><td>' + page + '</td><td><a href="#next" onclick="searchNextTitle()">Next</a></td>';
                    } else {
                        result += '<tr><td><a href="#prev" onclick="searchPrevTitle()">Prev</a></td>' +
                            '<td>' + page + '</td>' +
                            '<td><a href="#next" onclick="searchNextTitle()">Next</a></td>';
                    }
                    result += '</tr></table>';

                    result += '</td><td valign="top">';
                    result += '<table cellpadding="2" align="center" border="0">';
                    result += '<tr><td><div class="dropdown"><button class="dropbtn1">Limit</button>';
                    result += '<div id="searchPageLimit" class="dropdown-content">';
                    result +=  '<a href="#" onclick="setListPageLimit(event)">10</a>';
                    result +=  '<a href="#" onclick="setListPageLimit(event)">25</a>';
                    result +=  '<a href="#" onclick="setListPageLimit(event)">50</a>';
                    result +=  '<a href="#" onclick="setListPageLimit(event)">100</a>';
                    result +=  '</div></div></td><td><div id="listShowMe" class="divd">' + limit + '</div></td>';
                    result += '</tr></table>';
                    result += '</td></tr></table>';


                    result += '</td></tr></table>';

                    clearMessages();
                    document.getElementById("movieListPage").innerHTML = result;
                    activateTab("movieListPage");

                } else {
                    document.getElementById("searchStatus").style.color = "red";
                    document.getElementById("searchStatus").innerHTML = "No response data received";
                }
            }
            else
            {
                console.log(xhr);
                var message = xhr.responseJSON ? xhr.responseJSON.message : "Oops! Something went wrong!";
                document.getElementById("searchStatus").style.color = "red";
                document.getElementById("searchStatus").innerHTML = message;
                return false;
            }
        })
        .fail(function (xhr, exception) {
            console.log(xhr);
            console.log(exception);
            var message = xhr.responseJSON ? xhr.responseJSON.message : "Oops! Something went wrong!";
            document.getElementById("searchStatus").style.color = "red";
            document.getElementById("searchStatus").innerHTML = message;
        });
    }, $.cookie("delay"));
}


function searchStartTitle() {
    //
    // var orderby = getOrderBy();
    // console.log("Got: " + orderby);
    //
    // var direction = getDirection();
    // console.log("Got: " + direction);
    //
    //
    $.cookie('offset', 0);
    $.cookie('page', 1);
    if (!$.cookie('limit')) {
        $.cookie('limit', 10);
    }

    $.cookie('search_type', 'title');

    searchTitle();
}


function searchNextTitle() {
    var offset = parseInt($.cookie('offset'));
    var limit = parseInt($.cookie('limit'));
    var page = parseInt($.cookie('page'));

    $.cookie('offset', offset + limit);
    $.cookie('page', page+1);
    searchTitle();
}

function searchPrevTitle() {
    var offset = parseInt($.cookie('offset'));
    var limit = parseInt($.cookie('limit'));
    var page = parseInt($.cookie('page'));

    $.cookie('offset', offset - limit);
    $.cookie('page', page-1);

    searchTitle();
}
