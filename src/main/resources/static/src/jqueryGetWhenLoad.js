$(document).ready(function () {
    reload();

    $("#addUserButton").click(function () {
        let data = $('#newUserForm').serializeArray()
        let user = {};
        for (let i=0; i<data.length; i++) {
            if (data[i].name=="email") {
                user.email = data[i].value
            } else if (data[i].name=='password') {
                user.password = data[i].value
            } else if (data[i].name=='firstName') {
                user.firstName = data[i].value
            } else if (data[i].name=='lastName') {
                user.lastName = data[i].value
            } else if (data[i].name=='roles') {
                if (user.roles==null) {
                    user.roles=data[i].value+","
                } else {
                    user.roles+=data[i].value+","
                }
            }
        }
        addUser(user);
        reload();
    })

    $('[id^="editUserButton"]').click(function () {
        let id = $("[class='modal fade show']").attr("id").replace( /[^\d.]/g, '' )
        let data = $('#editUserForm'+id).serializeArray()
        let user = {};
        for (let i=0; i<data.length; i++) {
            if (data[i].name=="email") {
                user.email = data[i].value
            } else if (data[i].name=='password') {
                user.password = data[i].value
            } else if (data[i].name=='firstName') {
                user.firstName = data[i].value
            } else if (data[i].name=='lastName') {
                user.lastName = data[i].value
            } else if (data[i].name=='roles') {
                if (user.roles==null) {
                    user.roles=data[i].value+","
                } else {
                    user.roles+=data[i].value+","
                }
            }
        }

        editUser(user, id);
        reload();
    })

})

function reload() {
    $("#allUsers tr").remove();

    let url = "http://localhost:8080/api/";
    $.getJSON(url, function (data) {

        for (let i=0; i<data.length;i++) {
            let rolesString = "";
            for (let r=0; r<data[i].roles.length; r++) {
                rolesString += data[i].roles[r]["role"]+", ";
            }
            $("#allUsers").append(
                '<tr>\n' +
                '<th scope="row">'+data[i].id+'</th>\n' +
                '<td>'+data[i].firstName+'</td>\n' +
                '<td>'+data[i].lastName+'</td>\n' +
                '<td>'+data[i].email+'</td>\n' +
                '<td>'+rolesString+'</td>\n' +
                '<td><button  type="button" id="editButtonOnTable" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#editModal'+data[i].id+'" >Edit</button></td>\n'+
                '<td>\n' +
                '    <form>\n' +
                '        <a type="button" href="javascript: void(0);" onclick="onClickDelete('+data[i].id+')" class="btn btn-danger">Delete</a>\n' +
                '    </form>\n' +
                '</td>\n' +
                '</tr>'
            )
        }
    })
}


function onClickDelete(id) {
    $.ajax({
        type: "DELETE",
        url:"http://localhost:8080/api/"+id,
    }).then(function () {
        reload()
    })
}

function addUser(userData) {
    $.ajax({
        type: "POST",
        contentType: "application/json; charset=utf-8",
        url: "http://localhost:8080/api/",
        data: JSON.stringify(userData),
    })
}

function editUser(userData, id) {
    $.ajax({
        type: "PUT",
        contentType: "application/json; charset=utf-8",
        url: "http://localhost:8080/api/"+id,
        data: JSON.stringify(userData)
    })
}

