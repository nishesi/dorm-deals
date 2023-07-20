import React, {useContext} from "react";
import SearchBar from "./SearchBar";
import LoginForm from "./LoginForm";
import {AuthContext} from "../context";
import SignupForm from "./SignupForm";
import {observer} from "mobx-react-lite";

const Header = ({updateProducts}) => {
    const {store} = useContext(AuthContext);
    const user = store.user

    return (
        <header className="container-fluid">
            <nav className="navbar navbar-expand-md bg-light">
                <div className="container-fluid">
                    <a className="navbar-brand" href="#">
                        <img id="logo" src={"logo192.png"} alt="logo" style={{width: 50, height: 50}}/>
                        <strong>Dorm Deals</strong>
                    </a>

                    <button className="navbar-toggler" type="button" data-bs-toggle="collapse"
                            data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                            aria-expanded="false" aria-label="Toggle navigation">
                        <span className="navbar-toggler-icon"></span>
                    </button>

                    <div className="collapse navbar-collapse" id="navbarSupportedContent">
                        <ul className="navbar-nav me-auto mb-2 mb-md-0">

                            <li className="nav-item ms-md-5">
                                <SearchBar updateProducts={updateProducts}></SearchBar>
                            </li>
                        </ul>

                        {store.isAuth
                            ? <button className="navbar-brand fs-5 ms-md-5 btn" type="button" data-bs-toggle="offcanvas"
                                   data-bs-target="#offcanvasRight" aria-controls="offcanvasRight">
                                {user.firstName + " " + user.lastName}
                                <img className="rounded-circle" width="40" height="40" src={user.imgUrl}
                                     alt="user image"/>
                            </button>

                            : <div className="d-flex ms-md-5 mb-2 mb-md-0">
                                <div className="me-2">
                                    <SignupForm></SignupForm>
                                </div>
                                <div>
                                    <LoginForm></LoginForm>
                                </div>
                            </div>
                        }
                    </div>
                </div>
            </nav>
        </header>
    )
}

export default observer(Header)