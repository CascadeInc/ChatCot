window.onload = start;

function start() {

    var todoList = [];
    var state = 0;
    if (localStorage.getItem('todo') != undefined) {
        todoList = JSON.parse(localStorage.getItem('todo'));
        for (var i = 0; i < todoList.length; i++) {
            if (todoList[i] === null) {
                todoList.splice(i, 1);
            }
        }
        refresh();
    }

    var glId = getStartingId(todoList);

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
        syaye = 5;
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
        var searchName = document.getElementById("searchForm").value;

        getSearchResults(todoList, searchName).forEach(function (elem) {
            writeItem(item);
        });

        if (document.getElementById('Tasks').innerHTML == '') {
            writeEmptyMessage();
        }
    }

    function writeNotDoneList() {
        document.getElementById('Tasks').innerHTML = '';
        getNotDone(todoList).forEach(function (elem) {
            writeItem(elem)
        });

        if (document.getElementById('Tasks').innerHTML == '') {
            writeEmptyMessage();
        }
    }

    function writeItem(item) {
        var div = document.createElement('div');
        div.className = "Task" + item.id + " Task";
        div.innerHTML = "<input type='checkbox' id='checkbox" + item.id + "'><div class='TaskDesc'>" + item.todo + "</div><span  class='editbtn btn  btn-light' id='edit" + item.id + "'>Edit</span><span  class='deletebtn btn btn-danger' id='span" + item.id + "'>Delete</span>";

        document.getElementById('Tasks').appendChild(div);
        document.getElementById('span' + item.id).onclick = deleteItem;
        document.getElementById('checkbox' + item.id).onclick = checkItem;
        document.getElementById('edit' + item.id).onclick = editItem;
    }

    function writeDoneItem(item) {
        var div = document.createElement('div');
        div.className = "Task" + item.id + " Task";
        div.innerHTML = "<div class='TaskDesc'>" + item.todo + "</div><span  class='deletebtn btn btn-danger' id='span" + item.id + "'>Delete</span>";

        document.getElementById('Tasks').appendChild(div);
        document.getElementById('span' + item.id).onclick = deleteItem;
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
        refresh();
    }

    function deleteItem() {
        var str = this.id;
        var id = str.substring(4);
        var elem = document.getElementsByClassName('Task' + id);
        var el = elem[0];
        el.parentNode.removeChild(el);
        removeItem(todoList, id);
        localStorage.setItem('todo', JSON.stringify(todoList));
        refresh();
    }

    function writeEmptyMessage() {
        var div = document.createElement('h2');
        div.className = "EmptyH2";
        div.innerHTML = "Oops,<br>Nothing in this list";
        document.getElementById('Tasks').appendChild(div);
    }
}
