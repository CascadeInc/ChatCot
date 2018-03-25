function add(array, description, dateString, id) {
    if (description != "") {
        var temp = {};
        temp.todo = description;
        temp.check = false;
        temp.id = id;
        var date = new Date(dateString);
        if (isNaN(date.getTime()))
            date = new Date();
        date.setHours(0, 0, 0, 0);
        temp.date = date.toDateString();
        array[id] = temp;
    }
}

function getDone(array) {
    var result = [];
    for (var i = 0; i < array.length; i++)
        if (array[i].check === true)
            result.push(array[i]);
    return result;
}

function removeItem(array, id) {
    for (var key in array) {
        if (id == array[key].id) {
            array.splice(key, 1);
            break;
        }
    }
}

function changeDesc(array, id, desc) {
    for (var key in array) {
        if (id == array[key].id) {
            array[key].todo = desc;
            break;
        }
    }
}

function getSearchResults(array, desc) {
    var result = [];
    for (var key in array) {
        if (array[key].todo.indexOf(desc) >= 0) {
            result.push(array[key]);
        }
    }
    return result;
}

function setChecked(array, id) {
    for (var key in array) {
        if (id == array[key].id) {
            array[key].check = true;
            break;
        }
    }
}

function getToday(array) {
    var result = [];
    var now = new Date();
    now.setHours(0, 0, 0, 0);
    for (var key in array) {
        if ((array[key].date.localeCompare(now.toDateString()) == 0) && (array[key].check == false)) {
            result.push(array[key])
        }
    }
    return result;
}

function getWeek(array) {
    var result = [];
    var refNow = new Date();
    refNow.setHours(0, 0, 0, 0);

    for (var key in array) {
        var deadline = new Date(array[key].date).setHours(0, 0, 0, 0);
        if ((deadline - refNow < 688248497) && (deadline - refNow >= 0) && (array[key].check == false)) {
            result.push(array[key])
        }
    }
    return result;
}

function getOverdue(array) {
    var result = [];
    var refNow = new Date();
    refNow.setHours(0, 0, 0, 0);
    for (var key in array) {
        if (array[key].date == "") {
            continue;
        }
        var deadline = new Date(array[key].date);
        deadline.setHours(0, 0, 0, 0);
        if ((deadline - refNow < 0) && (array[key].check == false)) {
            result.push(array[key])
        }
    }
    return result;
}

function getForgotten(array) {
    var result = [];
    var refNow = new Date();
    refNow.setHours(0, 0, 0, 0);
    for (var key in array) {
        if (array[key].date == "") {
            continue;
        }
        var deadline = new Date(array[key].date).setHours(0, 0, 0, 0);
        if ((deadline - refNow < -688248497) && (deadline - refNow < 0) && (array[key].check == false)) {
            result.push(key)
        }
    }
    return result;
}

window.onload = start;

function start() {

    var todoList = [];
    if (localStorage.getItem('todo') != undefined) {
        todoList = JSON.parse(localStorage.getItem('todo'));
        for (var i = 0; i < todoList.length; i++) {
            if (todoList[i] === null) {
                todoList.splice(i, 1);
            }
        }
        writeNotDoneList();
    }
    var glId = todoList.length;

    document.getElementById('addBtn').onclick = function () {
        var descr = document.getElementById('descForm').value;
        var dateStr = document.getElementById("dateForm").value;
        add(todoList, descr, dateStr, glId);
        writeNotDoneList();
        localStorage.setItem('todo', JSON.stringify(todoList));
        glId++;
    };

    document.getElementById('allList').onclick = function () {
        document.title = "ToDo List";
        writeNotDoneList();
        if (document.getElementById('Tasks').innerHTML === '') {
            writeEmptyMessage();
        }
    };

    document.getElementById('doneList').onclick = displayDone;

    function displayDone() {
        document.getElementById('Tasks').innerHTML = '';
        document.title = "Done";
        for (var todoItem in getDone(todoList))
            writeDoneItem(todoItem);
        if (document.getElementById('Tasks').innerHTML == '') {
            writeEmptyMessage();
        }
    }

    document.getElementById('todayList').addEventListener("click", displayToday);

    function displayToday() {
        document.getElementById('Tasks').innerHTML = '';
        document.title = "Today";
        for (var item in getToday(todoList))
            writeItem(item);

        if (document.getElementById('Tasks').innerHTML == '') {
            writeEmptyMessage();
        }
    }

    document.getElementById('next7DayList').addEventListener("click", displayWeek);

    function displayWeek() {
        document.getElementById('Tasks').innerHTML = '';
        document.title = "Week";
        for (var item in getWeek(todoList))
            writeItem(item);

        if (document.getElementById('Tasks').innerHTML == '') {
            writeEmptyMessage();
        }
    }

    document.getElementById('overdueList').addEventListener("click", displayOverdue);

    function displayOverdue() {
        document.getElementById('Tasks').innerHTML = '';
        document.title = "Overdue";
        for (var item in getOverdue(todoList))
            writeItem(item);

        if (document.getElementById('Tasks').innerHTML == '') {
            writeEmptyMessage();
        }
    }

    document.getElementById('forgottenList').addEventListener("click", displayForgotten);

    function displayForgotten() {
        document.getElementById('Tasks').innerHTML = '';
        document.title = "Forgotten";

        for (var item in getForgotten(todoList))
            writeItem(item);
        if (document.getElementById('Tasks').innerHTML == '') {
            writeEmptyMessage();
        }
    }

    document.getElementById('searchBtn').addEventListener("click", search);

    function search() {
        document.getElementById('Tasks').innerHTML = '';
        document.title = "Search results";
        var searchName = document.getElementById("searchForm").value;

        for (var item in getSearchResults(todoList, searchName))
            writeItem(item);

        if (document.getElementById('Tasks').innerHTML == '') {
            writeEmptyMessage();
        }
    }

    function writeNotDoneList() {
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

        setChecked(todoList, id);

        localStorage.setItem('todo', JSON.stringify(todoList));
    }

    function editItem() {
        var str = this.id;
        var id = str.substring(4);
        var elem = document.getElementsByClassName('Task' + id);
        var el = elem[0];
        el.parentNode.removeChild(el);
        var newDesc = prompt("Write new description", "write description here");
        changeDesc(todoList, id, newDesc);
        localStorage.setItem('todo', JSON.stringify(todoList));
        writeNotDoneList();
    }

    function deleteItem() {
        var str = this.id;
        var id = str.substring(4);
        var elem = document.getElementsByClassName('Task' + id);
        var el = elem[0];
        el.parentNode.removeChild(el);
        removeItem(todoList, id);
        localStorage.setItem('todo', JSON.stringify(todoList));
        writeNotDoneList();
    }

    function writeEmptyMessage() {
        var div = document.createElement('h2');
        div.className = "EmptyH2";
        div.innerHTML = "Oops,<br>Nothing in this list";
        document.getElementById('Tasks').appendChild(div);
    }
}