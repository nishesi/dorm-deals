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
        AuthService.login(email, password)
            .then(response => {
                // console.log(response)
                localStorage.setItem("token", response.data.accessToken)
                this.setAuth(true)
                this.setUser(response.data.user)

            }).catch(e => {
                this.setAlerts([...this.alerts, {type: "danger", children: e.response.status + "Authorization failed :("}])
        })
    }

    async logout() {
        this.user = null;
        this.isAuth = false;
        const resp = await api.post(API_URL + "/logout");
        localStorage.removeItem("token")
        console.log(resp)
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