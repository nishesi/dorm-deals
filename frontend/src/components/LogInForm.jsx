import React, {useState} from "react";

const LogInForm = ({callBack}) => {
    const [logInData, setLogInData] = useState({email: "", password: ""});

    const logIn = function (event) {
        event.preventDefault();
        callBack(logInData)
    }

    return (
        <form>
            <div className="form m-4">
                <h4 className="text-center mb-3">Log In</h4>

                <div className="input-group mb-2">
                    <div className="input-group-text">Email</div>
                    <input type="text" className="form-control" placeholder="Write Email..." name="email"
                           onChange={(e) => setLogInData({...logInData, email: e.target.value})}
                           value={logInData.email}/>
                </div>

                <div className="input-group mb-2">
                    <div className="input-group-text">Password</div>
                    <input type="password" className="form-control" placeholder="Write Password..." name="password"
                           value={logInData.password}
                           onChange={(e) => setLogInData({...logInData, password: e.target.value})}/>
                </div>

                <div className="container text-center">
                    <button type="submit" className="btn btn-outline-primary" onClick={logIn}>Log In</button>
                </div>
            </div>
        </form>
    )
}

export default LogInForm