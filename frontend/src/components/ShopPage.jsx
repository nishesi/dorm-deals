import React, {useContext, useState} from "react";
import ProductsList from "./ProductsList";
import ShopService from "../API/ShopService";

const ShopPage = ({shopWithProducts}) => {
    return (
        <>
            <div className="container-fluid">
                <img className="rounded-circle d-inline-block"
                     src={shopWithProducts.shop.resourceUrl}
                     style={{width: 100, height: 100}}
                     alt="channel icon"/>
                <div className="d-inline-block ms-2 mt-4">
                    <h2>{shopWithProducts.shop.name}</h2>
                    <p>Rating: {shopWithProducts.shop.rating}</p>
                </div>
                {/*<div class="btn-group-vertical d-inline float-end m-2">*/}

                {/*    {% if channel.id equals user.channelId %}*/}

                {/*    <a href="{{ resolve('VC#getAddVideoPage') }}"*/}
                {/*       class="btn btn-outline-secondary" aria-current="true">*/}
                {/*        Add video*/}
                {/*    </a>*/}

                {/*    {% else %}*/}

                {/*    <form action="{{ resolve('UC#subscribeToChannel', queryParams = {'channelId': channel.id}) }}"*/}
                {/*          method="post">*/}
                {/*        <input type="hidden" name="{{ _csrf.parameterName }}" value="{{ _csrf.token }}">*/}
                {/*            <input type="hidden" name="channelId" value="{{ channel.id }}">*/}
                {/*                <input class="btn {{channel.isSubscribed ? 'btn-outline-danger' : 'btn-primary' }}"*/}
                {/*                       type="submit"*/}
                {/*                       value="{{ channel.isSubscribed ? 'Unsubscribe' : 'Subscribe' }}">*/}
                {/*    </form>*/}

                {/*    {% endif %}*/}
                {/*</div>*/}
            </div>
            <div className="container text-center">
                <h4 className="m-4 mb-0 text-muted">Description</h4>
                <p className=" m-4 mt-0">
                    {shopWithProducts.shop.description}
                </p>
            </div>
            <div className="container text-center">
                Точки: {shopWithProducts.shop.dormitories?.join(", ")}
            </div>

            <ProductsList products={shopWithProducts.productsPage.products}></ProductsList>
        </>
    )
}

export default ShopPage