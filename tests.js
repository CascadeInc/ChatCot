require("./arrayFunctions");
var should = require('should');
describe("Array operations in todo list", function () {
    var testArray;
    var resArray;
    var today = new Date();
    var nextWeek = new Date();
    nextWeek.setDate(today.getDate() + 3);
    var pastWeek = new Date();
    pastWeek.setDate(today.getDate() - 2);
    var forgotten = new Date();
    forgotten.setDate(today.getDate() - 12);

    beforeEach(function () {
        testArray = [];
        resArray = [];
        testArray.push({"todo": "11", "check": false, "id": 1, "date": today.toDateString()});
        testArray.push({"todo": "2", "check": false, "id": 2, "date": pastWeek.toDateString()});
        testArray.push({"todo": "3", "check": true, "id": 3, "date": today.toDateString()});
        testArray.push({"todo": "4", "check": false, "id": 5, "date": nextWeek.toDateString()});
        testArray.push({"todo": "5", "check": true, "id": 10, "date": nextWeek.toDateString()});
        testArray.push({"todo": "16", "check": true, "id": 11, "date": forgotten.toDateString()});
        testArray.push({"todo": "7", "check": false, "id": 25, "date": forgotten.toDateString()});
        testArray.push({"todo": "8", "check": true, "id": 10, "date": pastWeek.toDateString()});
    });

    describe('Add function', function () {
        it('should add to the end of array new item', function () {
            var len = testArray.length;
            add(testArray, "9", nextWeek.toDateString(), 15);
            should(testArray[len]).be.eql({
                "todo": "9",
                "check": false,
                "id": 15,
                "date": nextWeek.toDateString()
            }, "Add element worked wrong")
        });
    });

    describe('Remove function', function () {
        it('should remove element from array', function () {
            resArray.push({"todo": "11", "check": false, "id": 1, "date": today.toDateString()});
            resArray.push({"todo": "2", "check": false, "id": 2, "date": pastWeek.toDateString()});
            resArray.push({"todo": "4", "check": false, "id": 5, "date": nextWeek.toDateString()});
            resArray.push({"todo": "5", "check": true, "id": 10, "date": nextWeek.toDateString()});
            resArray.push({"todo": "16", "check": true, "id": 11, "date": forgotten.toDateString()});
            resArray.push({"todo": "7", "check": false, "id": 25, "date": forgotten.toDateString()});
            resArray.push({"todo": "8", "check": true, "id": 10, "date": pastWeek.toDateString()});
            removeItem(testArray, 3);
            should(testArray).be.eql(resArray, "Actual result is different from expected");
        });
    });

    describe('Get done list', function () {
        it('should return list with done items', function () {
            resArray.push({"todo": "3", "check": true, "id": 3, "date": today.toDateString()});
            resArray.push({"todo": "5", "check": true, "id": 10, "date": nextWeek.toDateString()});
            resArray.push({"todo": "16", "check": true, "id": 11, "date": forgotten.toDateString()});
            resArray.push({"todo": "8", "check": true, "id": 10, "date": pastWeek.toDateString()});
            should(getDone(testArray)).be.eql(resArray, "Actual result is different from expected");
        });
    });

    describe('Get not done list', function () {
        it('should return list with not done yet items', function () {
            resArray.push({"todo": "11", "check": false, "id": 1, "date": today.toDateString()});
            resArray.push({"todo": "2", "check": false, "id": 2, "date": pastWeek.toDateString()});
            resArray.push({"todo": "4", "check": false, "id": 5, "date": nextWeek.toDateString()});
            resArray.push({"todo": "7", "check": false, "id": 25, "date": forgotten.toDateString()});
            should(getNotDone(testArray)).be.eql(resArray, "Actual result is different from expected");
        });
    });

    describe('Change description', function () {
        it('should change description of item in array', function () {
            changeDesc(testArray, 25, "test");
            should(testArray[6].todo).be.eql("test", "Actual result is different from expected");
        });
    });

    describe('Get search results', function () {
        it('should return searched objects', function () {
            resArray.push({"todo": "11", "check": false, "id": 1, "date": today.toDateString()});
            resArray.push({"todo": "16", "check": true, "id": 11, "date": forgotten.toDateString()});
            should(getSearchResults(testArray, "1")).be.eql(resArray, "Actual result is different from expected");
        });
    });

    describe('Set item checked', function () {
        it('should set item check property to true', function () {
            setChecked(testArray, 1);
            should(testArray[0].check).be.eql(true, "State of item is still unchecked.");
        });
    });

    describe('Get today items', function () {
        it('should return list of not done items with deadline today', function () {
            resArray.push({"todo": "11", "check": false, "id": 1, "date": today.toDateString()});
            should(getToday(testArray)).be.eql(resArray, "Actual result is different from expected");
        });
    });

    describe('Get this week items', function () {
        it('should return list of not done items with deadline this week', function () {
            resArray.push({"todo": "11", "check": false, "id": 1, "date": today.toDateString()});
            resArray.push({"todo": "4", "check": false, "id": 5, "date": nextWeek.toDateString()});
            should(getWeek(testArray)).be.eql(resArray, "Actual result is different from expected");
        });
    });

    describe('Get overdue items', function () {
        it('should return list of not done in time items', function () {
            resArray.push({"todo": "2", "check": false, "id": 2, "date": pastWeek.toDateString()});
            resArray.push({"todo": "7", "check": false, "id": 25, "date": forgotten.toDateString()});
            should(getOverdue(testArray)).be.eql(resArray, "Actual result is different from expected");
        });
    });

    describe('Get forgotten items', function () {
        it('should return list of not done items with deadline more than week ago', function () {
            resArray.push({"todo": "7", "check": false, "id": 25, "date": forgotten.toDateString()});
            should(getForgotten(testArray)).be.eql(resArray, "Actual result is different from expected");
        });
    });

    describe('Get starting id for new elements', function () {
        it('should return max id + 1 or zero if array is empty', function () {
            should(getStartingId(testArray)).be.eql(26, "Wrong starting index");
        });
    });
});