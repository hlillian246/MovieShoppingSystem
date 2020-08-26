function limitSelect(event) {
    var limit = event.target.innerText;
    console.log(limit);
    $.cookie("limit", limit);
    $.cookie("offset", 0);
}
