window.onload = start;

function start() {
    if (getCookie("userName")==undefined||getCookie("userName")=="")
    {
        const div1 = document.createElement("div");
        div1.innerHTML="<a class=\"btn btn-light\" href='login.html'>Login</a>";
        document.getElementById("firstDynamic").appendChild(div1);
        const div2 = document.createElement("div");
        div2.innerHTML="<a class=\"btn btn-light\" href='register.html'>Sign Up</a>";
        document.getElementById("secondDynamic").appendChild(div2);
        const help = document.createElement("div");
        help.innerHTML="<p class='text-info'>Please, log in to use system</\p>";
        document.getElementById("searchFieldDiv").appendChild(help);
    }
    else {
        const div = document.createElement("div");
        div.innerHTML="Hello, "+getCookie("userName");
        document.getElementById("firstDynamic").appendChild(div);
        const div2 = document.createElement("div");
        div2.innerHTML="<a class=\"btn btn-light\" href='admin.html'>Phrases</a>";
        document.getElementById("secondDynamic").appendChild(div2);
        const div3 = document.createElement("div");
        div3.innerHTML="<button class=\"btn btn-light\" id='logoutBtn'>Logout</button>";
        document.getElementById("thirdDynamic").appendChild(div3);
        const search = document.createElement("div");
        search.innerHTML="<input class=\"form-control\" type=\"text\" id=\"searchForm\" placeholder=\"Search..\">";
        document.getElementById("searchFieldDiv").appendChild(search);
        const searchBtn = document.createElement("div");
        searchBtn.innerHTML="<span class=\"btn btn-primary\" id=\"searchBtn\">Search</span>";
        document.getElementById("searchBtnDiv").appendChild(searchBtn);
        document.getElementById("logoutBtn").onclick = function(){
            new HttpClient().get("/logout", function () {
                window.location.replace("/");
            },function(){
                window.location.replace("/");
            });

        }
    }
    function getCookie(name) {
        const matches = document.cookie.match(new RegExp(
            `(?:^|; )${name.replace(/([.$?*|{}()\[\]\\\/+^])/g, '\\$1')}=([^;]*)`
        ));
        return matches ? decodeURIComponent(matches[1]) : undefined;
    }

    let loginButton = document.getElementById("loginBtn");
    if (loginButton!=null){
        loginButton.onclick=function () {
            let log=document.getElementById('loginInput').value;
            let pass=document.getElementById('password').value;
            let user = {login:log,password:pass};
            let httpClient = new HttpClient();
            let succ;
            httpClient.post("/login","application/json",JSON.stringify(user),function () {
                succ = true;
            }, function () {
                succ = false;
            });
            if (succ) window.location.replace("/");
        };
    }

    let signUpButton = document.getElementById("signUpBtn");
    if (signUpButton!=null){
        signUpButton.onclick=function () {
            let log=document.getElementById('loginInput').value;
            let pass=document.getElementById('password').value;
            let repPass=document.getElementById('repPassword').value;
            let mail=document.getElementById('emailInput').value;
            let user = {login:log,password:pass,repPassword:repPass,email:mail};
            let httpClient = new HttpClient();
            httpClient.post("/pages/reg","application/json",JSON.stringify(user),function () {
                window.location.replace("/");
            }, function () {
                window.location.replace("/register.html");
            })
        };
        return;
    }

    if (getCookie("userName")==undefined||getCookie("userName")=="") return;
    bot_start(document);
    let todoList = [];
    let state = 0;
    if (localStorage.getItem('todo') != undefined) {
        todoList = JSON.parse(localStorage.getItem('todo'));
        for (let i = 0; i < todoList.length; i++) {
            if (todoList[i] === null) {
                todoList.splice(i, 1);
            }
        }
        refresh();
    }

    let glId = getStartingId(todoList);
    let addBtn = document.getElementById('addBtn');
    if (addBtn!=null)
    addBtn.onclick = function () {
        const descr = document.getElementById('descForm').value;
        const dateStr = document.getElementById("dateForm").value;
        add(todoList, descr, dateStr, glId);
        writeNotDoneList();
        localStorage.setItem('todo', JSON.stringify(todoList));
        glId++;
    };
    else return;


    document.getElementById('allList').onclick = function () {
        document.title = "ToDo List";
        state = 0;
        refresh();
        if (document.getElementById('Tasks').innerHTML === '') {
            writeEmptyMessage();
        }
    };

    function refresh() {
        switch (state) {
            case 0:
                writeNotDoneList();
                break;
            case 1:
                displayToday();
                break;
            case 2:
                displayWeek();
                break;
            case 3:
                displayOverdue();
                break;
            case 4:
                displayDone();
                break;
            case 5:
                displayForgotten();
                break;
        }
    }

    document.getElementById('doneList').onclick = displayDone;

    function displayDone() {
        state = 4;
        document.getElementById('Tasks').innerHTML = '';
        document.title = "Done";
        getDone(todoList).forEach(function (item) {
            writeDoneItem(item);
        });

        if (document.getElementById('Tasks').innerHTML == '') {
            writeEmptyMessage();
        }
    }

    document.getElementById('todayList').addEventListener("click", displayToday);

    function displayToday() {
        state = 1;
        document.getElementById('Tasks').innerHTML = '';
        document.title = "Today";
        getToday(todoList).forEach(function (item) {
            writeItem(item);
        });

        if (document.getElementById('Tasks').innerHTML == '') {
            writeEmptyMessage();
        }
    }

    document.getElementById('next7DayList').addEventListener("click", displayWeek);

    function displayWeek() {
        state = 2;
        document.getElementById('Tasks').innerHTML = '';
        document.title = "Week";
        getWeek(todoList).forEach(function (item) {
            writeItem(item);
        });

        if (document.getElementById('Tasks').innerHTML == '') {
            writeEmptyMessage();
        }
    }

    document.getElementById('overdueList').addEventListener("click", displayOverdue);

    function displayOverdue() {
        state = 3;
        document.getElementById('Tasks').innerHTML = '';
        document.title = "Overdue";
        getOverdue(todoList).forEach(function (item) {
            writeItem(item);
        });

        if (document.getElementById('Tasks').innerHTML == '') {
            writeEmptyMessage();
        }
    }

    document.getElementById('forgottenList').addEventListener("click", displayForgotten);

    function displayForgotten() {
        state = 5;
        document.getElementById('Tasks').innerHTML = '';
        document.title = "Forgotten";
        getForgotten(todoList).forEach(function (item) {
            writeItem(item);
        });

        if (document.getElementById('Tasks').innerHTML == '') {
            writeEmptyMessage();
        }
    }

    document.getElementById('searchBtn').addEventListener("click", search);

    function search() {
        document.getElementById('Tasks').innerHTML = '';
        document.title = "Search results";
        const searchName = document.getElementById("searchForm").value;

        getSearchResults(todoList, searchName).forEach(function (item) {
            writeItem(item);
        });

        if (document.getElementById('Tasks').innerHTML == '') {
            writeEmptyMessage();
        }
    }

    function writeNotDoneList() {
        let elem = document.getElementById('Tasks');
        if (elem==null) return;
        elem.innerHTML = '';
        getNotDone(todoList).forEach(function (elem) {
            writeItem(elem)
        });

        if (document.getElementById('Tasks').innerHTML == '') {
            writeEmptyMessage();
        }
    }

    function writeItem(item) {
        const div = document.createElement('div');
        div.className = "Task" + item.id + " Task";
        div.innerHTML = "<input type='checkbox' id='checkbox" + item.id + "'><div class='TaskDesc'>" +
            item.date + ": " +
            item.todo + "</div><span  class='btn  btn-light' id='edit" +
            item.id + "'>Edit</span><span  class='btn btn-danger' id='span" +
            item.id + "'>Delete</span>";

        document.getElementById('Tasks').appendChild(div);
        document.getElementById('span' + item.id).onclick = deleteItem;
        document.getElementById('checkbox' + item.id).onclick = checkItem;
        document.getElementById('edit' + item.id).onclick = editItem;
    }

    function writeDoneItem(item) {
        const div = document.createElement('div');
        div.className = "Task" + item.id + " Task";
        div.innerHTML = "<div class='TaskDesc'>" + item.date + ": " +
            item.todo + "</div><span  class='btn btn-danger' id='span" +
            item.id + "'>Delete</span>";

        document.getElementById('Tasks').appendChild(div);
        document.getElementById('span' + item.id).onclick = deleteItem;
    }

    function checkItem() {
        const str = this.id;
        const id = str.substring(8);
        const el = document.getElementsByClassName('Task' + id)[0];
        el.parentNode.removeChild(el);

        setChecked(todoList, id);

        localStorage.setItem('todo', JSON.stringify(todoList));
    }

    function editItem() {
        const str = this.id;
        const id = str.substring(4);
        const el = document.getElementsByClassName('Task' + id)[0];
        el.parentNode.removeChild(el);
        const newDesc = prompt("Write new description", "write description here");
        changeDesc(todoList, id, newDesc);
        localStorage.setItem('todo', JSON.stringify(todoList));
        refresh();
    }

    function deleteItem() {
        const str = this.id;
        const id = str.substring(4);
        const el = document.getElementsByClassName('Task' + id)[0];
        el.parentNode.removeChild(el);
        removeItem(todoList, id);
        localStorage.setItem('todo', JSON.stringify(todoList));
        refresh();
    }

    function writeEmptyMessage() {
        const div = document.createElement('h2');
        div.className = "EmptyH2";
        div.innerHTML = "Oops,<br>Nothing in this list";
        document.getElementById('Tasks').appendChild(div);
    }
}
