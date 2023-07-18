import React, {useState} from "react";
import axios from "axios";
import {apiUrl} from "../App";

const LogIn = () => {
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')

    function onSubmit() {
        axios.post(apiUrl + "/auth/token", "email=" + email + "&password=" + password)
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
                            <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
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
                            <button type="button" className="btn btn-primary" onClick={onSubmit}>Войти</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default LogIn