import React, {useEffect, useState} from "react";
import './App.css';
import LogInForm from "./components/LogInForm";
import axios from "axios";
import SearchProductService from "./API/SearchProductService";
import ProductsList from "./components/ProductsList";
import SearchBar from "./components/UI/SearchBar";

function App() {
    const [likes, setLikes] = useState(5);
    const [products, setProducts] = useState([])
    const [users, setUsers] = useState([]);
    const addUser = (user) => {
        setUsers([...users, user]);
    }

    async function fetchPosts() {
        const resp = await axios.get("http://localhost/app/search/products?name-query=Товар");
        console.log(resp)
    }

    // Product Search Block

    useEffect(() => {
        SearchProductService.getByCriteria([], ['Товар'], [], 0)
            .then((newProducts) => {
                setProducts(newProducts)
            })
    }, [])

    return (
        <div className="App">
            <SearchBar updateProducts={(pr) => setProducts(pr)}></SearchBar>
            <LogInForm callBack={addUser}/>
            <ProductsList products={products}></ProductsList>
        </div>
    );
}

export default App;
