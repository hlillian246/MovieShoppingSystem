function loadCart() {
//    clearMessages();
    _data = {
        "email": $.cookie("email")
    };
    console.log(_data)
    $.ajax({
        method: "POST", // Declare request type
        url: GW_ENDPOINT + "/billing/cart/retrieve",
        headers: {
            "Content-Type": "application/json",
            "sessionID": $.cookie("sessionID"),
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

        fecthCartReport(0);
    })
    .fail(function (xhr, exception, data) {
        console.log(xhr);
        console.log(exception);
        var message = xhr.responseJSON ? xhr.responseJSON.message : "Oops! Something went wrong!";
        document.getElementById("cartStatus").style.color = "red";
        document.getElementById("cartStatus").innerHTML = message;
    });
}

function fecthCartReport(count)
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
                if (count >= MAX_REPORT_FETCH_TRIES) {
                    document.getElementById("cartStatus").style.color = "red";
                    document.getElementById("cartStatus").innerHTML = "Timeout. Unable to fetch search result.";
                    return false;
                } else {
                    fecthCartReport(count + 1);
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
                    if (data.resultCode != 3130) {
                        if (data.resultCode == 312) {
                            document.getElementById("cartDetails").innerHTML = "";
                        }
                        document.getElementById("cartStatus").style.color = "red";
                        document.getElementById("cartStatus").innerHTML = data.message;
                        return
                    }

                    var style = document.getElementById("cartStatus").style.color;
                    var old = document.getElementById("cartStatus").innerHTML;

                    console.log("old: " + old)

                    if (data.hasOwnProperty("items")) {

                        var html = '<table class="centerTable3" align="center" border="0" cellpadding="5">'
                        html += '<tr><td align="center"><h2>My Shopping Cart</h2></td></tr>'
                        html += '<tr><td>'
                        html += '<table class="centerTable3" align="center" border="1"><tr><td align="center"><b>Movie ID</b></td>';
                        html += '<td align="center"><b>Quantity</b></td><td align="center"><b>Action</b></td>';
                        for (let i = 0; i < data.items.length; ++i) {
                            let movieObject = data.items[i];
                            html += "<tr>";
                            html += '<td align="center">' + movieObject["movieId"] + '</td>';
                            //                            html += "<td>" + movieObject["quantity"] + "</td>";

                            html += '<td style="text-align:center"> <div class="quantity" align="center"><input class="quantity" type="number" min=1 value=' + movieObject["quantity"] + '></div></td>';

                            html += '<td align="center"> <button type="submit" class="submitbtn" onclick="cartUpdate(event)">Update</button> ' +
                                '<button type="submit" class="submitbtn" onclick="cartDelete(event)">Delete</button></td>';
                            html += "</tr>";
                        }
                        html += "</table>";
                        html += '</td></tr>';

                        html += '<tr><td><table class="centerTable3" align="center" border="0" cellpadding="10"></td></tr>'
                        html += '<tr><td align="center"><p> <button id="CheckOut" type="submit" class="submitbtn" onclick="CheckOut()">Check Out</button></td>';

                        html += '<td  align="center"><button id="clearCart" type="submit" class="submitbtn" onclick = "clearCart()" >Clear Cart</button></td></tr>';
                        html += '</table></table>'

                        document.getElementById("cartDetails").innerHTML = html;

                        document.getElementById("cartStatus").style.color = style;
                        document.getElementById("cartStatus").innerHTML = old;
                    } else {
                        var html = "<h2>My Shopping Cart</h2>";
                        html += data.message;
                        document.getElementById("cartDetails").innerHTML = html;
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

