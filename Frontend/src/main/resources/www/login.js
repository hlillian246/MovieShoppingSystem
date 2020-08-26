$('form[id="loginForm"]').submit(function (event) {
    event.preventDefault(); // Prevent the default form submit event, using ajax instead
    let email = $("#loginemail").val() // Extract data from search input box to be the title argument
    let password = $("#loginpassword").val()
    let pswd = password.split("")
    console.log("email:" + email)
    console.log("Pass: " + password)

    if (email === "") {
        document.getElementById("loginResult").style.color = "red";
        document.getElementById("loginResult").innerHTML = "Email is empty";
        return false;
    }
    if (password === "") {
        document.getElementById("loginResult").style.color = "red";
        document.getElementById("loginResult").innerHTML = "Password is empty";
        return false;
    }

    removeCookies();

    _data = {
            "email": email,
            "password": pswd
        };
    console.log(_data)

    $.ajax({
        method: "POST", // Declare request type
        url: GW_ENDPOINT + "/idm/login",
        headers: {"Content-Type": "application/json; charset=utf-8,"},
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify(_data),
    }).done(function (data, status, xhr) {
        console.log(data);
        console.log(status);
        console.log(xhr);

        if (xhr.status !== 204) {
            let message = data ? data.message : "Oops! Something went wrong.";

            document.getElementById("loginResult").style.color = "red";
            document.getElementById("loginResult").innerHTML = message;
            return false;
        }

        var transactionID = xhr.getResponseHeader('transactionID');
        var delay = xhr.getResponseHeader('delay');

//        var emailLogin = email;

        $.cookie("email", email);
        $.cookie("transactionID", transactionID);
        $.cookie("delay", delay);

        console.log("TransactionID: " + $.cookie("transactionID"));
        console.log(delay);

        fetcLoginReport(email, 0);
    })
    .fail(function (xhr, exception) {
        console.log(xhr);
        console.log(exception);
        var message = xhr.responseJSON ? xhr.responseJSON.message : "Oops! Something went wrong!";
        document.getElementById("loginResult").style.color = "red";
        document.getElementById("loginResult").innerHTML = message;
    });
})

function fetcLoginReport(email, count)
{
    setTimeout(function() {
        $.ajax({
            method: "GET", // Declare request type
            url: GW_ENDPOINT + "/report",
            headers:{
                "Content-Type":"application/json",
                "Accept" : "*/*",
                'email' : email,
                'transactionID' : $.cookie('transactionID'),
                'sessionID': $.cookie('sessionID')
            }
        }).done(function(data, status, xhr) {
            console.log(xhr);
            console.log(status);

            if (xhr.status == 204) {
                if (count >= MAX_REPORT_FETCH_TRIES)
                {
                    document.getElementById("loginResult").style.color = "red";
                    document.getElementById("loginResult").innerHTML = "Timeout. Unable to fetch login result.";
                    return false;
                }
                else
                {
                    fetcLoginReport(email, count+1);
                }
            }
            else if (xhr.status == 200)
            {
                if (status !== "success")
                {
                    console.log(xhr);

                    var message = xhr.responseJSON ? xhr.responseJSON.message : "Oops! Something went wrong!";
                    document.getElementById("loginResult").style.color = "red";
                    document.getElementById("loginResult").innerHTML = message;
                    return false;
                }

                if (data)
                {
                    if (data.resultCode === 120)
                    {
                        sessionID = data.sessionID;
                        $.cookie("sessionID", sessionID);

                        clearMessages()
                        activateTab("index");
                    }
                    else
                    {
                        message = data.message;
                        document.getElementById("loginResult").style.color = "red";
                        document.getElementById("loginResult").innerHTML = message;
                    }
                }
                else
                {
                    document.getElementById("loginResult").style.color = "red";
                    document.getElementById("loginResult").innerHTML = "No response data received";
                }
            }
            else
            {
                console.log(xhr);
                document.getElementById("loginResult").style.color = "red";
                document.getElementById("loginResult").innerHTML = "Oops! Something went wrong!";
                return false;
            }
        })
        .fail(function (xhr, exception) {
            console.log(xhr);
            console.log(exception);
            var message = xhr.responseJSON ? xhr.responseJSON.message : "Oops! Something went wrong!";

            document.getElementById("loginResult").style.color = "red";
            document.getElementById("loginResult").innerHTML = message;
        });
    }, $.cookie("delay"));
}





