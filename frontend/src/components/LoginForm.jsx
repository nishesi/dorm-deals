import React, {useContext, useState} from "react";
import axios from "axios";
import {API_URL} from "../App";
import {AuthContext} from "../context";
import {observer} from "mobx-react-lite";

const LoginForm = () => {
    const {store} = useContext(AuthContext)
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')

    function onSubmit() {
        axios.post(API_URL + "/auth/token", "email=" + email + "&password=" + password, {
            withCredentials: true
        })
            .then((resp) => {
                console.log(resp.data)
            })
            .catch(err => {
                console.log(err)
            })
    }

    return (
        <div>
            <button type="button" className="btn btn-primary" data-bs-toggle="modal" data-bs-target="#LogInModal">
                Войти
            </button>
            <div className="modal fade" id="LogInModal" tabIndex="-1" aria-labelledby="LogInModalLabel"
                 aria-hidden="true">
                <div className="modal-dialog">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h1 className="modal-title fs-5" id="LogInModalLabel">Вход в аккаунт</h1>
                            <button type="button" className="btn-close" data-bs-dismiss="modal"
                                    aria-label="Close"></button>
                        </div>
                        <div className="modal-body">
                            <div className="input-group mb-3">
                                <div className="input-group-text">Email</div>
                                <input type="text" className="form-control" placeholder="Email..."
                                       name="email" value={email} onChange={e => setEmail(e.target.value)}/>
                            </div>
                            <div className="input-group">
                                <div className="input-group-text">Password</div>
                                <input type="password" className="form-control" placeholder="Password"
                                       name="password" value={password} onChange={e => setPassword(e.target.value)}/>
                            </div>
                        </div>
                        <div className="modal-footer">
                            <button type="button" className="btn btn-primary"
                                    onClick={e => store.login(email, password)}>Войти
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default LoginForm