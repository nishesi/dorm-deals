import React, {useContext, useState} from "react";
import {AuthContext} from "../context";
import {Alert, Button, Container, InputGroup, Modal} from "react-bootstrap";
import Form from 'react-bootstrap/Form';

const LoginForm = () => {
    const [show, setShow] = useState(false)
    const {store} = useContext(AuthContext)
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')

    async function onSubmit() {
        store.login(email, password)
            .then(() => {
                setShow(false)
            }).catch(e => {
        })
    }

    return (
        <div>
            <Button onClick={() => setShow(true)}>
                Войти
            </Button>
            <Modal show={show} onHide={() => setShow(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>
                        Вход в аккаунт
                    </Modal.Title>
                </Modal.Header>
                <Modal.Body id="loginForm">
                    <Alert
                        variant="warning"
                        className="p-1 text-center" hidden>
                        Invalid email or password.
                    </Alert>

                    <InputGroup className="mb-3">
                        <InputGroup.Text>Email</InputGroup.Text>
                        <Form.Control placeholder="Email..."
                                      value={email}
                                      onChange={e => setEmail(e.target.value)}></Form.Control>
                    </InputGroup>

                    <InputGroup className="mb-3">
                        <InputGroup.Text>Password</InputGroup.Text>
                        <Form.Control placeholder="Password..."
                                      value={password}
                                      onChange={e => setPassword(e.target.value)}></Form.Control>
                    </InputGroup>
                </Modal.Body>
                <Modal.Footer>
                    <Button onClick={onSubmit}>
                        Войти
                    </Button>
                </Modal.Footer>
            </Modal>
        </div>
    )
}

export default LoginForm