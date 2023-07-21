import {User} from "../models/User";
import {makeAutoObservable} from "mobx";
import AuthService from "../API/AuthService"
import axios from "axios";
import {API_URL} from "../App";
import api from "../API";

export default class Store {
    user = null;
    isAuth = false;
    alerts;
    setAlerts;

    constructor() {
        makeAutoObservable(this);
    }

    setAuth(bool: boolean) {
        this.isAuth = bool
    }

    setUser(user: User) {
        this.user = user
    }

    async login(email: string, password: string) {
        await AuthService.login(email, password)
            .then(response => {
                localStorage.setItem("token", response.data.accessToken)
                this.setAuth(true)
                this.setUser(response.data.user)

            }).catch(e => {
                document.querySelector("#loginForm div.alert").removeAttribute("hidden")
                throw e;
        })
    }

    async logout() {
        this.user = null;
        this.isAuth = false;
        await AuthService.logout();
        localStorage.removeItem("token")
    }

    async register(newUser) {
        console.log(newUser)
    }

    async checkAuth() {
        axios.post(
            API_URL + "/auth/token", null, {withCredentials: true}
        ).then(resp => {
            localStorage.setItem("token", resp.data.accessToken)
            this.setAuth(true)
            this.setUser(resp.data.user)

        }).catch(e => {
            if (e.response.status !== 401) console.log(e)
        })
    }
}