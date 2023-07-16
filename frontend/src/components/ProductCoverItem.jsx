import React from 'react';
import ProductButton from "./UI/buttons/ProductButton";
import classes from "./ProductCover.module.css";

const ProductCoverItem = ({productCover, ind, ...props}) => {
    return (
        <div key={ind} className={ classes.ProductCover + " rounded-2 border border-success border-1 p-1"}>
            <img  style={{width:150, height:150}} src={"logo192.png"}/>
            <div className={"productName"}>{productCover.name}</div>
            <div className={"price"}>{productCover.price}</div>
            <div className={"countInStorage"}>{productCover.countInStorage}</div>
            <ProductButton onClick={(event) => {
                console.log("product added")
            }}>
                "Add to cart"
            </ProductButton>
        </div>
    );
};

export default ProductCoverItem;