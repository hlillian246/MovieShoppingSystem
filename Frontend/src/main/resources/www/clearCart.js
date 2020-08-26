function clearCart(){
    event.preventDefault();
    _data = {
        "email": $.cookie("email")
    };

    console.log(_data);

    $.ajax({
        method: "POST", // Declare request type
        url: GW_ENDPOINT + "/billing/cart/clear",
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

        fetchClearCartReport(0);
    })
    .fail(function (xhr, exception, data) {
        console.log(xhr);
        console.log(exception);
        var message = xhr.responseJSON ? xhr.responseJSON.message : "Oops! Something went wrong!";
        document.getElementById("cartStatus").style.color = "red";
        document.getElementById("cartStatus").innerHTML = message;
    });
}

function fetchClearCartReport(count) {
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
                    document.getElementById("cartStatus").innerHTML = "Timeout. Unable to fetch clear cart report result.";
                    return false;
                } else {
                    fetchClearCartReport(count + 1);
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
                    if (data.resultCode == 3140) {
                        document.getElementById("cartStatus").style.color = "green";
                    } else {
                        document.getElementById("cartStatus").style.color = "red";
                    }
                    document.getElementById("cartDetails").innerHTML = "";
                    document.getElementById("cartStatus").innerHTML = data.message;
                } else {
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
