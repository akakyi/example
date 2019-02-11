var authToken;
var user;
var tasks;
var categories;

window.onload = function (evt) {
    if (navigator.cookieEnabled) {
        doAuth();
        user = loadDataSync('http://127.0.0.1:8080/web-api/tsp/rest/user/login', 'GET');
        setUserNameSpan(user);
        tasks = loadTasks();

        categories = loadDataSync('http://127.0.0.1:8080/web-api/tsp/rest/category/user', 'GET');
        categories = categories == null ? [] : categories;
        redrawTasks(tasks, categories);
        document.getElementById("cancelButton").onclick = function (evt) {
            hideUpdateWindow();
        };
        document.getElementById("sendButton").onclick = function (evt) {
            if (updateTask()) {
                hideUpdateWindow();

                tasks = loadTasks();
                redrawTasks(tasks, categories);
            }
        };
        document.getElementById("newSendButton").onclick = function (evt) {
            if (createTask()) {
                hideTaskCreateWindow();

                tasks = loadTasks();
                redrawTasks(tasks, categories);
            }
        };
        document.getElementById("taskShareButton").onclick = function (evt) {
            if (shareTask()) {
                hideShareTaskWindow();
            }
        };
        document.getElementById("closeTaskShareButton").onclick = function (evt) {
            hideShareTaskWindow();
        };
        document.getElementById("createTaskButton").onclick = function (evt) {
            createTaskWindow();
            //document.getElementById("userName").innerHTML = "hui";
        };
        document.getElementById("newCancelButton").onclick = function (evt) {
            hideTaskCreateWindow();
        };
        document.getElementById("exitButton").onclick = function (evt) {
            exit();
        };
        // document.getElementById("taskNameInput").onblur = function (evt) {
        //     validateSomeField(document.getElementById("taskNameInput"), 'Имя', 1, 50, false);
        // };
        // document.getElementById("placeArea").onblur = function (evt) {
        //     validateSomeField(document.getElementById("placeArea"), 'Место', 0, 135, true);
        // };
        // document.getElementById("descriptionArea").onblur = function (evt) {
        //     validateSomeField(document.getElementById("descriptionArea"), 'Описание', 0, 135, true);
        // };
        // document.getElementById("newTaskNameInput").onblur = function (evt) {
        //     validateSomeField(document.getElementById("newTaskNameInput"), 'Имя', 1, 50, false);
        // };
        // document.getElementById("newPlaceArea").onblur = function (evt) {
        //     validateSomeField(document.getElementById("newPlaceArea"), 'Место', 0, 135, true);
        // };
        // document.getElementById("newDescriptionArea").onblur = function (evt) {
        //     validateSomeField(document.getElementById("newDescriptionArea"), 'Описание', 0, 135, true);
        // };
        // document.getElementById("categoryDescriptionArea").onblur = function (evt) {
        //     validateSomeField(document.getElementById("categoryDescriptionArea"), 'Описание', 0, 135, true);
        // };
        // document.getElementById("categoryNameInput").onblur = function (evt) {
        //     validateSomeField(document.getElementById("categoryNameInput"), 'Имя', 1, 50, false);
        // };
        document.getElementById("createCategoryButton").onclick = function (evt) {
            createCategoryWindow();
        };
        document.getElementById("categoryCancelButton").onclick = function (evt) {
            hideCategoryCreateWindow();
        };
        document.getElementById("categorySendButton").onclick = function (evt) {
            if (createCategory()) {
                hideCategoryCreateWindow();

                categories = loadDataSync('http://127.0.0.1:8080/web-api/tsp/rest/category/user', 'GET');
            }
        };
        document.getElementById("updateCategoryButton").onclick = function (evt) {
            if (categories) {
                updateCategoryWindow();
            }
        };
        document.getElementById("updateCategorySelect").onchange = function (evt) {
            setCategoryWindowProperties();
        };
        // document.getElementById("categoryUpdateDescriptionArea").onblur = function (evt) {
        //     validateSomeField(
        //         document.getElementById("categoryUpdateDescriptionArea"), 'Описание', 0, 135, true);
        // };
        // document.getElementById("categoryUpdateNameInput").onblur = function (evt) {
        //     validateSomeField(document.getElementById("categoryUpdateNameInput"), 'Имя', 1, 50, false);
        // };
        document.getElementById("updateCategoryCancelButton").onclick = function (evt) {
            hideCategoryUpdateWindow();
        };
        document.getElementById("updateCategorySendButton").onclick = function (evt) {
            if (updateCategory()) {
                hideCategoryUpdateWindow();

                categories = loadDataSync('http://127.0.0.1:8080/web-api/tsp/rest/category/user', 'GET');
                redrawTasks(tasks, categories);
            }
        };
        document.getElementById("deleteCategoryButton").onclick = function (evt) {
            deleteCategory();
            hideCategoryUpdateWindow();

            categories = loadDataSync('http://127.0.0.1:8080/web-api/tsp/rest/category/user', 'GET');
            redrawTasks(tasks, categories);
        }
    } else {
        alert("Cookie must be enabled for this site Лондонисзекепиталофгрейтбритан!");
    }
};

