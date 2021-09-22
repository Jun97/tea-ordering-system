import { Injectable } from '@angular/core'
import { Http, Headers, RequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import { OrderModel } from '../model/order.model';

@Injectable()
export class OrderItemService
{
    static readonly URL = 'http://localhost:50001/tos-rest/api/tea-session/order/order-item';

    constructor( private http:Http) {}

    addOrderItem(orderId: number, menuItemId: number, quantity: number):Observable<Map<string,any>>
    {
        const headers = new Headers();
        headers.append('Content-Type', 'application/x-www-form-urlencoded');

        const params = new URLSearchParams();
        params.append('orderId', <string><any>orderId);
        params.append('menuItemId', <string><any>menuItemId);
        params.append('quantity', <string><any>quantity);
        return this.http.post(`${OrderItemService.URL}/add`,params.toString(), { headers: headers }).map(res => res.json());
    }

    findOrderItemByTeaSessionId(orderId: number):Observable<Map<string,any>>
    {
        const headers = new Headers();
        headers.append('Content-Type', 'application/x-www-form-urlencoded');

        const params = new URLSearchParams();
        params.append('orderId', <string><any>orderId);
        return this.http.post(`${OrderItemService.URL}/find-by-order-id`,params.toString(), { headers: headers }).map(res => res.json());
    }

    updateOrderItem(orderItemId: number, quantity: number):Observable<Map<string,any>>
    {
        const headers = new Headers();
        headers.append('Content-Type', 'application/x-www-form-urlencoded');

        const params = new URLSearchParams();
        params.append('orderItemId', <string><any>orderItemId);
        params.append('quantity', <string><any>quantity);
        return this.http.post(`${OrderItemService.URL}/update-by-id`,params.toString(), { headers: headers }).map(res => res.json());
    }



    deleteOrderItemByOrderItemId(orderItemId: number):Observable<Map<string,any>>
    {
        const headers = new Headers();
        headers.append('Content-Type', 'application/x-www-form-urlencoded');

        const params = new URLSearchParams();
        params.append('orderItemId', <string><any>orderItemId);
        return this.http.post(`${OrderItemService.URL}/delete-by-id`,params.toString(), { headers: headers }).map(res => res.json());
    }
}