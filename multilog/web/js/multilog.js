function setReplyTarget(id, who) {
    if (id <= 0) {
        return;
    }

    var DOMHiddenField = document.getElementById("hidden_input");
    DOMHiddenField.value = id;

    var DOMReplyToLabel = document.getElementById("reply_to");
    DOMReplyToLabel.innerHTHML = "Replying to " + who;

    var DOMDontReplyButton = document.getElementById("dont_reply");
    DOMDontReplyButton.innerHTML = "Forget"
}

function forget() {
    document.getElementById("hidden_input").value = "";
    document.getElementById("reply_to").innerHTML = "";
    document.getElementById("dont_reply").innerHTML = "";
}