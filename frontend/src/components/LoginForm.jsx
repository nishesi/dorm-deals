import React, {useContext, useState} from "react";
import {AuthContext} from "../context";
import bootstrap from "../../node_modules/bootstrap/dist/js/bootstrap.bundle.min";
import {Button, Modal} from "react-bootstrap";

const LoginForm = () => {
    const [show, setShow] = useState(false)
    const {store} = useContext(AuthContext)
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')

    async function onLogin(e) {
        setShow(false)
        await store.login(email, password)
    }

    return (
        <div>
            <Button onClick={() => setShow(true)}>
                Войти
            </Button>
            <Modal show={show} onHide={() => setShow(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>
                        <h1 className="modal-title fs-5" id="LogInModalLabel">Вход в аккаунт</h1>
                    </Modal.Title>
                </Modal.Header>
                <Modal.Body>
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
                </Modal.Body>
                <Modal.Footer>
                    <Button className="btn btn-primary" onClick={onLogin}>
                        Войти
                    </Button>
                </Modal.Footer>
            </Modal>
        </div>
    )
}

export default LoginForm