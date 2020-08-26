function setSearchPageLimit(event)
{
    var limit = event.target.innerText;
    console.log(limit);
    $.cookie("limit", limit);
    $.cookie("offset", 0);

    var hiddenDiv = document.getElementById("showMe");
    hiddenDiv.innerText = limit;
    hiddenDiv.style.display = "block";
}

function setAdvSearchPageLimit(event)
{
    var limit = event.target.innerText;
    console.log(limit);
    $.cookie("limit", limit);

    $.cookie("offset", 0);

    var hiddenDiv = document.getElementById("advShowMe");
    hiddenDiv.innerText = limit;
    hiddenDiv.style.display = "block";
}

function setBrowsePageLimit(event)
{
    var limit = event.target.innerText;
    console.log(limit);
    $.cookie("limit", limit);

    $.cookie("offset", 0);

    var hiddenDiv = document.getElementById("browseShowMe");
    hiddenDiv.innerText = limit;
    hiddenDiv.style.display = "block";
}

function setListPageLimit(event)
{
    var limit = event.target.innerText;
    console.log(limit);
    $.cookie("limit", limit);

    var hiddenDiv = document.getElementById("listShowMe");
    hiddenDiv.innerText = limit;
    hiddenDiv.style.display = "block";

    var type = $.cookie('search_type');

    if (type === 'title')
    {
        searchTitle();
    }
    else if (type === 'advance')
    {
        advSearch();
    }
    else if (type === 'title_browse')
    {
        titleSelect();
    }
    else if (type === 'genre_browse')
    {
        genreSelect();
    }

}



function resetLimit()
{
    $.cookie("limit", 10);
    $.cookie("page", 1);
    $.cookie("offset", 0);
}


function getSearchOrderBy()
{
    if (document.getElementById('searchOrderby')) {
        var orderby = document.getElementById('searchOrderby');
        for (var i=0, length=orderby.length; i<length; i++) {
            if (orderby[i].checked) {
                console.log("Set orderby: " + orderby[i].value);
                return orderby[i].value;
            }
        }
    }
}

function getSearchDirection()
{
    if (document.getElementById('searchDirection')) {
        var direction = document.getElementById('searchDirection');
        for (var i=0, length=direction.length; i<length; i++) {
            if (direction[i].checked) {
                console.log("Set direction: " + direction[i].value);
                return direction[i].value;
            }
        }
    }
}

function getAdvSearchOrderBy()
{
    if (document.getElementById('advSearchOrderby')) {
        var orderby = document.getElementById('advSearchOrderby');
        for (var i=0, length=orderby.length; i<length; i++) {
            if (orderby[i].checked) {
                console.log("Set orderby: " + orderby[i].value);
                return orderby[i].value;
            }
        }
    }
}

function getAdvSearchDirection()
{
    if (document.getElementById('advSearchDirection')) {
        var direction = document.getElementById('advSearchDirection');
        for (var i=0, length=direction.length; i<length; i++) {
            if (direction[i].checked) {
                console.log("Set direction: " + direction[i].value);
                return direction[i].value;
            }
        }
    }
}

function getBrowseOrderBy()
{
    if (document.getElementById('browseSearchOrderby')) {
        var orderby = document.getElementById('browseSearchOrderby');
        for (var i=0, length=orderby.length; i<length; i++) {
            if (orderby[i].checked) {
                console.log("Set orderby: " + orderby[i].value);
                return orderby[i].value;
            }
        }
    }
}

function getBrowseDirection()
{
    if (document.getElementById('browseSearchDirection')) {
        var direction = document.getElementById('browseSearchDirection');
        for (var i=0, length=direction.length; i<length; i++) {
            if (direction[i].checked) {
                console.log("Set direction: " + direction[i].value);
                return direction[i].value;
            }
        }
    }
}

function removeCookies() {
    var res = document.cookie;
    var multiple = res.split(";");
    for(var i = 0; i < multiple.length; i++) {
        var key = multiple[i].split("=");
        document.cookie = key[0]+" =; expires = Thu, 01 Jan 1970 00:00:00 UTC";
    }
}


