function readCookie(name) {
    var searchName = name + '=';
    var cookies = document.cookie.split(';');

    for (var i = 0; i < cookies.length; i++) {
        var c = cookies[i];
        while (c.charAt(0) == ' ') { //удаляет пустоту в начале
            c = c.substring(1, c.length);
        }

        if (c.indexOf(searchName) == 0) {
            return c.substring(searchName.length, c.length);
        }
    }

    return null;
}

function loadTasks() {
    var tasks = loadDataSync('http://127.0.0.1:8080/web-api/tsp/rest/task/user', 'GET');
    if (tasks == null) {
        return [];
    }
    tasks.sort(function (a, b) {
        if (a.doBefore > b.doBefore) {
            return 1;
        } else {
            if (a.doBefore == b.doBefore) {
                return 0;
            } else {
                return -1;
            }
        }
    });

    return tasks;
}

function doAuth() {
    if (navigator.cookieEnabled) {
        authToken = readCookie("authorization");
        if (!authToken) {
            window.location.href = "auth.html";
        }
    }
}

function loadDataSync(url, method) {
    var someData = null;
    var req = new XMLHttpRequest();
    req.onreadystatechange = function () {
        if (this.readyState == 4) {
            if (this.status != 200) {
                if (this.status == 401) {
                    window.location.href = "auth.html";
                }
                alert("something go wrong");
            } else {
                someData = JSON.parse(req.responseText)
            }
        }
    };
    req.open(method, url, false);
    req.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
    req.setRequestHeader("authorization", authToken);
    req.send();

    return someData;
}

function createTask() {
    var inner = document.getElementById("newTaskNameInput");
    if (!validateSomeField(inner, 'Имя', 1, 50, false)) {
        return false;
    }
    var data = new Object();
    data.name = inner.value;
    inner = document.getElementById("newDescriptionArea");
    if (!validateSomeField(inner, 'Описание', 0, 135, true)) {
        return false;
    }
    data.description = inner.value ? inner.value : null;
    inner = document.getElementById("newSelect").value;
    data.category = inner ? inner : null;

    var year = null;
    if (document.getElementById("newDatePicker").value) {
        year = document.getElementById("newDatePicker").value;
    }
    if (year) {
        var time = "00:00";
        if (document.getElementById("newTimePicker").value) {
            time = document.getElementById("newTimePicker").value;
        }
        var date = new Date(year + ' ' + time);
        var yyyy = date.getFullYear();
        var mm = (date.getMonth() + 1) < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1);
        var dd = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
        var h = date.getHours() < 10 ? '0' + date.getHours() : date.getHours();
        var m = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes();
        data.doBefore = yyyy + '-' + mm + '-' + dd + ' ' + h + ':' + m + ':00';
    } else {
        data.doBefore = null;
    }
    data.modifiedBy = user.id;
    inner = document.getElementById("newPlaceArea");
    if (!validateSomeField(inner, 'Место', 0, 135, true)) {
        return false;
    }
    data.plase = inner.value ? inner.value : null;

    data.did = false;

    var jString = JSON.stringify(data);
    console.log(jString);

    var req = new XMLHttpRequest();
    req.onreadystatechange = function () {
        if (this.readyState == 4 && this.status != 200) {
            alert("ашиппка(((");
            return false;
        }
    };
    req.open('PUT', 'http://127.0.0.1:8080/web-api/tsp/rest/task/save', false);
    req.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
    req.setRequestHeader("authorization", authToken);
    req.send(jString);
    return true;
}

