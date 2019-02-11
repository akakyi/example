function setUserNameSpan(user) {
    if (!user.firstName) {
        document.getElementById("userName").innerHTML = user.login;
    } else {
        document.getElementById("userName").innerHTML = user.firstName;
    }
}

function existCat(categories, id) {
    for (var i = 0; i < categories.length; i++) {
        if (categories[i].id == id) {
            return categories[i];
        }
    }

    return null
}

function colorPicker(i) {
    var col = "";
    //console.log(color % 2);
    if (i || i == 0) {
        if (i % 2 == 0) {
            col = "background-color: darkgray";
        } else {
            col = "background-color: #c2c2c2";
        }
    }

    return col;
}

function redrawTasks(tasks, categories) {
    var table = document.getElementById("mainTable");
    var elCount = table.rows.length;
    for (var j = 1; j < elCount; j++) {
        table.deleteRow(1);
    }
    for (var i = 0; i < tasks.length; i++) {
        var row = table.insertRow(i + 1);
        if (!tasks[i].did) {
            row.style = colorPicker(i);
        } else {
            row.style = "background-color: rgba(57, 184, 0, 0.64)";
        }
        var cell = row.insertCell(0);
        cell.colSpan = "2";

        // cell.innerHTML += '';

        cell.innerHTML += '<strong>' + (i + 1) + '</strong><br>';
        cell.innerHTML += '<strong>Имя задачи:</strong> ' + tasks[i].name + '<br>';
        if (tasks[i].category) {
            var category = existCat(categories, tasks[i].category);
            if (category) {
                var categoryText = category.name;
                if (category.description) {
                    categoryText = '<span title="' + category.description + '" style="cursor: pointer">' +
                        category.name + '</span>';
                }
                cell.innerHTML += '<strong>Категория:</strong>' + categoryText + '<br>';
            }
        }
        if (tasks[i].doBefore) {
            var date = new Date(tasks[i].doBefore);
            var yyyy = date.getFullYear();
            var mm = (date.getMonth() + 1) < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1);
            var dd = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
            var h = date.getHours() < 10 ? '0' + date.getHours() : date.getHours();
            var m = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes();
            cell.innerHTML +=
                '<strong>Выполнить до:</strong><time>' + dd + '-' + mm + '-' + yyyy + ' ' + h + ':' + m + ':00</time><br>';
        }
        if (tasks[i].plase) {
            cell.innerHTML += '<strong>Место:</strong> ' + tasks[i].plase + '<br>';
        }
        if (tasks[i].did && tasks[i].modifiedAt) {
            cell.innerHTML += '<strong>Сделано:</strong> ' + tasks[i].modifiedAt + '<br>';
        }
        if (tasks[i].sharedBy) {
            cell.innerHTML += '<strong>Поделился:</strong> ' + tasks[i].sharedBy + '<br>';
        }
        if (tasks[i].description) {
            cell.innerHTML += '<strong>Описание:</strong><pre>' + tasks[i].description + '</pre>';
        } else {
            cell.innerHTML += '<br>';
        }
        cell.innerHTML += '<button onclick="updateTaskWindow(' + i + ')">Редактировать</button>';
        cell.innerHTML += '<button onclick="deleteTask(' + i + ')">Удалить</button>';
        cell.innerHTML += '<button onclick="shareTaskWindow(' + i + ')">Поделиться</button>';

        if (!tasks[i].did) {
            cell.innerHTML += '<button onclick="didTask(' + i + ')">Сделано!</button>';
        } else {
            cell.innerHTML += '<button onclick="undidTask(' + i + ')">Переделать(</button>';
        }
    }
}

function createTaskWindow() {
    hideUpdateWindow();
    hideCategoryCreateWindow();
    hideCategoryUpdateWindow();
    hideShareTaskWindow();
    $('#modalCreateWindow').css('display', 'block');
    var select = document.getElementById("newSelect");
    select.innerHTML = '<option value="">Без категории</option>';
    for (var j = 0; j < categories.length; j++) {
        select.innerHTML += '<option value="' + categories[j].id + '">' + categories[j].name + '</option>';
    }

    document.getElementById('newTaskNameInput').value = '';
    document.getElementById('newSelect').value = '';
    document.getElementById('newDatePicker').value = '';
    document.getElementById('newTimePicker').value = '';
    document.getElementById('newPlaceArea').value = '';
    document.getElementById('newDescriptionArea').value = '';

}

function createCategoryWindow() {
    hideCategoryUpdateWindow();
    hideTaskCreateWindow();
    hideUpdateWindow();
    hideShareTaskWindow();
    $('#modalCreateCategoryWindow').css('display', 'block');

    document.getElementById('categoryNameInput').value = '';
    document.getElementById('categoryDescriptionArea').value = '';
}

