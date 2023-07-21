import React, {useContext, useState} from "react";
import {Toast, ToastContainer} from "react-bootstrap";
import {AuthContext} from "../context";


const CustomToast = ({children, type}) => {
    return (
        <Toast autohide={true}>
            <div className={"d-flex alert alert-" + type + " m-0 p-0"}>
                <Toast.Body>
                    {children}
                </Toast.Body>
                <button type="button" className="btn-close me-2 m-auto" data-bs-dismiss="toast"
                        aria-label="Close"></button>
            </div>
        </Toast>
    )
}
const AlertBar = () => {
    const {store} = useContext(AuthContext)
    const [alerts, setAlerts] = useState([])
    store.alerts = alerts;
    store.setAlerts = setAlerts;

    return (
        <ToastContainer className="position-fixed m-2" position="bottom-end">
            <Toast autohide={true}>
                {alerts.map((alert, ind) => (
                    <CustomToast key={ind} type={alert.type}>
                        {alert.children}
                    </CustomToast>
                ))}
            </Toast>
        </ToastContainer>
    )
}


export default AlertBar