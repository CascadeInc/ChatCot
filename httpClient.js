var HttpClient = function () {
    this.get = function (url, successCallback, failCallback) {
        var httpRequest = new XMLHttpRequest();
        httpRequest.onreadystatechange = function () {
            if (httpRequest.readyState == 4) {
                if (httpRequest.status == 200) {
                    successCallback(httpRequest.responseText);
                } else failCallback(httpRequest.status)
            }
        };
        httpRequest.open("GET", url, true);
        httpRequest.send();
    };

    this.post = function (url, contentType, content, successCallback, failCallback) {
        var httpRequest = new XMLHttpRequest();
        httpRequest.open("POST", url, true);
        httpRequest.setRequestHeader("Content-type", contentType);
        httpRequest.onreadystatechange = function () {
            if (httpRequest.readyState == 4) {
                if (httpRequest.status == 200) {
                    successCallback(httpRequest.responseText);
                } else failCallback(httpRequest.status)
            }
        };
        httpRequest.send(content);
    }
};