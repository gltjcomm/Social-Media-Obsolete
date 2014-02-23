function bindingAccount() {

    var secCode = document.getElementById("securityCode").value;
        secCode = secCode.substring(1, 2);

    if (Number(secCode) > 5) {
        document.location.replace("./bindingSuccess.html");
    } else {
        document.location.replace("./bindingFailed.html");
    }
}