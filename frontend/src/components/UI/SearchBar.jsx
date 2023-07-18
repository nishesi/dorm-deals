import React, {useState} from "react";
import SearchProductService from "../../API/SearchProductService";

const SearchBar = ({updateProducts}) => {
    const [queryString, setQueryString] = useState('')

    return (
        <div className={"searchBar"}>
            <input type={"text"} name={"query"} value={queryString} onChange={(e) => setQueryString(e.target.value)}/>
            <button onClick={() => {
                let arr = queryString.split(" ")
                SearchProductService.getByCriteria([], arr, [], 0)
                    .then((products) => {
                        updateProducts(products)
                    })
            }}>
                Find
            </button>
        </div>
    )
}

export default SearchBar