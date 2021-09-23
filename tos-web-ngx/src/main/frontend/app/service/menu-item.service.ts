import { Injectable } from '@angular/core'
import { Http, Headers, RequestOptions } from '@angular/http';
import { HttpClient, HttpRequest, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import { TeaSessionModel } from '../model/tea-session.model';

@Injectable()
export class MenuItemService
{
  static readonly URL = 'http://localhost:50001/tos-rest/api/tea-session/menu-item';

  constructor( private http:Http, private httpClient:HttpClient) {}

  addMenuItem(teaSessionId: number, name: string, imagePath: File):Observable<Map<string,any>>
  {
    const headers = new Headers();
    headers.append('Content-Type', 'multipart/form-data');

    const formData = new FormData();
    formData.append('teaSessionId', <string><any>teaSessionId);
    formData.append('name', name);
    formData.append('imagePath', imagePath);
    return this.http.post(`${MenuItemService.URL}/add`,formData, { headers: headers }).map(res => res.json());
  }

  uploadMenuItemImage(teaSessionId: number, imagePath: File):Observable<Map<string,any>>
  {
    const headers = new Headers();
    headers.append('Content-Type', 'multipart/form-data');

    const formData:FormData = new FormData();
    formData.append('teaSessionId', <string><any>teaSessionId);
    formData.append('imagePath', imagePath);
    return this.http.post(`${MenuItemService.URL}/add-image-by-multipart`,formData, { headers: headers }).map(res => res.json());
  }

  findMenuItemByTeaSessionIdAndPassword(teaSessionId: number, password: string):Observable<Map<string,any>>
  {
    const headers = new Headers();
    headers.append('Content-Type', 'application/x-www-form-urlencoded');

    const params = new URLSearchParams();
    params.append('teaSessionId', <string><any>teaSessionId);
    params.append('password', password)
    return this.http.post(`${MenuItemService.URL}/find-by-tea-session-id`,params.toString(), { headers: headers }).map(res => res.json());
  }

  updateMenuItem(menuItemId: number, name: string, imagePath: File):Observable<Map<string,any>>
  {
    const headers = new Headers();
    headers.append('Content-Type', 'multipart/form-data');

    const formData = new FormData();
    formData.append('menuItemId', <string><any>menuItemId);
    formData.append('name', name);
    formData.append('imagePath', imagePath);
    return this.http.post(`${MenuItemService.URL}/update`,formData, { headers: headers }).map(res => res.json());
  }

  deleteMenuItemById(menuItemId:number):Observable<Map<string,any>>
  {
    const headers = new Headers();
    headers.append('Content-Type', 'application/x-www-form-urlencoded');

    const params = new URLSearchParams();
    params.append('teaSessionId', <string><any>menuItemId);
    return this.http.post(`${MenuItemService.URL}/delete-by-id`,params.toString(), { headers: headers }).map(res => res.json());
  }
}