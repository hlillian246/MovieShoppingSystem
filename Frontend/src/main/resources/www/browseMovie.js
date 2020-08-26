var title=undefined;
var genre=undefined;

function createDropdown() {
    console.log("This is a test")

    title=undefined;
    genre=undefined;

    _data = {
        "email": $.cookie("email")
    };
    console.log(_data)

    $.ajax({
        method: "GET", // Declare request type
        url: GW_ENDPOINT + "/movies/genre",
        headers: {
            "Content-Type": "application/json",
            "sessionID": $.cookie("sessionID"),
            "email": $.cookie("email")
        },
        contentType: "application/json",
        dataType: "json",
    }).done(function (data, status, xhr) {

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
                document.getElementById("browseMovieStatus").style.color = "red";
                document.getElementById("browseMovieStatus").innerHTML = message;
            }
            return false;
        }

        var transactionID = xhr.getResponseHeader('transactionID');
        var delay = xhr.getResponseHeader('delay');

        $.cookie("transactionID", transactionID);
        $.cookie("delay", delay);

        console.log("TransactionID: " + $.cookie("transactionID"));
        console.log("delay: " + delay);

        fetchGenreReport(0);
    })
    .fail(function (xhr, exception, data) {
        console.log(xhr);
        console.log(exception);
        var message = xhr.responseJSON ? xhr.responseJSON.message : "Oops! Something went wrong!";
        document.getElementById("browseMovieStatus").style.color = "red";
        document.getElementById("browseMovieStatus").innerHTML = message;
    });
}

function fetchGenreReport(count)
{
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
            if (xhr.status == 204) {
                if (count >= MAX_REPORT_FETCH_TRIES)
                {
                    document.getElementById("browseMovieStatus").style.color = "red";
                    document.getElementById("browseMovieStatus").innerHTML = "Timeout. Unable to fetch search result.";
                    return false;
                }
                else
                {
                    fetchGenreReport(count+1);
                }
            }
            else if (xhr.status == 200) {
                if (status !== "success") {
                    console.log(xhr);

                    var message = xhr.responseJSON ? xhr.responseJSON.message : "Oops! Something went wrong!";
                    document.getElementById("browseMovieStatus").style.color = "red";
                    document.getElementById("browseMovieStatus").innerHTML = message;
                    return false;
                }

                if (data) {
                    console.log(data);
                    if (data.resultCode != 219) {
                        document.getElementById("browseMovieStatus").style.color = "red";
                        document.getElementById("browseMovieStatus").innerHTML = data.message;
                        return false;
                    }

                    var html = '<h3>Choose Browse Criteria</h3>';
                    document.getElementById("browseTitle").innerHTML = html;


                    html = '<table cellpadding="0"><tr><td>';

                    html += '<div class="dropdown"><button class="dropbtn">Title</button><div class="dropdown-content">';
                    for (let i = 0; i < 26; i++) {
                        html += '<a href="#" onclick="titleSelected(event)">' + String.fromCharCode(65 + i) + '</a>';
                    }
                    html += "</div></div></td>"

                    html += '<td><div id="browseTitleShowMe" class="div">None</div></td></tr></table>';

                    document.getElementById("letterDropdown").innerHTML = html;


                    html = '<table cellpadding="0"><tr><td>';


                    var genres = data.genres;
                    html += '<div class="dropdown"><button class="dropbtn">Genres</button><div class="dropdown-content">';
                    for (let i = 0; i < genres.length; i++) {
                        html += '<a href="#" onclick="genreSelected(event)">' + genres[i].name + '</a>';
                    }
                    html += "</div></div></td>"

                    html += '<td><div id="browseGenreShowMe" class="div">None</div></td></tr></table>';


                    document.getElementById("genreDropdown").innerHTML = html;


                    document.getElementById("orderBySection").innerHTML = generateOrderByText();
                    document.getElementById("directionSection").innerHTML = generateDirectionText();
                    document.getElementById("limitDropdown").innerHTML = generatePageLimit();

                    document.getElementById("hiddenSubmit").innerHTML = generateHiddenSubmit()

                    $.cookie('limit', 10);

                    clearMessages();
                } else {
                    console.log("NO DATA");
                    document.getElementById("browseMovieStatus").style.color = "red";
                    document.getElementById("browseMovieStatus").innerHTML = "No response data received";
                }
            }
            else
            {
                console.log(xhr);
                var message = xhr.responseJSON ? xhr.responseJSON.message : "Oops! Something went wrong!";
                document.getElementById("browseMovieStatus").style.color = "red";
                document.getElementById("browseMovieStatus").innerHTML = message;
                return false;
            }
        })
            .fail(function (xhr, exception) {
                console.log(xhr);
                console.log(exception);
                var message = xhr.responseJSON ? xhr.responseJSON.message : "Oops! Something went wrong!";
                document.getElementById("browseMovieStatus").style.color = "red";
                document.getElementById("browseMovieStatus").innerHTML = message;
            });
    }, $.cookie("delay"));
}


