function loadOrderHistory()
{
    let MAX_PULL_COUNT = 10;
    console.log("This is a test")
    _data = {
        "email": $.cookie("email")
    };
    console.log("Email: " + $.cookie("email"))
    console.log(_data)
    var url = GW_ENDPOINT + "/billing/order/retrieve?email=" + $.cookie("email")
    $.ajax({
        method: "POST", // Declare request type
        url: url,
        headers: {
            "Content-Type": "application/json",
            "sessionID": $.cookie("sessionID")
        },
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify(_data)
    }).done(function (data, status, xhr) {
        console.log(xhr);
        console.log(status);
        console.log(data);

        console.log("status: " + xhr.status);

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
                document.getElementById("historyStatus").style.color = "red";
                document.getElementById("historyStatus").innerHTML = message;
            }

            return false;
        }

        var transactionID = xhr.getResponseHeader('transactionID');
        var delay = xhr.getResponseHeader('delay');

        $.cookie("transactionID", transactionID);
        $.cookie("delay", delay);

        console.log("TransactionID: " + $.cookie("transactionID"));
        console.log("delay: " + delay);

        console.log("delay: " + delay);

        fetchHistoryReport(0);
    })

    .fail(function (xhr, exception, data) {
        console.log(xhr);
        console.log(exception);
        console.log(data);
        var message = xhr.responseJSON ? xhr.responseJSON.message : "Oops! Something went wrong!";
        document.getElementById("historyStatus").style.color = "red";
        document.getElementById("historyStatus").innerHTML = message;
    });
}

function fetchHistoryReport(count) {
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
            },
        }).done(function (data, status, xhr) {
            console.log(xhr);
            console.log(status);
            console.log(data);

            if (xhr.status == 204) {
                if (count >= MAX_REPORT_FETCH_TRIES) {
                    document.getElementById("historyStatus").style.color = "red";
                    document.getElementById("historyStatus").innerHTML = "Timeout. Unable to fetch search result.";
                    return false;
                } else {
                    fetchHistoryReport(count + 1);
                }
            } else if (xhr.status == 200) {
                if (data) {
                    console.log("Data: " + data);


                    if (data.resultCode != 3410) {
                        document.getElementById("historyStatus").style.color = "red";
                        document.getElementById("historyStatus").innerHTML = data.message;
                        return
                    }

                    var html = '<div class="divc"><h3>Transaction History</h3></div>';
                    html += "<br>";

                    html += '<table class="centerTable5" align="center" border="1"><tr><td><b>Transaction</b></td>';
                    html += "<td><b>State</b></td><td><b>Amount</b></td><td><b>Items</b></td><td><b>Sale Date</b></td>";
                    for (let i = 0; i < data.transactions.length; ++i) {
                        let transaction = data.transactions[i];
                        html += "<tr>";
                        html += "<td>" + transaction["transactionId"] + "</td>";
                        html += "<td>" + transaction["state"] + "</td>";
                        html += "<td>" + transaction["amount"]["total"] + " " + transaction["amount"]["currency"] + "</td>"
                        html += "<td>";
                        var saleDate = "";

                        var inner = "<table border=0 cellpadding='3'><tr><td><b>Item</b></td><td><b>Quantity</b></td><td><b>Unit Price</b></td><td><b>Discount</b></td></tr>";

                        for (let j = 0; j < transaction["items"].length; j++) {
                            let item = transaction["items"][j];
                            saleDate = item.saleDate;

                            inner += "<tr>"
                            inner += "<td>" + item.movieId + "</td>"
                            inner += "<td align='center'>" + item.quantity + "</td>"
                            inner += "<td>" + item.unit_price + "</td>"
                            inner += "<td>" + item.discount + "</td>"
                            inner += "</tr>";
                        }

                        inner += "</table>";
                        html += inner;
                        html += "</td>";
                        html += "<td>" + saleDate + "</td>"
                        html += "</tr>";
                    }

                    html += "</table>";

                    document.getElementById("historyDetails").innerHTML = html;
                    clearMessages();
                    return;
                }
                else
                {
                    document.getElementById("historyStatus").style.color = "red";
                    document.getElementById("historyStatus").innerHTML = "No data received";
                }
            } else {
                console.log(xhr);
                var message = xhr.responseJSON ? xhr.responseJSON.message : "Oops! Something went wrong!";
                document.getElementById("historyStatus").style.color = "red";
                document.getElementById("historyStatus").innerHTML = message;
                return false;
            }
        })
        .fail(function (xhr, exception, data) {
            console.log(xhr);
            console.log(exception);
            console.log(data);
            var message = xhr.responseJSON ? xhr.responseJSON.message : "Oops! Something went wrong!";
            document.getElementById("historyStatus").style.color = "red";
            document.getElementById("historyStatus").innerHTML = message;
            return false;
        });
    }, $.cookie("delay"));
}