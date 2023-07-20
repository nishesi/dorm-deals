import React, {useContext} from "react";
import SearchBar from "./SearchBar";
import LoginForm from "./LoginForm";
import {AuthContext} from "../context";
import SignupForm from "./SignupForm";
import {observer} from "mobx-react-lite";
import {Button, Container, Nav, Navbar} from "react-bootstrap";

const Header = ({updateProducts}) => {
    const {store} = useContext(AuthContext);
    const user = store.user

    function logout() {
        store.logout()
    }

    return (
        <Navbar expand="md" className="bg-body-tertiary">
            <Container>
                <Navbar.Brand href="#home">
                    <img id="logo" src={"logo192.png"} alt="logo" style={{width: 50, height: 50}}/>
                    <strong>Dorm Deals</strong>
                </Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav"/>
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="me-auto">
                        <Nav.Item className="mt-2">
                            <SearchBar updateProducts={updateProducts}></SearchBar>
                        </Nav.Item>

                        {store.isAuth
                            ? <Nav.Item className="m-2">
                                <div>
                                    {user.firstName + " " + user.lastName}
                                    <img className="rounded-circle" width="40" height="40" src={user.imgUrl}
                                         alt="user img"/>
                                </div>
                                <Button onClick={logout}>Logout</Button>
                            </Nav.Item>

                            : <Nav.Item>
                                <div className="d-flex m-2">
                                    <div className="me-2">
                                        <SignupForm></SignupForm>
                                    </div>
                                    <div>
                                        <LoginForm></LoginForm>
                                    </div>
                                </div>
                            </Nav.Item>
                        }
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    )
}

export default observer(Header)