function generateOrderByText()
{
    var html = '<div><b>Order By</b></div>';
    html += '<form id="browseSearchOrderby">';
    html += '<input id="browseSeachOrderRating" type="radio" name="orderby" value="rating" checked="checked">';
    html += '<label for="browseSeachOrderRating">Rating</label><br>';
    html += '<input id="browseSeachOrderTitle" type="radio" name="orderby" value="title">';
    html += '<label for="browseSeachOrderTitle">Title</label>';
    html += '</form>';

    return html;
}

function generateDirectionText() {
    var html = '<div><b>Direction</b></div>';
    html += '<form id="browseSearchDirection">';
    html += '<input id="browseSearchDirectionDesc" type="radio" name="orderby" value="desc" checked="checked">';
    html += '<label for="browseSearchDirectionDesc">DESC</label><br>';
    html += '<input id="browseSearchDirectionAsc" type="radio" name="orderby" value="asc">';
    html += '<label for="browseSearchDirectionAsc">ASC</label>';
    html += '</form>';

    return html;
}

function generatePageLimit() {

    var html = '<table cellpadding="1"><tr><td>';
    html += '<div class="dropdown"><button class="dropbtn1">Page Limit</button><div class="dropdown-content">';
    html += '<a href="#" onclick="setBrowsePageLimit(event)">10</a><a href="#" onclick="setBrowsePageLimit(event)">25</a>';
    html += '<a href="#" onclick="setBrowsePageLimit(event)">50</a><a href="#" onclick="setBrowsePageLimit(event)">100</a></div></div>';
    html += '</td><td><div id="browseShowMe" class="divd">10</div></td></tr></table>';

    return html;
}

function generateHiddenSubmit()
{
    var html ='<td></td><td></td>';
    html += '<td align="center">';
    html += '<button type="submit" class="submitbtn" onclick="browseSearchStart()">Submit</button>';
    html += '</td><td></td><td></td>';

    return html;
}

function browseSearchStart()
{
    if (!title && !genre)
    {
        document.getElementById("browseMovieStatus").style.color = "red";
        document.getElementById("browseMovieStatus").innerHTML = "Please select a title or a genre.";
        return;
    }

    if (title)
    {
        console.log("Title selected: " + title);
        startTitleSearch(title);
    }
    else
    {
        console.log("Genre selected: " + genre);
        startGenreSearch(genre);
    }
}


function titleSelected(event) {
    title = event.target.innerText;
    genre = undefined;

    console.log("T: " + title)
    document.getElementById("browseMovieStatus").innerHTML = "";

    var hiddenDiv = document.getElementById("browseTitleShowMe");
    hiddenDiv.innerText = title;
    hiddenDiv.style.display = "block";

}

function genreSelected(event) {
    genre = event.target.innerText;
    titl = undefined;

    console.log("G: " + genre)
    document.getElementById("browseMovieStatus").innerHTML = "";

    var hiddenDiv = document.getElementById("browseGenreShowMe");
    hiddenDiv.innerText = genre;
    hiddenDiv.style.display = "block";

}
