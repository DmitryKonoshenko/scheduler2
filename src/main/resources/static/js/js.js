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
        firstName: '',
        secondName: '',
        email: '',
        password: '',
        pass2: '',
        profile: '',
        loginForm: false,
        regForm: false,
        loginMessage: ''
    },
    mounted() {
        this.fetchUsers();
    },
    methods: {
        fetchUsers() {
            if (this.profile !== '') {
                axios.get("/users").then(function (response) {
                    this.users = response.data;
                }.bind(this));
            }
        },
        login() {
            let request = new XMLHttpRequest();
            request.open('POST', 'api/v1/auth/login', false);
            request.setRequestHeader('Content-Type', 'application/json');
            let body = {
                "username": this.username,
                "password": this.password
            };
            let json = JSON.stringify(body);
            request.send(json);
            let response = JSON.parse(request.responseText);
            if (response.token != null) {
                this.loginForm = false;
                this.regForm = false;
                set_cookie("token", response.token);
                this.profile = response.username;
                console.log(response.username);
            }
            this.fetchUsers();
        },
        logout() {
            this.loginMessage = '';
            delete_cookie("token");
            document.location.replace("/");
        },
        registrationPrepare() {
            this.loginForm = false;
            this.regForm = true;
            // document.location.replace("/");
        }
        ,
        registration() {
            if (this.password === this.pass2) {
                let request = new XMLHttpRequest();
                request.open('POST', '/users/register', false);
                request.setRequestHeader('Content-Type', 'application/json');
                let body = {
                    "username": this.username,
                    "firstName": this.firstName,
                    "secondName": this.secondName,
                    "email": this.email,
                    "password": this.password,
                };
                let json = JSON.stringify(body);
                request.send(json);
                if (request.responseText === "true") {
                    this.loginMessage = "Вы успешно зарегестрированы.";
                    this.regForm = false;
                    this.loginForm = true;
                } else {
                    this.loginMessage = "Что-то пошло не так. Попробуйте снова";
                }

                // axios.post('/users/register', {
                //     "username": this.username,
                //     "firstName": this.firstName,
                //     "secondName": this.secondName,
                //     "email": this.email,
                //     "password": this.password,
                // }).then(function (response) {
                //     if(response.data.register === true){
                //     this.loginMessage = "Вы успешно зарегестрированы.";
                //     this.regForm = false;
                //     this.loginForm = true;
                //     }else{
                //         this.loginMessage = "Что-то пошло не так. Попробуйте снова";
                //     }
                // }).catch(function (error) {
                //     console.log(error);
                // });
            } else {
                this.loginMessage = "Пароли не совпадают";
            }
            console.log("keck keck");
        }
    },
    created: function() {
       let token = getCookie("token");
       if(token && token.length<64){
           let addr = "/users/" + token;
           let request = new XMLHttpRequest();
           request.open('GET',addr,false);
           request.send();
           console.log(request.status);
           this.profile = request.responseText;
       }
        this.loginForm = true;
    }
});