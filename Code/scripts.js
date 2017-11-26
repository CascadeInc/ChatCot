window.onload = function(){
//    alert("123");
    
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
    }
    
    document.getElementById('doneList').onclick = function() {
        document.getElementById('Tasks').innerHTML = '';
        for (var key in todoList){
            if (todoList[key].check == true){
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
        div.innerHTML = "<input type='checkbox' id='checkbox" + todoList[key].id + "'>" + todoList[key].todo + "<span  class='addBtn btn' id='span" + todoList[key].id + "'>Delete</span>";
        document.getElementById('Tasks').appendChild(div);
        //document.getElementById('span0').addEventListener("onclick", deleteItem);
        document.getElementById('span'+ todoList[key].id).onclick = deleteItem;
        document.getElementById('checkbox'+ todoList[key].id).onclick = checkItem;
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
}