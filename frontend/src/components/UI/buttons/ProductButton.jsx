import React from "react";
import classes from "./ProductButton.module.css"

const ProductButton = ({children, ...props}) => {
    return (
        <button type={"button"} {...props} className={classes.ProductButton + " btn btn-outline-primary"}>
            {children}
        </button>
    )
}

export default ProductButton