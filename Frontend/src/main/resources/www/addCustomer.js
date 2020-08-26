function AddCustomer() {
    insertCreditCard();
}

function insertCreditCard() {
    let fName = $("#Fname").val();
    let lName = $("#LName").val();
    let ccId = $("#ccId").val();
    let expiration = $("#ExpDate").val();

    ccInfo =
        {
            "firstName": fName,
            "lastName": lName,
            "id": ccId,
            "expiration": expiration
        }
    console.log(ccInfo)

    $.ajax({
        method: "POST", // Declare request type
        headers: {
            "Content-Type": "application/json",
            "SessionID": $.cookie("sessionID"),
            "email": $.cookie("email")
        },
        contentType: "application/json",
        url: GW_ENDPOINT + "/billing/creditcard/insert",
        dataType: "json",
        data: JSON.stringify(ccInfo)

    }).done(function (data, status, xhr) {
        console.log(data);
        console.log(xhr);
        console.log(status);

        if (xhr.status !== 204) {
            var message = data ? data.message : "Oops! Something went wrong!";

            document.getElementById("customerInsertStatus").style.color = "red";
            document.getElementById("customerInsertStatus").innerHTML = message;
            return false;
        }

        var transactionID = xhr.getResponseHeader('transactionID');
        var delay = xhr.getResponseHeader('delay');

        $.cookie("transactionID", transactionID);
        $.cookie("delay", delay);

        console.log("TransactionID: " + $.cookie("transactionID"));
        console.log("delay: " + delay);

        fetchCreditCardReport(0);
    })
    .fail(function (xhr, exception) {
        console.log(xhr);
        console.log(exception);
        var message = xhr.responseJSON ? xhr.responseJSON.message : "Oops! Something went wrong!";
        document.getElementById("customerInsertStatus").style.color = "red";
        document.getElementById("customerInsertStatus").innerHTML = message;
        return false
    });
}


function fetchCreditCardReport(count)
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
            console.log(data);
            console.log(xhr);
            console.log(status);

            if (xhr.status == 204) {
                if (count >= MAX_REPORT_FETCH_TRIES) {
                    document.getElementById("customerInsertStatus").style.color = "red";
                    document.getElementById("customerInsertStatus").innerHTML = "Timeout. Unable to fetch result.";
                    return false;
                } else {
                    fetchCreditCardReport(count+1);
                }
            } else if (xhr.status == 200) {
                if (status !== "success") {
                    console.log(xhr);
                    var message = "Credit Card: ";
                    message += xhr.responseJSON ? xhr.responseJSON.message : "Oops! Something went wrong!";
                    document.getElementById("customerInsertStatus").style.color = "red";
                    document.getElementById("customerInsertStatus").innerHTML = message;
                    return false;
                }

                if (data) {
                    console.log(data);

                    if (data.resultCode !== 3200) {
                        document.getElementById("customerInsertStatus").style.color = "red";
                        document.getElementById("customerInsertStatus").innerHTML = "Credit Card: " + data.message;
                        return false
                    }

                    document.getElementById("customerInsertStatus").style.color = "green";
                    document.getElementById("customerInsertStatus").innerHTML = data.message;

                    insertCustomer();
                }
                else
                {
                    document.getElementById("customerInsertStatus").style.color = "red";
                    document.getElementById("customerInsertStatus").innerHTML = "Credit Card: No response data received";
                    return false;
                }
            } else {
                console.log(xhr);
                var message = "Credit Card: ";
                message += xhr.responseJSON ? xhr.responseJSON.message : "Oops! Something went wrong!";
                document.getElementById("customerInsertStatus").style.color = "red";
                document.getElementById("customerInsertStatus").innerHTML = message;
                return false;
            }
        })
        .fail(function (xhr, exception) {
            console.log(xhr);
            console.log(exception);
            var message = "Credit Card: ";
            message += xhr.responseJSON ? xhr.responseJSON.message : "Oops! Something went wrong!";
            document.getElementById("customerInsertStatus").style.color = "red";
            document.getElementById("customerInsertStatus").innerHTML = message;
            return false;
        });
    }, $.cookie("delay"));
    return true;
}


