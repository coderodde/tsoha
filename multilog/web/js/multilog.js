function setReplyTarget(id) {
    if (id <= 0) {
        return;
    }

    var DOMHiddenField = document.getElementById("hidden_input");
    DOMHiddenField.value = id;

    var DOMReplyToLabel = document.getElementById("reply_to");
    DOMReplyToLabel.innerHTHML = "";
}

function forget() {
    document.getElementById("hidden_input").value = "";
    document.getElementById("dont_reply").innerHTML = "";
}