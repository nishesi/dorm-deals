import api from "./index";
import {API_URL} from "../App";

export default class ShopService {
    static async getMainPage(shopId) {
        return await api.get(API_URL + `/shops/${shopId}`)
    }
    static async update(shop) {}
    static async createShop(shop) {}
    static async deleteMyShop() {}
    static async updateIcon() {}
    static async getShopOrders(shopId) {}
}