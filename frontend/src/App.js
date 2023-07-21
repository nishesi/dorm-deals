import React, {useEffect, useState} from "react";
import './App.css';
import SearchProductService from "./API/SearchProductService";
import {AuthContext} from "./context";
import Store from "./store/Store";
import Header from "./components/Header";
import ProductsList from "./components/ProductsList";
import AlertBar from "./components/AlertBar";

export const API_URL = "http://localhost/app"
const store = new Store();

function App() {

    const [products, setProducts] = useState([])

    // useEffect(() => {
    //     SearchProductService.getByCriteria()
    //         .then((newProducts) => {
    //             setProducts(newProducts)
    //         })
    // }, [])

    // Authorization

    useEffect( () => {
        if (localStorage.getItem("token")) {
             store.checkAuth()
        }
    }, [])

    return (
        <div className="App">
            <AuthContext.Provider value={{
                store
            }}>
                <AlertBar></AlertBar>
                <Header updateProducts={(pr) => setProducts(pr)}></Header>
                <ProductsList products={products}></ProductsList>
            </AuthContext.Provider>
        </div>
    );
}

export default App