function insertCustomer()
{
    let fName = $("#Fname").val();
    let lName = $("#LName").val();
    let ccId = $("#ccId").val();
    let address = $("#address").val();

    _data =
        {
            "email" : $.cookie("email"),
            "firstName": fName,
            "lastName": lName,
            "ccId": ccId,
            "address": address
        }

    $.ajax({
        method: "POST", // Declare request type
        headers: {
            "Content-Type": "application/json",
            "SessionID": $.cookie("sessionID"),
            "email": $.cookie("email")
        },
        contentType: "application/json",
        url: GW_ENDPOINT + "/billing/customer/insert",
        dataType: "json",
        data: JSON.stringify(_data)

    }).done(function (data, status, xhr) {
        console.log(data);
        console.log(xhr);
        console.log(status);

        if (xhr.status !== 204) {
            var message = "Customer: ";
            message += data ? data.message : "Oops! Something went wrong!";

            document.getElementById("customerInsertStatus").style.color = "red";
            document.getElementById("customerInsertStatus").innerHTML = message;
            return false;
        }

        var transactionID = xhr.getResponseHeader('transactionID');
        var delay = xhr.getResponseHeader('delay');

        $.cookie("transactionID", transactionID);
        $.cookie("delay", delay);

        console.log("TransactionID: " + $.cookie("transactionID"));
        console.log("delay: " + delay);

        fetchCustReport(0);
    })
    .fail(function (xhr, exception) {
        console.log(xhr);
        console.log(exception);
        var message = "Customer: ";
        message += xhr.responseJSON ? xhr.responseJSON.message : "Oops! Something went wrong!";
        document.getElementById("customerInsertStatus").style.color = "red";
        document.getElementById("customerInsertStatus").innerHTML = message;
    });
}

function fetchCustReport(count)
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
            console.log(data);
            console.log(xhr);
            console.log(status);

            if (xhr.status == 204) {
                if (count >= MAX_REPORT_FETCH_TRIES) {
                    document.getElementById("customerInsertStatus").style.color = "red";
                    document.getElementById("customerInsertStatus").innerHTML = "Customer: Timeout. Unable to fetch search result.";
                    return false;
                } else {
                    fetchCustReport(count+1);
                }
            } else if (xhr.status == 200) {
                if (status !== "success") {
                    console.log(xhr);
                    var message = "Customer: ";
                    message += xhr.responseJSON ? xhr.responseJSON.message : "Oops! Something went wrong!";
                    document.getElementById("customerInsertStatus").style.color = "red";
                    document.getElementById("customerInsertStatus").innerHTML = message;
                    return false;
                }

                if (data) {
                    console.log(data);

                    if (data.resultCode != 3300) {
                        document.getElementById("customerInsertStatus").style.color = "red";
                        document.getElementById("customerInsertStatus").innerHTML = "Customer: " + data.message;
                        return false
                    }

                    document.getElementById("customerInsertStatus").style.color = "green";
                    document.getElementById("customerInsertStatus").innerHTML = "Customer Billing Account Created Successfully";

                    return true;
                }
                else
                {
                    document.getElementById("customerInsertStatus").style.color = "red";
                    document.getElementById("customerInsertStatus").innerHTML = "Customer: No response data received";
                    return false
                }
            } else {
                console.log(xhr);
                var message = "Customer: ";
                message += xhr.responseJSON ? xhr.responseJSON.message : "Oops! Something went wrong!";
                document.getElementById("customerInsertStatus").style.color = "red";
                document.getElementById("customerInsertStatus").innerHTML = message;
                return false;
            }
        })
            .fail(function (xhr, exception) {
                console.log(xhr);
                console.log(exception);
                var message = "Customer: ";
                message += xhr.responseJSON ? xhr.responseJSON.message : "Oops! Something went wrong!";
                document.getElementById("customerInsertStatus").style.color = "red";
                document.getElementById("customerInsertStatus").innerHTML = message;
                return false
            });
    }, $.cookie("delay"));
}

