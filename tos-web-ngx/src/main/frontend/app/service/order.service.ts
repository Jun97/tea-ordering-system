import { Injectable } from '@angular/core'
import { Http, Headers, RequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import { OrderModel } from '../model/order.model';

@Injectable()
export class OrderService
{
    static readonly URL = 'http://localhost:50001/tos-rest/api/tea-session/order';

    constructor( private http:Http) {}

    addOrder(teaSessionId: number, userId: number):Observable<Map<string,any>>
    {
      const headers = new Headers();
      headers.append('Content-Type', 'application/x-www-form-urlencoded');

      const params = new URLSearchParams();
      params.append('teaSessionId', <string><any>teaSessionId);
      params.append('userId', <string><any>userId);
      return this.http.post(`${OrderService.URL}/add`,params.toString(), { headers: headers }).map(res => res.json());
    }

    findOrderByTeaSessionId(teaSessionId: number):Observable<Map<string,any>>
    {
        const headers = new Headers();
        headers.append('Content-Type', 'application/x-www-form-urlencoded');

        const params = new URLSearchParams();
        params.append('teaSessionId', <string><any>teaSessionId);
        return this.http.post(`${OrderService.URL}/find-by-tea-session-id`,params.toString(), { headers: headers }).map(res => res.json());
    }

    getOrderById(orderId: number):Observable<Map<string,any>>
    {
        const headers = new Headers();
        headers.append('Content-Type', 'application/x-www-form-urlencoded');

        const params = new URLSearchParams();
        params.append('orderId', <string><any>orderId);
        return this.http.post(`${OrderService.URL}/get-by-id`,params.toString(), { headers: headers }).map(res => res.json());
    }

    deleteOrderById(orderId: number):Observable<Map<string,any>>
    {
        const headers = new Headers();
        headers.append('Content-Type', 'application/x-www-form-urlencoded');

        const params = new URLSearchParams();
        params.append('orderId', <string><any>orderId);
        return this.http.post(`${OrderService.URL}/delete-by-id`,params.toString(), { headers: headers }).map(res => res.json());
    }

    getOrderSummaryByTeaSessionId(teaSessionId: number):Observable<Map<string,any>>
    {
        const headers = new Headers();
        headers.append('Content-Type', 'application/x-www-form-urlencoded');

        const params = new URLSearchParams();
        params.append('teaSessionId', <string><any>teaSessionId);
        return this.http.post(`${OrderService.URL}/get-order-summary-by-tea-session-id`,params.toString(), { headers: headers }).map(res => res.json());
    }
}