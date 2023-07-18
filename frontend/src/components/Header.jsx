import React from "react";
import SearchBar from "./SearchBar";
import LogIn from "./LogIn";

const Header = ({updateProducts}) => {
    return (
        <header className="container-fluid">
            <nav className="navbar navbar-expand-md bg-light">
                <div className="container-fluid">
                    <a className="navbar-brand" href="#">
                        <img id="logo" src={"logo192.png"} alt="logo" style={{width: 50, height: 50}}/>
                        <strong>Dorm Deals</strong></a>

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
                        <ul>
                            <LogIn></LogIn>
                        </ul>

                        {/*/!*{% if user is not null %}*!/*/}

                        {/*<div className="navbar-brand fs-5 ms-md-5 btn" type="button" data-bs-toggle="offcanvas"*/}
                        {/*     data-bs-target="#offcanvasRight" aria-controls="offcanvasRight">*/}
                        {/*    Nurislam Zaripov*/}
                        {/*    <img className="rounded-circle" width="40" height="40" src="{{ user.userImgUrl }}"*/}
                        {/*         alt="user image"/>*/}
                        {/*</div>*/}

                        {/*/!*{% else  %}*!/*/}

                        {/*<ul className="navbar-nav ms-md-5 mb-2 mb-md-0">*/}
                        {/*    /!*{% if request.contextPath + request.servletPath != resolve('UC#getRegistrationPage') %}*!/*/}
                        {/*    <li className="nav-item me-2">*/}
                        {/*        <a type="button" className="btn btn-outline-secondary"*/}
                        {/*           href="{{ resolve('UC#getRegistrationPage') }}">*/}
                        {/*            <strong>Register</strong>*/}
                        {/*        </a>*/}
                        {/*    </li>*/}
                        {/*    /!*{% endif %}*!/*/}
                        {/*    /!*{% if request.servletPath != '/login' %}*!/*/}
                        {/*    <li className="nav-item">*/}
                        {/*        <a type="button" className="btn btn-outline-primary"*/}
                        {/*           href="{{ resolve('SC#getLoginPage') }}">*/}
                        {/*            <strong>Login</strong>*/}
                        {/*        </a>*/}
                        {/*    </li>*/}
                        {/*    /!*{% endif %}*!/*/}
                        {/*</ul>*/}

                        {/*/!*{% endif %}*!/*/}
                    </div>
                </div>
            </nav>
        </header>
    )
}

export default Header