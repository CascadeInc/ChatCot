window.onload = function(){
    
    var todoList = [];
    if (localStorage.getItem('todo')!=undefined) {
        todoList = JSON.parse(localStorage.getItem('todo'));
        writeNoteDoneList();
    }
    var glId = todoList.length;
    
    document.getElementById('addBtn').onclick = function (){
        var d = document.getElementById('descForm').value;
        var temp = {};
        temp.todo = d;
        temp.check = false;
        temp.id = glId;
        d = document.getElementById("dateForm").value; //2017-11-26
        temp.date = d;         
        //if (d=='') {alert("123");}
        todoList[glId] = temp;
        glId = glId + 1;
        localStorage.setItem('todo', JSON.stringify(todoList));
        writeItem(glId-1);
    }
    
    document.getElementById('allList').onclick = function() {
        writeNoteDoneList();
        if (document.getElementById('Tasks').innerHTML == '') {writeEmptyMessage();}  
    }
    
    document.getElementById('doneList').onclick = function() {
        document.getElementById('Tasks').innerHTML = '';
        for (var key in todoList){
            if (todoList[key].check == true){
                writeDoneItem(key);
            }
        }
        if (document.getElementById('Tasks').innerHTML == '') {writeEmptyMessage();}
    }
    
    document.getElementById('todayList').onclick = function() {
        document.getElementById('Tasks').innerHTML = '';
        var now = new Date();
        var year = now.getFullYear();
        var month = now.getMonth() + 1;
        var day = now.getDate();
        var strDateNow = year + "-" + month + "-" + day;
        for (var key in todoList){
            if (todoList[key].date.localeCompare(strDateNow) == 0){
                writeItem(key);
            }
        }
    }
    
    document.getElementById('next7DayList').onclick = function() {
        document.getElementById('Tasks').innerHTML = '';
        var now = new Date();
        var year = now.getFullYear();
        var month = now.getMonth();
        var day = now.getDate();
        var refNow = new Date(year, month, day, 0, 0, 0, 0);
        for (var key in todoList){
            year = todoList[key].date.substring(0, 4);
            month = todoList[key].date.substring(5, 7) -1;
            day = todoList[key].date.substring(8);
            var deadline = new Date(year, month, day, 0, 0, 0, 0);
            //alert(deadline - now);
            if ((deadline - refNow < 688248497) && (deadline - refNow >= 0)){
                writeItem(key);
            }
        }
    }
    
    document.getElementById('overdueList').onclick = function() {
        document.getElementById('Tasks').innerHTML = '';
        var now = new Date();
        var year = now.getFullYear();
        var month = now.getMonth();
        var day = now.getDate();
        var refNow = new Date(year, month, day, 0, 0, 0, 0);
        for (var key in todoList){
            if (todoList[key].date == "") {continue;}
            year = todoList[key].date.substring(0, 4);
            month = todoList[key].date.substring(5, 7) -1;
            day = todoList[key].date.substring(8);
            var deadline = new Date(year, month, day, 0, 0, 0, 0);
            //alert(deadline - now);
            if (deadline - refNow < 0) {
                writeItem(key);
            }
        }
    }
    
    document.getElementById('forgottenList').onclick = function() {
        document.getElementById('Tasks').innerHTML = '';
        var now = new Date();
        var year = now.getFullYear();
        var month = now.getMonth();
        var day = now.getDate();
        var refNow = new Date(year, month, day, 0, 0, 0, 0);
        for (var key in todoList){
            if (todoList[key].date == "") {continue;}
            year = todoList[key].date.substring(0, 4);
            month = todoList[key].date.substring(5, 7) -1;
            day = todoList[key].date.substring(8);
            var deadline = new Date(year, month, day, 0, 0, 0, 0);
            //alert(deadline - now);
            if ((deadline - refNow < -688248497) && (deadline - refNow < 0)){
                writeItem(key);
            }
        }
    }
    
    document.getElementById('searchBtn').onclick = function() {
        document.getElementById('Tasks').innerHTML = '';
        var searchName = document.getElementById("searchForm").value;
        for (var key in todoList){
            if (todoList[key].todo.indexOf(searchName)>=0)
                {
                    writeItem(key);
                }
        }
    }
    
    function writeNoteDoneList() {
        document.getElementById('Tasks').innerHTML = '';
        for (var key in todoList){
            
            if (todoList[key].check == false){
                writeItem(key);
            }
        }
    }
    
    function writeItem(key) {
        var div = document.createElement('div');
        div.className = "Task"+todoList[key].id+" Task";
        div.innerHTML = "<input type='checkbox' id='checkbox" + todoList[key].id + "'>" + todoList[key].todo + "<span  class='deletebtn' id='span" + todoList[key].id + "'>Delete</span>";
        document.getElementById('Tasks').appendChild(div);
        //document.getElementById('span0').addEventListener("onclick", deleteItem);
        document.getElementById('span'+ todoList[key].id).onclick = deleteItem;
        document.getElementById('checkbox'+ todoList[key].id).onclick = checkItem;
    }
    
    function writeDoneItem(key) {
        var div = document.createElement('div');
        div.className = "Task"+todoList[key].id+" Task";
        div.innerHTML = todoList[key].todo + "<span  class='deletebtn' id='span" + todoList[key].id + "'>Delete</span>";
        document.getElementById('Tasks').appendChild(div);
        document.getElementById('span'+ todoList[key].id).onclick = deleteItem;
    }
    
    function checkItem() {
        //alert(this.checked);
        var str = this.id;
        var id = str.substring(8);
        var elem = document.getElementsByClassName('Task'+id);
        var el = elem[0];
        el.parentNode.removeChild(el);  
        for (var key in todoList) {
            if (id == todoList[key].id)
                {
                    todoList[key].check = true;
                    break;
                }
        }
        localStorage.setItem('todo', JSON.stringify(todoList));
    }
    
    function deleteItem() {
        var str = this.id;
        var id = str.substring(4);
        var elem = document.getElementsByClassName('Task'+id);
        var el = elem[0];
        el.parentNode.removeChild(el);
        for (var key in todoList) {
            if (id == todoList[key].id)
                {
                    todoList.splice(key, 1);
                    break;
                }
        }
        localStorage.setItem('todo', JSON.stringify(todoList));
    }
    
    function writeEmptyMessage() {
        var div = document.createElement('h2');
        div.className = "EmptyH2";
        div.innerHTML = "Oops,<br>Nothing in this list";
        document.getElementById('Tasks').appendChild(div);
    }
}