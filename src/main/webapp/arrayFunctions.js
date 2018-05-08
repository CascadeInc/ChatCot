add = function (array, name, description, dateString, id) {
    if (description != "") {
        const temp = {};
        temp.description = name;
        temp.todo = description;
        temp.check = false;
        temp.id = id;
        let date = new Date(dateString);
        if (isNaN(date.getTime()))
            date = new Date();
        temp.date = date.toDateString();
        array.push(temp);
    }
};

getDone = function (array) {
    const result = [];
    array.forEach(function (item) {
        if (item.check === true)
            result.push(item);
    });
    return result;
};

getNotDone = function (array) {
    const result = [];
    array.forEach(function (item) {
        if (item.check === false)
            result.push(item);
    });
    return result;
};

removeItem = function (array, id) {
    for (let i = 0; i < array.length; i++)
        if (id == array[i].id) {
            array.splice(i, 1);
            break;
        }
};

changeDesc = function (array, id, desc) {
    for (let i = 0; i < array.length; i++)
        if (id == array[i].id) {
            array[i].todo = desc;
            break;
        }
};


getSearchResults = function (array, desc) {
    const result = [];
    array.forEach(function (item) {
        if (item.todo.indexOf(desc) >= 0) {
            result.push(item);
        }
    });
    return result;
};

setChecked = function (array, id) {
    for (let i = 0; i < array.length; i++)
        if (id == array[i].id) {
            array[i].check = true;
            break;
        }
};

getToday = function (array) {
    const now = new Date();
    const result = [];
    array.forEach(function (item) {
        if ((item.date.localeCompare(now.toDateString()) == 0) && (item.check == false)) {
            result.push(item);
        }
    });
    return result;
};

getWeek = function (array) {
    const refNow = new Date();
    refNow.setHours(0, 0, 0, 0);

    const result = [];
    array.forEach(function (item) {
        const deadline = new Date(item.date).setHours(0, 0, 0, 0);
        if ((deadline - refNow < 688248497) && (deadline - refNow >= 0) && (item.check == false)) {
            result.push(item);
        }
    });
    return result;
};

getOverdue = function (array) {
    const refNow = new Date();
    refNow.setHours(0, 0, 0, 0);
    const result = [];
    array.forEach(function (item) {
        const deadline = new Date(item.date);
        deadline.setHours(0, 0, 0, 0);
        if ((deadline - refNow < 0) && (item.check == false)) {
            result.push(item);
        }
    });
    return result;
};

getForgotten = function (array) {
    const result = [];
    const refNow = new Date();
    refNow.setHours(0, 0, 0, 0);
    array.forEach(function (item) {
        const deadline = new Date(item.date).setHours(0, 0, 0, 0);
        if ((deadline - refNow < -688248497) && (deadline - refNow < 0) && (item.check == false)) {
            result.push(item);
        }
    });
    return result;
};

getStartingId = function (array) {
    let max = 0;
    array.forEach(function (item) {
        if (item.id > max) max = item.id;
    });
    if (max == 0) return max;
    return max + 1;
};