function updateTask() {
    var inner = document.getElementById("taskNameInput");
    if (!validateSomeField(inner, 'Имя', 1, 50, false)) {
        return false;
    }
    let taskArrayId =  document.getElementById("windHeader").innerHTML - 1;
    var data = {};
    data.id = tasks[taskArrayId].id;
    data.did = tasks[taskArrayId].did;
    data.name = inner.value;
    data.sharedBy = tasks[taskArrayId].sharedBy;
    inner = document.getElementById("descriptionArea");
    if (!validateSomeField(inner, 'Описание', 0, 135, true)) {
        return false;
    }
    data.description = inner.value ? inner.value : null;
    inner = document.getElementById("select").value;
    data.category = inner ? inner : null;

    var year = null;
    if (document.getElementById("datePicker").value) {
        year = document.getElementById("datePicker").value;
    }
    if (year) {
        var time = "00:00";
        if (document.getElementById("timePicker").value) {
            time = document.getElementById("timePicker").value;
        }
        var date = new Date(year + ' ' + time);
        var yyyy = date.getFullYear();
        var mm = (date.getMonth() + 1) < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1);
        var dd = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
        var h = date.getHours() < 10 ? '0' + date.getHours() : date.getHours();
        var m = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes();
        data.doBefore = yyyy + '-' + mm + '-' + dd + ' ' + h + ':' + m + ':00';
    } else {
        data.doBefore = null;
    }
    data.modifiedBy = user.id;
    inner = document.getElementById("placeArea");
    if (!validateSomeField(inner, 'Место', 0, 135, true)) {
        return false;
    }
    data.plase = inner.value ? inner.value : null;

    var jString = JSON.stringify(data);
    console.log(jString);

    var req = new XMLHttpRequest();
    req.onreadystatechange = function () {
        if (this.readyState == 4 && this.status != 200) {
            alert("ашиппка(((");
            return false;
        }
    };
    req.open('POST', 'http://127.0.0.1:8080/web-api/tsp/rest/task/update', false);
    req.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
    req.setRequestHeader("authorization", authToken);
    req.send(jString);
    return true;
}

function didTask(i) {
    var data = new Object();
    data.id = tasks[i].id;
    data.name = tasks[i].name;
    data.description = tasks[i].description;
    data.category = tasks[i].category;
    data.doBefore = tasks[i].doBefore;
    data.modifiedBy = user.id;
    data.plase = tasks[i].plase;
    data.sharedBy = tasks[i].sharedBy;

    data.did = true;

    var jString = JSON.stringify(data);
    console.log(jString);

    var req = new XMLHttpRequest();
    req.onreadystatechange = function () {
        if (this.readyState == 4 && this.status != 200) {
            alert("ашиппка(((");
            return;
        }
        if (this.status == 200) {
            tasks = loadTasks();
            redrawTasks(tasks, categories);
        }
    };
    req.open('POST', 'http://127.0.0.1:8080/web-api/tsp/rest/task/update', false);
    req.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
    req.setRequestHeader("authorization", authToken);
    req.send(jString);
}

function undidTask(i) {
    var data = new Object();
    data.id = tasks[i].id;
    data.name = tasks[i].name;
    data.description = tasks[i].description;
    data.category = tasks[i].category;
    data.doBefore = tasks[i].doBefore;
    data.modifiedBy = user.id;
    data.plase = tasks[i].plase;
    data.sharedBy = tasks[i].sharedBy;

    data.did = false;

    var jString = JSON.stringify(data);
    console.log(jString);

    var req = new XMLHttpRequest();
    req.onreadystatechange = function () {
        if (this.readyState == 4 && this.status != 200) {
            alert("ашиппка(((");
            return;
        }
        if (this.status == 200) {
            tasks = loadTasks();
            redrawTasks(tasks, categories);
        }
    };
    req.open('POST', 'http://127.0.0.1:8080/web-api/tsp/rest/task/update', false);
    req.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
    req.setRequestHeader("authorization", authToken);
    req.send(jString);
}

