function bot_start(document) {
    let botSendBtn = document.getElementById("sendToBot");
    const botFunctions = ["standard greetings", 'special greetings', 'question greetings', 'greetings answer', 'yes', 'no', 'default', "filter", 'filter check', 'adding'];
    if (botSendBtn == null) return;
    let state = 99;
    botSendBtn.onclick = function () {
        let httpClient = new HttpClient();
        let q = {quote: document.getElementById("botInput").value};
        document.getElementById("botReply").innerHTML = "";
        let div = document.createElement("div");
        if (state != 0) {
            httpClient.post("/main", "application/json", JSON.stringify(q), function (response) {
                let resp = JSON.parse(response);
                if (resp.type === 0) {
                    let res = "Please, enter type of command you want to assign: ";
                    botFunctions.forEach(function (element) {
                        res += element + " | "
                    });
                    state = 0;
                    div.innerHTML = res;
                } else if (resp.type === 8 || resp.type === 9) {
                    let reArray = JSON.parse(resp.answer);
                    let resString = "";
                    reArray.forEach(function (element) {
                        resString += "<p>" + new Date(element.date).toDateString() + ": <b>" + element.description + "</b>: " + element.todo + "; checked: " + element.check + "</p>"
                    });
                    div.innerHTML = resString;
                    if (reArray.length == 0)
                        div.innerHTML = "There are no such elements";
                } else if (resp.type ===10) window.location.reload();
                    else
                    div.innerText = resp.answer;
                document.getElementById("botReply").appendChild(div);
            }, function () {

            })
        } else {
            if (botFunctions.indexOf(document.getElementById("botInput").value) != -1) {
                httpClient.post("/main", "application/json", JSON.stringify(q), function (response) {
                    document.getElementById("botReply").innerHTML = "Created successfully";
                    state = 99;
                }, function () {

                })
            }
            else {
                let res = "Please, enter type of command you want to assign: ";
                botFunctions.forEach(function (element) {
                    res += element + " | "
                });
                document.getElementById("botReply").innerHTML = res;
            }
        }
    }
}