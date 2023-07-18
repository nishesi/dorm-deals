import React from "react";
import ProductCoverItem from "./ProductCoverItem";

const ProductsList = ({products}) => {
    return (
        <div className="row row-cols-2 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 row-cols-xl-5 g-3 p-3">
            {products.map((pr, index) => (
                    <ProductCoverItem key={pr.id} productCover={pr}></ProductCoverItem>
                )
            )}
        </div>
    )
}

export default ProductsList