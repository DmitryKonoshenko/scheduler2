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

Vue.component('date-row', {
    props: ['date', 'workDatesDesireds'],
    template: '<div>' +
        '<i>{{ date.date }}</i>' +
        '<span style="position: absolute; right:0;">' +
        '<input type="submit" value="X" v-on:click="del" class="btn-small"/> ' +
        '</span>' +
        '</div>',
    methods: {
        del: function () {
            let token = getCookie("token");
            let addr = "/users/deleteDate/" + token + "/" + this.date.id;
            axios.get(addr).then(function (response) {
                this.workDatesDesireds = response.data.workDatesDesireds;
                document.location.replace("/");
            }.bind(this));
        }
    }
});

var app = new Vue({
    el: "#root",
    template:
        '<div>' +
        '<div class="row">' +
        '<div class="twelve columns">' +
        '<ul class="navMenu">' +
        '<li><p id="loggedin">{{profile}}</p></li>' +
        '<li/>' +
        '<li><input v-if="profile" class="button-primary" type="submit"' +
        'v-on:click="logout" value="Выйти" style="position: absolute; right: 0;"/></li>' +
        '</ul>' +
        '</div>' +
        '</div>' +
        '<p v-if="loginMessage">{{loginMessage}}</p>' +
        '<div class="container" v-if="profile">' +
        '<div class="row">' +
        '<div class="twelve columns">' +
        '<h1>Расписание</h1>' +
        '<ul>' +
        '<li><h3>Добро пожаловать {{users.lastName}} {{users.firstName}}</h3></li>' +
        '</ul>' +
        '<ul>' +
        '<li><p>Вы работаете в этом месяце:</p></li>' +
        '<li v-for="date in workDates">{{date.date}} на машине номер: {{date.car.id}} с госномером {{date.car.number}}</li>' +
        '</ul>' +
        '<ul>' +
        '<li><p>Вы хотите работать в следующем месяце:</p></li>' +
        '<li v-if="moreDates"><input type="date" id="desireDate"><input class="button" type="submit"' +
        'v-on:click="addDate" style="position: absolute; right: 0;"' +
        'value="Добавить"/></li>' +
        '</ul>' +
        '<date-row v-for="date in workDatesDesireds" :key="date.id" ' +
        ':date="date" :workDatesDesireds="workDatesDesireds"/>' +
        '<ul>' +
        '<li><p>Введите даты замены смен:</p></li>' +
        '<li>' +
        '<label>Желаемая дата работы</label><input type="date" id="desireDate"><input type="submit" value="Добавить" class="button" style="position: absolute; right: 0px;">' +
        '<div><i>2019-12-20</i><span style="position: absolute; right: 0px;"><input type="submit" value="X" class="btn-small"></span></div>'+
        '<label>Желаемая дата снятия смены</label><input type="date" id="desireDate"><input type="submit" value="Добавить" class="button" style="position: absolute; right: 0px;">' +
        '<div><i>2019-12-19</i><span style="position: absolute; right: 0px;"><input type="submit" value="X" class="btn-small"></span></div>'+
        '</li>' +
        '</ul>'+
        '</div>' +
        '</div>' +
        ' </div>' +
        '<div class="container" v-if="loginForm">' +
        '<div class="row">' +
        '<div class="twelve columns">' +
        '<h4 id="loginHeader">Вход</h4>' +
        '<div class="six columns align-center">' +
        '<label for="username">Имя пользователя</label>' +
        '<input v-model="username" class="u-full-width" type="text" placeholder="yourUsername"' +
        'id="username"/>' +
        '<label for="password">Пароль</label>' +
        '<input v-model="password" class="u-full-width" type="password" id="password"/>' +
        '<input class="u-full-width button-primary" type="submit" v-on:click="login" value="Войти"/>' +
        '<input class="u-full-width button-primary" type="submit" v-on:click="registrationPrepare" value="Регистрация"/>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '<div class="container" v-if="regForm">' +
        '<div class="row">' +
        '<div class="twelve columns">' +
        '<h4 id="RegistrationHeader">Регистрация</h4>' +
        '<div class="six columns align-center">' +
        '<input v-model="username" class="u-full-width" type="text" placeholder="Имя" id="логин"/>' +
        '<input v-model="firstName" class="u-full-width" type="text" placeholder="Имя" id="regFirstName"/>' +
        '<input v-model="secondName" class="u-full-width" type="text" placeholder="Фамилия" id="regSecondName"/>' +
        '<input v-model="email" class="u-full-width" type="text" placeholder="электронная почта" id="email"/>' +
        '<input v-model="password" class="u-full-width" type="password" placeholder="введите пароль" id="pass1"/>' +
        '<input v-model="pass2" class="u-full-width" type="password" placeholder="повторите пароль" id="pass2"/>' +
        '<input class="u-full-width button-primary" type="submit" v-on:click="registration" value="Submit"/>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '</div>',
    data: {
        users: [],
        workDates: [],
        workDatesDesireds: [],
        username: '',
        firstName: '',
        secondName: '',
        email: '',
        password: '',
        pass2: '',
        profile: '',
        loginForm: false,
        regForm: false,
        loginMessage: '',
        moreDates: true
    },
    mounted() {
        this.fetchUsers();
    },
    methods: {
        fetchUsers() {
            if (this.profile !== '') {
                let token = getCookie("token");
                let addr = "/users/user/" + token;
                axios.get(addr).then(function (response) {
                    this.users = response.data;
                    this.workDates = this.users.workDates;
                    this.workDatesDesireds = this.users.workDatesDesireds;
                    let counter = 0;
                    for (let key in this.workDatesDesireds) {
                        counter++;
                    }
                    if (counter > 9) this.moreDates = false;
                    console.log(this.users);
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
            } else {
                this.loginMessage = "Пароли не совпадают";
            }
            console.log("keck keck");
        },
        addDate() {
            let dateC = document.getElementById("desireDate").value;
            if (dateC !== "") {
                let uniq = true;
                for (let key in this.workDatesDesireds) {
                    if (this.workDatesDesireds[key].date === dateC) uniq = false;
                }
                if (uniq) {
                    let token = getCookie("token");
                    let addr = "/users/addDate/" + token + "/" + dateC;
                    axios.get(addr).then(function (response) {
                        document.location.replace("/");
                    }.bind(this));
                }
            }
        }
    },
    created: function() {
        let token = getCookie("token");
        if (token) {
            let addr = "/users/" + token;
            let request = new XMLHttpRequest();
            request.open('GET', addr, false);
            request.send();
            console.log(request.status);
            if (request.status === 200) {
                this.profile = request.responseText;
            } else this.loginForm = true;
        } else this.loginForm = true;
    }
});