function updateCategoryWindow() {
    hideTaskCreateWindow();
    hideUpdateWindow();
    hideCategoryCreateWindow();
    hideShareTaskWindow();
    $('#modalUpdateCategoryWindow').css('display', 'block');

    var select = document.getElementById('updateCategorySelect');
    select.innerHTML = "";
    for (var j = 0; j < categories.length; j++) {
        select.innerHTML += '<option value="' + j + '">' + categories[j].name + '</option>';
    }

    document.getElementById('categoryUpdateNameInput').value = categories[select.value].name;
    document.getElementById('categoryUpdateDescriptionArea').value = categories[select.value].description;
}

function setCategoryWindowProperties() {
    var select = document.getElementById('updateCategorySelect');
    document.getElementById('categoryUpdateNameInput').value = categories[select.value].name;
    document.getElementById('categoryUpdateDescriptionArea').value = categories[select.value].description;
}

function hideTaskCreateWindow() {
    $('#modalCreateWindow').css('display', 'none');
}

function hideCategoryUpdateWindow() {
    $('#modalUpdateCategoryWindow').css('display', 'none');
}

function hideCategoryCreateWindow() {
    $('#modalCreateCategoryWindow').css('display', 'none');
}

function updateTaskWindow(i) {
    hideTaskCreateWindow();
    hideCategoryCreateWindow();
    hideCategoryUpdateWindow();
    hideShareTaskWindow();
    $('#modalUpdateWindow').css('display', 'block');

    document.getElementById("windHeader").innerHTML = i + 1;

    document.getElementById("taskNameInput").value = tasks[i].name;
    document.getElementById("taskNameInput").style = 'background-color: #d6d6d6;';

    var select = document.getElementById("select");
    select.innerHTML = '<option value="">Без категории</option>';
    for (var j = 0; j < categories.length; j++) {
        if (tasks[i].category) {
            if (categories[j].id == tasks[i].category) {
                select.innerHTML += '<option selected value=' + categories[j].id + '>' +
                    categories[j].name + '</option>';
                continue;
            }
        }
        select.innerHTML += '<option value="' + categories[j].id + '">' + categories[j].name + '</option>';
    }

    if (tasks[i].doBefore) {
        var date = new Date(tasks[i].doBefore);
        var yyyy = date.getFullYear();
        var mm = (date.getMonth() + 1) < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1);
        var dd = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
        document.getElementById("datePicker").value = yyyy + '-' + mm + '-' + dd;
        var h = date.getHours() < 10 ? '0' + date.getHours() : date.getHours();
        var m = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes();
        document.getElementById("timePicker").value = h + ':' + m;
        console.log("b" + document.getElementById("datePicker").value);
    } else {
        document.getElementById("datePicker").value = null;
        document.getElementById("timePicker").value = null;
    }

    if (tasks[i].plase) {
        document.getElementById("placeArea").value = tasks[i].plase;
    } else {
        document.getElementById("placeArea").value = '';
    }
    document.getElementById("placeArea").style =
        'resize: none; width: 99%; height: 80px; background-color: #d6d6d6';

    if (tasks[i].description) {
        document.getElementById("descriptionArea").value = tasks[i].description;
    } else {
        document.getElementById("descriptionArea").value = '';
    }
    document.getElementById("descriptionArea").style =
        'resize: none; width: 99%; height: 80px; background-color: #d6d6d6';
}

function hideUpdateWindow() {
    $('#modalUpdateWindow').css('display', 'none');
}

function validateSomeField(textField, fieldName, min = 0, max = 135, nullable = true) {
    if (textField.value) {
        while (textField.value.charAt(0) == ' ') {
            textField.value = textField.value.substring(1, textField.value.length);
        }
        while (textField.value.charAt(textField.value.length - 1) == ' ') {
            textField.value = textField.value.substring(0, textField.value.length - 1);
        }
    }
    if (!nullable && !textField.value) {
        $('#' + textField.id).css('border-color', 'rgba(175, 0, 3, 0.64)');
        alert('Поле ' + (fieldName ? '"' + fieldName + '" ' : '') + 'обязательно к заполнению!');
        return false;
    } else {
        if (textField.value) {
            if (textField.value.length >= min && textField.value.length <= max) {
                $('#' + textField.id).css('border-color', '#d6d6d6');
                return true;
            } else {
                $('#' + textField.id).css('border-color', 'rgba(175, 0, 3, 0.64)');
                alert('Поле ' +
                    (fieldName ? '"' + fieldName + '" ' : '') +
                    'Вмещает в себя от ' + min + ' до ' + max);
                return false;
            }
        }
        return true;
    }
}


function shareTaskWindow(i) {
    hideUpdateWindow();
    hideTaskCreateWindow();
    hideCategoryCreateWindow();
    hideCategoryUpdateWindow();
    $('#modalShareTaskWindow').css('display', 'block');

    document.getElementById("windUpdateHeader").innerHTML = i + 1;
}

function hideShareTaskWindow() {
    $('#modalShareTaskWindow').css('display', 'none');
}
