import React, {useContext, useState} from "react";
import {AuthContext} from "../context";
import {Alert, Button, Modal} from "react-bootstrap";
import Form from "react-bootstrap/Form";

const SignupForm = () => {
    const [show, setShow] = useState(false)
    const [validated, setValidated] = useState(false);
    const {store} = useContext(AuthContext);
    const [newUser, setNewUser] = useState({
        email: "",
        password: "",
        firstName: "",
        lastName: "",
        telephone: "",
    });

    const fields = {
        email: {
            name: "Email",
            placeholder: "Email..."
        },
        password: {
            name: "Password",
            placeholder: "Email..."
        },
        firstName: {
            name: "First Name",
            placeholder: "Email..."
        },
        lastName:{
            name: "Last Name",
            placeholder: "Email..."
        },
        telephone: {
            name: "Phone",
            placeholder: "Email..."
        },
    }

    async function onSubmit() {
        await store.register(newUser)
            .then(message => {
                store.setAlerts([...store.alerts, {children: message, type: "success"}])
                setShow(false)
            })
            .catch(e => {
                if (e.response.status === 422) {
                    const errors = e.response.data.errors
                    const fieldElements = document.querySelectorAll("#signupForm div.form-group")

                    // set valid fields that has not errors from server
                    fieldElements.forEach(field => {
                        let valid = true;
                        for (let i = 0; i < errors.length; i++) {
                            console.log(field.id + " " + errors[i].field  + " " + (errors[i].field === field.id) + "")

                            if (errors[i].field === field.id) {
                                valid = false;
                                break;
                            }
                        }
                        console.log("result: " + valid)
                        if (valid) {
                            const classes = document.querySelector("#signupForm #"+ field.id +" input").classList
                            classes.add("is-valid")
                            classes.remove("is-invalid")
                        }
                    })

                    // set invalid fields and their messages from server
                    errors.forEach((error, key) => {
                        const feedbackEl = document.querySelector("#signupForm #" + error.field +" div.invalid-feedback")
                        feedbackEl.innerText = error.message
                        const input = document.querySelector("#signupForm #" + error.field +" input")
                        input.classList.add("is-invalid")
                        input.classList.remove("is-valid")
                    })
                }
            })
    }

    const handleSubmit = async (event) => {
        const form = event.currentTarget;
        if (form.checkValidity() === false) {
            event.preventDefault();
            event.stopPropagation();
        }
        await onSubmit()
    };

    return (
        <>
            <Button variant="outline-primary" onClick={() => setShow(true)}>
                Регистрация
            </Button>
            <Modal show={show} onHide={() => setShow(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>
                        Регистрация на сайте
                    </Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form id="signupForm" noValidate validated={validated} onSubmit={handleSubmit}>
                        <Alert
                            variant="warning"
                            className="p-1 text-center" hidden>
                        </Alert>

                        {Object.keys(fields).map(key => (
                            <Form.Group className="form-group" id={key} key={key} md="4">
                                <Form.Label>{fields[key].name}</Form.Label>
                                <Form.Control
                                    type="text"
                                    placeholder={fields[key].placeholder}
                                    onChange={e => setNewUser({...newUser, [key]: e.target.value})}
                                />
                                <Form.Control.Feedback type="valid">Looks good!</Form.Control.Feedback>
                                <Form.Control.Feedback type="invalid"></Form.Control.Feedback>
                            </Form.Group>
                        ))}
                        <Button className="mt-2" variant="outline-primary" type="submit">
                            Войти
                        </Button>
                    </Form>
                </Modal.Body>
            </Modal>
        </>
    )
}

export default SignupForm