
$('form[id="registerForm"]').submit(function (event) {
    console.log("This is a test")
    event.preventDefault(); // Prevent the default form submit event, using ajax instead
    let email = $("#regemail").val() // Extract data from search input box to be the title argument
    let pass = $("#regpassword").val()
    let confirm = $("#confirm").val()
    console.log(pass !== confirm);
    if(confirm === "" && pass === "")
    {
        document.getElementById("registerResult").style.color = "red";
        document.getElementById("registerResult").innerHTML = "Passwords are both blank";
        return false;

    }
    if(confirm === "")
    {
        document.getElementById("registerResult").style.color = "red";
        document.getElementById("registerResult").innerHTML = "Confirm Password is blank";
        return false;
    }
    if(pass === "")
    {
        document.getElementById("registerResult").style.color = "red";
        document.getElementById("registerResult").innerHTML = "Password is blank";
        return false;
    }

    if(pass!== confirm)
    {
        document.getElementById("registerResult").style.color = "red";
        document.getElementById("registerResult").innerHTML = "Passwords do not match";
        return false;
    }
    let pswd = pass.split("")
    console.log("email:" + email)
    console.log("Pass: " + pass)
    console.log("Confirm: " + confirm)
    console.log("split: " + pswd)
    _data =
        {
            "email": email,
            "password": pswd
        }

    console.log(_data)

    $.ajax({
        method: "POST", // Declare request type
        url: GW_ENDPOINT + "/idm/register",
        headers:{
            "Content-Type":"application/json"
        },
        contentType: "application/json",
        dataType: "json",
        data:JSON.stringify(_data),
    }).done(function(data, status, xhr) {

        console.log(data);
        console.log(status);
        console.log(xhr);

        if (xhr.status !== 204) {
            let message = data ? data.message : "Oops! Something went wrong.";

            document.getElementById("registerResult").style.color = "red";
            document.getElementById("registerResult").innerHTML = message;
            return false;
        }

        var transactionID = xhr.getResponseHeader('transactionID');
        var delay = xhr.getResponseHeader('delay');

        $.cookie("transactionID", transactionID);
        $.cookie("delay", delay);

        console.log("cookie " + $.cookie("transactionID"));
        console.log(delay);


        fetchRegisterReport(email, 0);

    } )
    .fail(function (xhr, exception) {
        console.log(xhr);
        console.log(exception);
        var message = xhr.responseJSON ? xhr.responseJSON.message : "Oops! Something went wrong!";
        document.getElementById("registerResult").style.color = "red";
        document.getElementById("registerResult").innerHTML = message;
    });
});


function fetchRegisterReport(email, count) {
    setTimeout(function() {
        $.ajax({
            method: "GET", // Declare request type
            url: GW_ENDPOINT + "/report",
            headers:{
                "Content-Type":"application/json",
                "Accept" : "*/*",
                'email' : email,
                'transactionID' : $.cookie('transactionID')
            }
        }).done(function(data,status,xhr) {
            if (xhr.status == 204) {
                if (count >= MAX_REPORT_FETCH_TRIES)
                {
                    document.getElementById("registerResult").style.color = "red";
                    document.getElementById("registerResult").innerHTML = "Timeout. Unable to fetch register result.";
                    return false;
                }
                else
                {
                    fetchRegisterReport(email, count+1);
                }
            }
            else if (xhr.status == 200) {
                if (data) {
                    console.log(data);
                    message = data.message;
                    if (data.resultCode === 110) {
                        clearMessages();
                        activateTab("login");
                    } else {
                        document.getElementById("registerResult").style.color = "red";
                        document.getElementById("registerResult").innerHTML = message;
                    }
                } else {
                    document.getElementById("registerResult").style.color = "red";
                    document.getElementById("registerResult").innerHTML = "No response data received";
                }
            }
            else {
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
            document.getElementById("registerResult").style.color = "red";
            document.getElementById("registerResult").innerHTML = message;
        });
    }, $.cookie("delay"))


}