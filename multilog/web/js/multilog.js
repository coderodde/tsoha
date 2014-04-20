function setReplyTarget(id, who) {
    if (id <= 0) {
        return;
    }

    document.getElementById("hidden_input").value = id;
    document.getElementById("reply_to").innerHTML = "Replying to " + who + ".";
    document.getElementById("dont_reply").style.color = "white";
    location.hash = '#reply_anchor';
}

function forget() {
    document.getElementById("hidden_input").value = "";
    document.getElementById("reply_to").innerHTML = "";
    document.getElementById("dont_reply").style.color = "#ff3300";
}