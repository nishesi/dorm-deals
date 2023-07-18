import React, {useEffect, useState} from "react";
import './App.css';
import SearchProductService from "./API/SearchProductService";
import ProductsList from "./components/ProductsList";
import Header from "./components/Header";

export const apiUrl = "http://localhost/app"

function App() {
    const [products, setProducts] = useState([])

    useEffect(() => {
        SearchProductService.getByCriteria()
            .then((newProducts) => {
                setProducts(newProducts)
            })
    }, [])

    // Authorization





    return (
        <div className="App">
            <Header updateProducts={(pr) => setProducts(pr)}></Header>
            <ProductsList products={products}></ProductsList>
        </div>
    );
}

export default App;
