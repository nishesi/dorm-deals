import React, {useState} from "react";
import SearchProductService from "../API/SearchProductService";
import {Button, InputGroup, Form} from "react-bootstrap";
import IndexUmd from "bootstrap/js/index.umd";

const SearchBar = ({updateProducts}) => {
    const [queryString, setQueryString] = useState('')

    return (
        <InputGroup>
            <Form.Control
                placeholder="Search"
                value={queryString}
                onChange={(e) => setQueryString(e.target.value)}>
            </Form.Control>
            <Button
                onClick={() => {
                    let arr = queryString.split(" ")
                    SearchProductService.getByCriteria([], arr, [], 0)
                        .then((products) => {
                            updateProducts(products)
                        })
                }}>
                Find
            </Button>
        </InputGroup>
    )
}

export default SearchBar