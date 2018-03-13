window.onload = function () {

    var todoList = [];
    if (localStorage.getItem('todo') != undefined) {
        todoList = JSON.parse(localStorage.getItem('todo'));
        writeNoteDoneList();
    }
    var glId = todoList.length;

    document.getElementById('addBtn').onclick = function () {
        var d = document.getElementById('descForm').value;
        if (d != "") {
            var temp = {};
            temp.todo = d;
            temp.check = false;
            temp.id = glId;
            d = document.getElementById("dateForm").value;
            var date = new Date(d);
            if (isNaN(date.getTime()))
                date = new Date();
            date.setHours(0, 0, 0, 0);
            temp.date = date.toDateString();
            todoList[glId] = temp;
            localStorage.setItem('todo', JSON.stringify(todoList));
            writeNoteDoneList();
            glId++;
        }
    };

    document.getElementById('allList').onclick = function () {
        writeNoteDoneList();
        if (document.getElementById('Tasks').innerHTML === '') {
            writeEmptyMessage();
        }
    };

    document.getElementById('doneList').onclick = function () {
        document.getElementById('Tasks').innerHTML = '';

        for (var key in todoList) {
            if (todoList[key].check == true) {
                writeDoneItem(key);
            }
        }

        if (document.getElementById('Tasks').innerHTML == '') {
            writeEmptyMessage();
        }
    };

    document.getElementById('todayList').onclick = function () {
        document.getElementById('Tasks').innerHTML = '';
        var now = new Date();
        now.setHours(0, 0, 0, 0);

        for (var key in todoList) {
            if ((todoList[key].date.localeCompare(now.toDateString()) == 0) && (todoList[key].check == false)) {
                writeItem(key);
            }
        }

        if (document.getElementById('Tasks').innerHTML == '') {
            writeEmptyMessage();
        }
    };

    document.getElementById('next7DayList').onclick = function () {
        document.getElementById('Tasks').innerHTML = '';

        var refNow = new Date();
        refNow.setHours(0, 0, 0, 0);

        for (var key in todoList) {
            var deadline = new Date(todoList[key].date).setHours(0, 0, 0, 0);
            //alert(deadline - now);
            if ((deadline - refNow < 688248497) && (deadline - refNow >= 0) && (todoList[key].check == false)) {
                writeItem(key);
            }
        }

        if (document.getElementById('Tasks').innerHTML == '') {
            writeEmptyMessage();
        }
    };

    document.getElementById('overdueList').onclick = function () {
        document.getElementById('Tasks').innerHTML = '';
        var refNow = new Date();
        refNow.setHours(0, 0, 0, 0);
        for (var key in todoList) {
            if (todoList[key].date == "") {
                continue;
            }
            var deadline = new Date(todoList[key].date);
            deadline.setHours(0, 0, 0, 0);
            if ((deadline - refNow < 0) && (todoList[key].check == false)) {
                writeItem(key);
            }
        }

        if (document.getElementById('Tasks').innerHTML == '') {
            writeEmptyMessage();
        }
    };

    document.getElementById('forgottenList').onclick = function () {
        document.getElementById('Tasks').innerHTML = '';
        var refNow = new Date();
        refNow.setHours(0, 0, 0, 0);
        for (var key in todoList) {
            if (todoList[key].date == "") {
                continue;
            }
            var deadline = new Date(todoList[key].date).setHours(0, 0, 0, 0);
            //alert(deadline - now);
            if ((deadline - refNow < -688248497) && (deadline - refNow < 0) && (todoList[key].check == false)) {
                writeItem(key);
            }
        }

        if (document.getElementById('Tasks').innerHTML == '') {
            writeEmptyMessage();
        }
    };

    document.getElementById('searchBtn').onclick = function () {
        document.getElementById('Tasks').innerHTML = '';
        var searchName = document.getElementById("searchForm").value;

        for (var key in todoList) {
            if (todoList[key].todo.indexOf(searchName) >= 0) {
                writeItem(key);
            }
        }

        if (document.getElementById('Tasks').innerHTML == '') {
            writeEmptyMessage();
        }
    };

    function writeNoteDoneList() {
        document.getElementById('Tasks').innerHTML = '';

        for (var key in todoList) {
            if (todoList[key].check == false) {
                writeItem(key);
            }
        }

        if (document.getElementById('Tasks').innerHTML == '') {
            writeEmptyMessage();
        }
    }

    function writeItem(key) {
        var div = document.createElement('div');
        div.className = "Task" + todoList[key].id + " Task";
        div.innerHTML = "<input type='checkbox' id='checkbox" + todoList[key].id + "'>" + todoList[key].todo + "<span  class='deletebtn' id='span" + todoList[key].id + "'>Delete</span>" + "<span  class='editbtn' id='edit" + todoList[key].id + "'>Edit</span>";

        document.getElementById('Tasks').appendChild(div);
        document.getElementById('span' + todoList[key].id).onclick = deleteItem;
        document.getElementById('checkbox' + todoList[key].id).onclick = checkItem;
        document.getElementById('edit' + todoList[key].id).onclick = editItem;
    }

    function writeDoneItem(key) {
        var div = document.createElement('div');
        div.className = "Task" + todoList[key].id + " Task";
        div.innerHTML = todoList[key].todo + "<span  class='deletebtn' id='span" + todoList[key].id + "'>Delete</span>";

        document.getElementById('Tasks').appendChild(div);
        document.getElementById('span' + todoList[key].id).onclick = deleteItem;
    }

    function checkItem() {
        var str = this.id;
        var id = str.substring(8);
        var elem = document.getElementsByClassName('Task' + id);
        var el = elem[0];
        el.parentNode.removeChild(el);

        for (var key in todoList) {
            if (id == todoList[key].id) {
                todoList[key].check = true;
                break;
            }
        }

        localStorage.setItem('todo', JSON.stringify(todoList));
    }

    function editItem() {
        var str = this.id;
        var id = str.substring(4);
        var elem = document.getElementsByClassName('Task' + id);
        var el = elem[0];
        el.parentNode.removeChild(el);
        var newDesc = prompt("Write new description", "write description here");
        for (var key in todoList) {
            if (id == todoList[key].id) {
                todoList[key].todo = newDesc;
                break;
            }
        }
        localStorage.setItem('todo', JSON.stringify(todoList));
        writeNoteDoneList();
    }

    function deleteItem() {
        var str = this.id;
        var id = str.substring(4);
        var elem = document.getElementsByClassName('Task' + id);
        var el = elem[0];
        el.parentNode.removeChild(el);

        for (var key in todoList) {
            if (id == todoList[key].id) {
                todoList.splice(key, 1);
                break;
            }
        }

        localStorage.setItem('todo', JSON.stringify(todoList));
        writeNoteDoneList();
    }

    function writeEmptyMessage() {
        var div = document.createElement('h2');
        div.className = "EmptyH2";
        div.innerHTML = "Oops,<br>Nothing in this list";
        document.getElementById('Tasks').appendChild(div);
    }
};