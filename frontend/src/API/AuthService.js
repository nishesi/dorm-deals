import api from "./index"
import {AxiosError, AxiosResponse} from "axios";
import {AuthResponse} from "../models/AuthResponse"
import {API_URL} from "../App";

export default class AuthService {
    static async login(email, password): Promise<AxiosResponse<AuthResponse>> {
        return await api.post(API_URL + "/auth/token", "email=" + email + "&password=" + password, {
            withCredentials: true,
        })
    }

    static async registration(newUser) {
        return await api.post(API_URL + "/user", newUser)
    }

    static async logout(): Promise<void> {

    }
}