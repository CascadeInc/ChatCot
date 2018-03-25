add = function (array, description, dateString, id) {
    if (description != "") {
        var temp = {};
        temp.todo = description;
        temp.check = false;
        temp.id = id;
        var date = new Date(dateString);
        if (isNaN(date.getTime()))
            date = new Date();
        temp.date = date.toDateString();
        array.push(temp);
    }
};

getDone = function (array) {
    var result = [];
    array.forEach(function (item) {
        if (item.check === true)
            result.push(item);
    });
    return result;
};

getNotDone = function (array) {
    var result = [];
    array.forEach(function (item) {
        if (item.check === false)
            result.push(item);
    });
    return result;
};

removeItem = function (array, id) {
    for (var i = 0; i < array.length; i++)
        if (id == array[i].id) {
            array.splice(i, 1);
            break;
        }
};

changeDesc = function (array, id, desc) {
    for (var i = 0; i < array.length; i++)
        if (id == array[i].id) {
            array[i].todo = desc;
            break;
        }
};


getSearchResults = function (array, desc) {
    var result = [];
    array.forEach(function (item) {
        if (item.todo.indexOf(desc) >= 0) {
            result.push(item);
        }
    });
    return result;
};

setChecked = function (array, id) {
    for (var i = 0; i < array.length; i++)
        if (id == array[i].id) {
            array[i].check = true;
            break;
        }
};

getToday = function (array) {
    var now = new Date();
    var result = [];
    array.forEach(function (item) {
        if ((item.date.localeCompare(now.toDateString()) == 0) && (item.check == false)) {
            result.push(item);
        }
    });
    return result;
};

getWeek = function (array) {
    var refNow = new Date();
    refNow.setHours(0, 0, 0, 0);

    var result = [];
    array.forEach(function (item) {
        var deadline = new Date(item.date).setHours(0, 0, 0, 0);
        if ((deadline - refNow < 688248497) && (deadline - refNow >= 0) && (item.check == false)) {
            result.push(item);
        }
    });
    return result;
};

getOverdue = function (array) {
    var refNow = new Date();
    refNow.setHours(0, 0, 0, 0);
    var result = [];
    array.forEach(function (item) {
        var deadline = new Date(item.date);
        deadline.setHours(0, 0, 0, 0);
        if ((deadline - refNow < 0) && (item.check == false)) {
            result.push(item);
        }
    });
    return result;
};

getForgotten = function (array) {
    var result = [];
    var refNow = new Date();
    refNow.setHours(0, 0, 0, 0);
    array.forEach(function (item) {
        var deadline = new Date(item.date).setHours(0, 0, 0, 0);
        if ((deadline - refNow < -688248497) && (deadline - refNow < 0) && (item.check == false)) {
            result.push(item);
        }
    });
    return result;
};