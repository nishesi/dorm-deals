import axios from "axios";

export const API_URL = "http://localhost/app"

const api = axios.create({
    withCredentials: true,
    baseURL: API_URL
})

api.interceptors.request.use((config) => {
    if (localStorage.getItem("token") !== null)
        config.headers.Authorization = `Bearer ${localStorage.getItem("token")}`
    return config
})

export default api;