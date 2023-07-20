import {User} from "../models/User";
import {makeAutoObservable} from "mobx";
import AuthService from "../API/AuthService"
import axios from "axios";
import {API_URL} from "../App";
import type {AuthResponse} from "../models/AuthResponse";

export default class Store {
    user;
    isAuth = false;

    constructor(props) {
        makeAutoObservable(this);
    }

    setAuth(bool: boolean) {
        this.isAuth = bool
    }

    setUser(user: User) {
        this.user = user
    }

    async login(email: string, password: string) {
        try {
            const response = await AuthService.login(email, password)
            console.log(response)
            localStorage.setItem("token", response.data.accessToken)
            this.setAuth(true)
            this.setUser(response.data.user)
        } catch (e) {
            console.log(e)
        }
    }

    async register(newUser) {
        console.log(newUser)
    }

    async checkAuth() {
        axios.post(
            API_URL + "/auth/token", "", {withCredentials: true}
        ).then(resp => {
            localStorage.setItem("token", resp.data.accessToken)
            this.setAuth(true)
            this.setUser(resp.data.user)

        }).catch(e => {
            if (e.response.status !== 401) console.log(e)
        })
    }
}