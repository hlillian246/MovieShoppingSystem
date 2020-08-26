function placeOrder(){
    clearMessages();

    _data = {
        "email": $.cookie("email")
    };
    $.ajax({
            method: "POST", // Declare request type
            url: GW_ENDPOINT + "/billing/order/place",
            headers:{
                "Content-Type":"application/json",
                "SessionID": $.cookie("sessionID"),
                "email": $.cookie("email")
            },
            contentType: "application/json",
            dataType: "json",
            data:JSON.stringify(_data)

        }).done(function(data,status,xhr) {
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
                document.getElementById("checkoutStatus").style.color = "red";
                document.getElementById("checkoutStatus").innerHTML = message;
            }

            return false;
        }

        var transactionID = xhr.getResponseHeader('transactionID');
        var delay = xhr.getResponseHeader('delay');

        $.cookie("transactionID", transactionID);
        $.cookie("delay", delay);

        console.log("TransactionID: " + $.cookie("transactionID"));
        console.log("delay: " + delay);

        fetchPlaceOrderReport(0);
    })
    .fail(function (xhr, exception) {
        console.log(xhr);
        console.log(exception);
        var message = xhr.responseJSON ? xhr.responseJSON.message : "Oops! Something went wrong!";
        document.getElementById("checkoutStatus").style.color = "red";
        document.getElementById("checkoutStatus").innerHTML = message;
    });
}

function fetchPlaceOrderReport(count) {
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
                console.log("count: " + count);
                if (count >= MAX_REPORT_FETCH_TRIES) {
                    document.getElementById("checkoutStatus").style.color = "red";
                    document.getElementById("checkoutStatus").innerHTML = "Timeout. Unable to fetch place order result.";
                    return false;
                } else {
                    fetchPlaceOrderReport(count + 1);
                }
            } else if (xhr.status == 200) {
                if (status !== "success") {
                    console.log(xhr);

                    var message = xhr.responseJSON ? xhr.responseJSON.message : "Oops! Something went wrong!";
                    document.getElementById("checkoutStatus").style.color = "red";
                    document.getElementById("checkoutStatus").innerHTML = message;
                    return false;
                }

                if (data) {
                    console.log(data);

                    if (data.resultCode === 332) {
                        document.getElementById("checkoutStatus").style.color = "red";
                        document.getElementById("checkoutStatus").innerHTML = data.message;

                        var html = '<p> <a id="addCust" class="nav" href="#addCustomer" onclick=activateTab("addCustomer")>Create Customer Account</a></p>';
                        document.getElementById("instructionLink").innerHTML = html;
                    } else if (data.resultCode !== 3400) {
                        document.getElementById("checkoutStatus").style.color = "red";
                        document.getElementById("checkoutStatus").innerHTML = data.message;
                        return
                    }
                    else {
                        document.getElementById("checkoutStatus").style.color = "green";
                        document.getElementById("checkoutStatus").innerHTML = data.message;

                        var redirect = data.redirectURL;

                        console.log("Redirect URL: " + data.redirectURL)

                        var html = 'Click to <a href= "' + redirect + '">Complete Order</a> through PayPal.';
                        document.getElementById("instructionLink").innerHTML = html;
                    }
                } else {
                    document.getElementById("checkoutStatus").style.color = "red";
                    document.getElementById("checkoutStatus").innerHTML = "No response data received";
                }
            } else {
                console.log(xhr);
                var message = xhr.responseJSON ? xhr.responseJSON.message : "Oops! Something went wrong!";
                document.getElementById("checkoutStatus").style.color = "red";
                document.getElementById("checkoutStatus").innerHTML = message;
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

function goBackCheckout() {
    document.getElementById("checkoutStatus").innerHTML = "";
    document.getElementById("instructionLink").innerHTML = "";
    activateTab("checkout");
}
