import React, {useState} from "react";
import logo from './logo.svg';
import './App.css';
import ProductCover from "./components/ProductCoverItem";
import Shop from "./components/Shop";
import ProductCoverItem from "./components/ProductCoverItem";
import LogInForm from "./components/LogInForm";
import async from "async";
import axios from "axios";

function App() {
    const [likes, setLikes] = useState(5);
    const products = [
        {name: "Cheetos", price: 10.99, countInStorage: 1},
        {name: "Cheetos", price: 10.99, countInStorage: 1},
        {name: "Cheetos", price: 10.99, countInStorage: 1},
        {name: "Cheetos", price: 10.99, countInStorage: 1},
        {name: "Cheetos", price: 10.99, countInStorage: 1},
        {name: "Cheetos", price: 10.99, countInStorage: 1},
        {name: "Cheetos", price: 10.99, countInStorage: 1},
        {name: "Cheetos", price: 10.99, countInStorage: 1},
    ]
    const [users, setUsers] = useState([]);
    const addUser = (user) => {
        setUsers([...users, user]);
    }

    async function fetchPosts() {
        const resp = await axios.get("http://localhost/app/search/products?name-query=Товар");
        console.log(resp)
    }

    function incr() {
        setLikes(likes +    1)
    }

    // Product Search Block



    return (
        <div className="App">
            <LogInForm callBack={addUser}/>
            <button onClick={fetchPosts}>Click Me</button>
            <p>Users</p>
            {users.map((user)=>
                <div>{user.email + " " + user.password}</div>
            )}
            <div className="row row-cols-2 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 row-cols-xl-5 g-3 p-3">
                {products.map((pr, index) =>
                    <ProductCoverItem ind={index} productCover={pr}></ProductCoverItem>
                )}
            </div>
        </div>
    );
}

export default App;
