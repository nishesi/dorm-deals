import React, {useState} from "react";
import SearchProductService from "../API/SearchProductService";

const SearchBar = ({updateProducts}) => {
    const [queryString, setQueryString] = useState('')

    return (
        <div className={"searchBar"}>
            <div className="input-group flex-nowrap">
                <input
                    type={"text"}
                    name={"query"}
                    className="form-control"
                    value={queryString}
                    placeholder="Search"
                    onChange={(e) => setQueryString(e.target.value)}
                />
                <button
                    className="btn btn-outline-secondary"
                    onClick={() => {
                        let arr = queryString.split(" ")
                        SearchProductService.getByCriteria([], arr, [], 0)
                            .then((products) => {
                                updateProducts(products)
                            })
                    }}>
                    Find
                </button>
            </div>
        </div>
    )
}

export default SearchBar