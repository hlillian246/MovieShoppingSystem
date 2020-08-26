function CheckOut() {
    activateTab("checkout");
    _data = {
        "email": $.cookie("email")
    };

    $.ajax({
        method: "POST", // Declare request type
        url: GW_ENDPOINT + "/billing/cart/retrieve",
        headers: {
            "Content-Type": "application/json",
            "SessionID": $.cookie("sessionID"),
            "email": $.cookie("email")
        },
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify(_data)
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
                document.getElementById("cartStatus").style.color = "red";
                document.getElementById("cartStatus").innerHTML = message;
            }

            return false;
        }

        var transactionID = xhr.getResponseHeader('transactionID');
        var delay = xhr.getResponseHeader('delay');

        $.cookie("transactionID", transactionID);
        $.cookie("delay", delay);

        console.log("TransactionID: " + $.cookie("transactionID"));
        console.log("delay: " + delay);


        fetchCheckOutReport(0);
    })
    .fail(function (xhr, exception) {
        console.log(xhr);
        console.log(exception);
        var message = xhr.responseJSON ? xhr.responseJSON.message : "Oops! Something went wrong!";
        document.getElementById("cartStatus").style.color = "red";
        document.getElementById("cartStatus").innerHTML = message;
    });
}


function fetchCheckOutReport(count) {
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

            if (xhr.status == 204) {
                if (count >= MAX_REPORT_FETCH_TRIES) {
                    document.getElementById("cartStatus").style.color = "red";
                    document.getElementById("cartStatus").innerHTML = "Timeout. Unable to fetch check out result.";
                    return false;
                } else {
                    fetchCheckOutReport(count + 1);
                }
            } else if (xhr.status == 200) {
                if (status !== "success") {
                    console.log(xhr);

                    var message = xhr.responseJSON ? xhr.responseJSON.message : "Oops! Something went wrong!";
                    document.getElementById("cartStatus").style.color = "red";
                    document.getElementById("cartStatus").innerHTML = message;
                    return false;
                }

                if (data) {
                    console.log(data);
                    if (data.resultCode !== 3130) {
                        document.getElementById("cartStatus").style.color = "red";
                        document.getElementById("cartStatus").innerHTML = data.message;
                        return
                    }

                    if (data.hasOwnProperty("items")) {

                        var html = '<table class="centerTable4" align="center" border="0" cellpadding="5">'
                        html += '<tr><td align="center"><h3>Checkout Items</h3></td></tr>'
                        html += '<tr><td>'
                        html += '<table class="centerTable4" align="center" border="1"><tr><td align="center"><b>Movie ID</b></td>';
                        html += '<td align="center"><b>Quantity</b></td>';
                        for (var i = 0; i < data.items.length; ++i) {
                            var movieObject = data.items[i];
                            html += "<tr>";
                            html += '<td align="center">' + movieObject["movieId"] + '</td>';
                            html += '<td align="center">' + movieObject["quantity"] + '</td>';
                            html += "</tr>";
                        }
                        html += "</table>";
                        html += '</td></tr>';

                        html += '<tr><td></td></tr>';
                        html += '<tr><td align="center"><p> <button id="PlaceOrder" type="submit" class="submitbtn" onclick="placeOrder()">Place Order</button></td></tr>';
                        html += '</table>'

                        document.getElementById("checkoutDetails").innerHTML = html;
                    } else {
                        var html = "<h2>Checkout Items</h2>";
                        html += data.message;
                        document.getElementById("cartStatus").innerHTML = html;
                    }
                }
                else
                {
                    document.getElementById("cartStatus").style.color = "red";
                    document.getElementById("cartStatus").innerHTML = "No response data received";
                }
            } else {
                console.log(xhr);
                var message = xhr.responseJSON ? xhr.responseJSON.message : "Oops! Something went wrong!";
                document.getElementById("cartStatus").style.color = "red";
                document.getElementById("cartStatus").innerHTML = message;
                return false;
            }
        })
            .fail(function (xhr, exception) {
                console.log(xhr);
                console.log(exception);
                var message = xhr.responseJSON ? xhr.responseJSON.message : "Oops! Something went wrong!";
                document.getElementById("cartStatus").style.color = "red";
                document.getElementById("cartStatus").innerHTML = message;
            });
    }, $.cookie("delay"));
}
