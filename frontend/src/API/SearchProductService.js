import axios from "axios";

export default class SearchProductService {
    static async getByCriteria(categories, nameQueries, shopIds, pageInd) {
        let params = {
            pageInd: pageInd !== undefined ? pageInd : 0
        }
        if (categories !== undefined && categories.length !== 0) {
            params = {
                categories : categories.join(",")
            }
        }
        if (nameQueries !== undefined && nameQueries.length !== 0) {
            params = {
                ...params,
                "name-query" : nameQueries.join(",")
            }
        }
        if (shopIds !== undefined && shopIds.length !== 0) {
            params = {
                ...params,
                "shop-id": shopIds.join(",")
            }
        }

        let requestConfig = {
            params: params
        }
        const response = await axios.get("http://localhost/app/search/products", requestConfig);
        return response.data;
    }
}