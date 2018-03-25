window.onload = start;

function start() {

    var todoList = [];
    localStorage.clear();
    if (localStorage.getItem('todo') != undefined) {
        todoList = JSON.parse(localStorage.getItem('todo'));
        for (var i = 0; i < todoList.length; i++) {
            if (todoList[i] === null) {
                todoList.splice(i, 1);
            }
        }
        writeNotDoneList();
    }

    var glId = getmaxId(todoList);
    function getmaxId(array){
        var max = 0;
        array.forEach(function (item) {
            if (item.id>max) max = item.id;
        });
        if (max == 0) return max
        return max+1;
    }

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
        getDone(todoList).forEach(function (item) {
            writeDoneItem(item);
        });

        if (document.getElementById('Tasks').innerHTML == '') {
            writeEmptyMessage();
        }
    }

    document.getElementById('todayList').addEventListener("click", displayToday);

    function displayToday() {
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
        div.innerHTML = "<input type='checkbox' id='checkbox" + item.id + "'>" + item.todo + "<span  class='deletebtn' id='span" + item.id + "'>Delete</span>" + "<span  class='editbtn' id='edit" + item.id + "'>Edit</span>";

        document.getElementById('Tasks').appendChild(div);
        document.getElementById('span' + item.id).onclick = deleteItem;
        document.getElementById('checkbox' + item.id).onclick = checkItem;
        document.getElementById('edit' + item.id).onclick = editItem;
    }

    function writeDoneItem(item) {
        var div = document.createElement('div');
        div.className = "Task" + item.id + " Task";
        div.innerHTML = item.todo + "<span  class='deletebtn' id='span" + item.id + "'>Delete</span>";

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