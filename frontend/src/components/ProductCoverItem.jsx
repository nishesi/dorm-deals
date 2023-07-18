import React from 'react';
import ProductButton from "./UI/buttons/ProductButton";
import classes from "./ProductCover.module.css";

const ProductCoverItem = ({productCover, ...props}) => {
    return (
        <div className={ classes.ProductCover + " rounded-2 border border-success border-1 p-1 me-3"}>
            <img  style={{width:150, height:150}} src={"logo192.png"}/>
            <div className={"productName"}>{productCover.name}</div>
            <div className={"price"}>Цена: {productCover.price}</div>
            <div className={"countInStorage"}>Осталось: {productCover.countInStorage} шт.</div>
            <ProductButton onClick={(event) => {
                console.log("product added")
            }}>
                Add to cart
            </ProductButton>
        </div>
    );
};

export default ProductCoverItem;