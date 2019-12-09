function getCookie(name) {
    var value = "; " + document.cookie;
    var parts = value.split("; " + name + "=");
    if (parts.length == 2) return parts.pop().split(";").shift();
}

function set_cookie(name, value) {
    document.cookie = name + '=' + value + '; Path=/;';
}

function delete_cookie(name) {
    document.cookie = name + '=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}

var app = new Vue({
    el: "#root",
    data: {
        users: [],
        username: '',
        password: '',
        profile: ''
    },
    mounted() {
        this.fetchUsers();
    },
    methods: {
        fetchUsers() {
            axios.get("/users").then(function (response) {
                this.users = response.data;
            }.bind(this));
        },
        login() {
            axios.post('api/v1/auth/login', {
                "username": this.username,
                "password": this.password
            })
                .then(function (response) {
                    set_cookie("token", response.data.token);
                    document.location.replace("/");
                })
                .catch(function (error) {
                    console.log(error);
                });
        },
        logout(){
            delete_cookie("token");
            document.location.replace("/");
        }
    },
    created: function() {
       let token = getCookie("token");
       if(token){
           let addr = "/users/"+token;
           var request = new XMLHttpRequest();
           request.open('GET',addr,false);
           request.send();
           console.log(request.status);
           this.profile = request.responseText;
       }
    }
});