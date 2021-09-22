import { Injectable } from '@angular/core'
import { Http, Headers, RequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import { UserModel } from '../model/user.model';

@Injectable()
export class UserService
{
    static readonly URL = 'http://localhost:50001/tos-rest/api/user';

    constructor( private http:Http) {}

    verifyUserByEmail(email:string):Observable<UserModel>
    {
      const headers = new Headers();
      headers.append('Content-Type', 'application/x-www-form-urlencoded');

      const params = new URLSearchParams();
      params.append('email', email);
      return this.http.post(`${UserService.URL}/check-exists`,params.toString(), { headers: headers }).map(res => res.json());
    }

    login(email:string, password:string):Observable<Map<string,any>>
    {
       const headers = new Headers();
       headers.append('Content-Type', 'application/x-www-form-urlencoded');

       const params = new URLSearchParams();
       params.append('email', email);
       params.append('password', password)
       return this.http.post(`${UserService.URL}/login-by-email`, params.toString(), { headers: headers }).map(res => res.json());
    }

    findUserAll():Observable<UserModel>
    {
      return this.http.post(`${UserService.URL}/find-all`,'').map(res => res.json());
    }

    updateUserPrivilege(userId:number, isEnabled:boolean, isAdmin:boolean):Observable<Map<string,any>>
    {
        const headers = new Headers();
        headers.append('Content-Type', 'application/x-www-form-urlencoded');

        const params = new URLSearchParams();
        params.append('userId', <string><any>userId);
        params.append('isEnabled', <string><any>isEnabled);
        params.append('isAdmin', <string><any>isAdmin);
        return this.http.post(`${UserService.URL}/update-privilege`, params.toString(), { headers: headers }).map(res => res.json());
    }

    register(email:string, password:string, isEnabled:boolean, isAdmin:boolean):Observable<Map<string,any>>
    {
        const headers = new Headers();
        headers.append('Content-Type', 'application/x-www-form-urlencoded');

        const params = new URLSearchParams();
        params.append('email', email);
        params.append('password', password);
        params.append('isEnabled', <string><any>isEnabled);
        params.append('isAdmin', <string><any>isAdmin);
        return this.http.post(`${UserService.URL}/add`, params.toString(), { headers: headers }).map(res => res.json());
    }
}