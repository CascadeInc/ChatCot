const HttpClient = function () {
    this.get = function (url, successCallback, failCallback) {
        const httpRequest = new XMLHttpRequest();
        httpRequest.open("GET", url, false);
        httpRequest.onreadystatechange = function () {
            if (httpRequest.readyState == 4) {
                if (httpRequest.status == 200) {
                    successCallback(httpRequest.responseText);
                } else failCallback(httpRequest.status)
            }
        };
        httpRequest.send();
    };

    this.post = function (url, contentType, content, successCallback, failCallback) {
        const httpRequest = new XMLHttpRequest();

        httpRequest.onreadystatechange = function () {
            if (httpRequest.readyState == XMLHttpRequest.DONE) {
                if (httpRequest.status == 200) {
                    successCallback(httpRequest.responseText);
                } else failCallback(httpRequest.status);
            }
        };
        httpRequest.open("POST", url, false);
        httpRequest.setRequestHeader("Content-type", contentType);
        httpRequest.send(content);

    }
};