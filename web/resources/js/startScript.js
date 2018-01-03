function setCookie(cname, cvalue, exhours) {
    let d = new Date();
    d.setTime(d.getTime() + (exhours * 60 * 60 * 1000));
    let expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}

function login() {
    let value = document.getElementById("usernameForm:usernameInput").value;
    setCookie('username', value, 1);
    window.location.replace("chat.xhtml");
}