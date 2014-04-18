function setReplyTarget(id) {
    if (id <= 0) {
        return;
    }

    var DOMHiddenField = document.getElementById("hidden_input");

    alert(DOMHiddenField.length + "!");
}