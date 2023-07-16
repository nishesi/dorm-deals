import axios from "axios";

export default class SearchProductService {
    static async getByCriteria(categories, nameQueries, shopIds, pageInd) {
        let requestConfig = {
            params: [...categories, ...nameQueries, ...shopIds, pageInd !== undefined ? pageInd : 0]
        }
        const response = await axios.get("http://localhost/app/search/products", requestConfig);
        return response.data;
    }
}