function createCategory() {
    var inner = document.getElementById("categoryNameInput");
    if (!validateSomeField(inner, 'Имя', 1, 50, false)) {
        return false;
    }
    var data = new Object();
    data.name = inner.value;

    inner = document.getElementById("categoryDescriptionArea");
    if (!validateSomeField(inner, 'Описание', 0, 135, true)) {
        return false;
    }
    data.description = inner.value ? inner.value : null;

    data.createdBy = user.id;

    var jString = JSON.stringify(data);

    var req = new XMLHttpRequest();
    req.onreadystatechange = function () {
        if (this.readyState == 4 && this.status != 200) {
            alert("ашиппка(((");
            return false;
        }
    };
    req.open('PUT', 'http://127.0.0.1:8080/web-api/tsp/rest/category/save', false);
    req.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
    req.setRequestHeader("authorization", authToken);
    req.send(jString);
    return true;
}

function updateCategory() {
    var inner = document.getElementById("categoryUpdateNameInput");
    if (!validateSomeField(inner, 'Имя', 1, 50, false)) {
        return false;
    }
    var data = new Object();
    data.name = inner.value;

    inner = document.getElementById("updateCategorySelect");
    data.id = categories[inner.value].id;

    inner = document.getElementById("categoryUpdateDescriptionArea");
    if (!validateSomeField(inner, 'Описание', 0, 135, true)) {
        return false;
    }
    data.description = inner.value ? inner.value : null;

    data.createdBy = user.id;

    var jString = JSON.stringify(data);

    var req = new XMLHttpRequest();
    req.onreadystatechange = function () {
        if (this.readyState == 4 && this.status != 200) {
            alert("ашиппка(((");
            return false;
        }
    };
    req.open('POST', 'http://127.0.0.1:8080/web-api/tsp/rest/category/update', false);
    req.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
    req.setRequestHeader("authorization", authToken);
    req.send(jString);
    return true;
}

function deleteCategory() {
    let id = categories[document.getElementById("updateCategorySelect").value].id;
    var req = new XMLHttpRequest();
    req.onreadystatechange = function () {
        if (this.readyState == 4 && this.status != 200) {
            alert("ашиппка(((");
            return false;
        }
    };
    req.open('DELETE', 'http://127.0.0.1:8080/web-api/tsp/rest/category/delete/' + id, false);
    req.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
    req.setRequestHeader("authorization", authToken);
    req.send();
}

function exit() {
    var req = new XMLHttpRequest();
    req.open('GET', 'http://127.0.0.1:8080/web-api/tsp/rest/auth/exit', false);
    req.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
    req.setRequestHeader("authorization", authToken);
    req.send(null);
    killCookie("authorization");
    window.location.href = "auth.html";
}

function killCookie(name) {
    var date = new Date();
    date.setTime(date.getTime() + (-1) * 24 * 60 * 60 * 1000);
    document.cookie = name + '=; expires=' + date.toGMTString() + '; path =/';
}

function deleteTask(i) {
    var id = tasks[i].id;
    var req = new XMLHttpRequest();
    req.onreadystatechange = function () {
        if (this.readyState == 4 && this.status != 200) {
            alert("ашиппка(((");
            return false;
        }
    };
    req.open('DELETE', 'http://127.0.0.1:8080/web-api/tsp/rest/task/delete/' + id, false);
    req.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
    req.setRequestHeader("authorization", authToken);
    req.send();

    tasks = loadTasks();
    categories = loadDataSync('http://127.0.0.1:8080/web-api/tsp/rest/category/user', 'GET');
    redrawTasks(tasks, categories);
}

function shareTask() {
    let id = tasks[document.getElementById("windUpdateHeader").innerHTML - 1].id;
    let input = document.getElementById("recipientNameInput");
    if (!validateSomeField(input, 'Имя пользователя', 1, 150, false)) {
        return false;
    }

    let data = {sharedTo: input.value, taskId: id};
    let req = new XMLHttpRequest();
    req.onreadystatechange = function () {
        if (this.readyState == 4) {
            if (this.status != 200) {
                alert(this.responseText);
            } else {
                return true;
            }
        }
    };
    req.open('POST', 'http://127.0.0.1:8080/web-api/tsp/rest/task/share', false);
    req.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
    req.setRequestHeader("authorization", authToken);
    req.send(JSON.stringify(data));

    return true;
}