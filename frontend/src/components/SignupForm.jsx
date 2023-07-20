import React, {useContext, useState} from "react";
import {AuthContext} from "../context";

const SignupForm = () => {
    const {store} = useContext(AuthContext);
    const [newUser, setNewUser] = useState({
        email: "",
        password: "",
    });

    return (
        <div>
            <button type="button" className="btn btn-outline-primary" data-bs-toggle="modal" data-bs-target="#SignupModal">
                Регистрация
            </button>
            <div className="modal fade" id="SignupModal" tabIndex="-1" aria-labelledby="SignupModalLabel"
                 aria-hidden="true">
                <div className="modal-dialog">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h1 className="modal-title fs-5" id="LogInModalLabel">Регистрация на сайте</h1>
                            <button type="button" className="btn-close" data-bs-dismiss="modal"
                                    aria-label="Close"></button>
                        </div>
                        <div className="modal-body">
                            <div className="input-group mb-3">
                                <div className="input-group-text">Email</div>
                                <input type="text" className="form-control" placeholder="Email..."
                                       name="email" value={newUser.email} onChange={e => setNewUser({...newUser, email: e.target.value})}/>
                            </div>
                            <div className="input-group">
                                <div className="input-group-text">Password</div>
                                <input type="password" className="form-control" placeholder="Password"
                                       name="password" value={newUser.password} onChange={e => setNewUser({...newUser, password: e.target.value})}/>
                            </div>
                        </div>
                        <div className="modal-footer">
                            <button type="button" className="btn btn-primary"
                                    onClick={e => store.register(newUser)}>Зарегистрироваться
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default SignupForm