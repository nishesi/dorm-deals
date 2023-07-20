import React, {useContext, useEffect, useState} from "react";
import './App.css';
import SearchProductService from "./API/SearchProductService";
import ProductsList from "./components/ProductsList";
import Header from "./components/Header";
import {AuthContext} from "./context";
import Store from "./store/Store";
import {observer} from "mobx-react-lite"

export const API_URL = "http://localhost/app"

function App() {
    const [products, setProducts] = useState([])

    useEffect(() => {
        SearchProductService.getByCriteria()
            .then((newProducts) => {
                setProducts(newProducts)
            })
    }, [])

    // Authorization

    const store = new Store();
    useEffect(() => {
        if (localStorage.getItem("token")) {
            store.checkAuth()
        }
    }, [])


    return (
        <div className="App">
            <AuthContext.Provider value={{
                store
            }}>
                <Header updateProducts={(pr) => setProducts(pr)}></Header>
                <ProductsList products={products}></ProductsList>
            </AuthContext.Provider>
        </div>
    );
}

export default App
