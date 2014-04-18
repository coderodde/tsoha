function setReplyTarget(id, who) {
    if (id <= 0) {
        return;
    }

    document.getElementById("hidden_input").value = id;
    document.getElementById("reply_to").innerHTML = "Replying to " + who + ".";
    document.getElementById("dont_reply").innerHTML  = "Forget reply!";
}

function forget() {
    document.getElementById("hidden_input").value = "";
    document.getElementById("reply_to").innerHTML = "";
    document.getElementById("dont_reply").innerHTML = "";
    document.getElementById("dont_reply").style = "background-color: transparent;";
}