import { Injectable } from '@angular/core'
import { Http, Headers, RequestOptions } from '@angular/http';
import { HttpClient, HttpRequest, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import { TeaSessionModel } from '../model/tea-session.model';

@Injectable()
export class TeaSessionService
{
  static readonly URL = 'http://localhost:50001/tos-rest/api/tea-session';

  constructor( private http:Http, private httpClient:HttpClient) {}

  addTeaSession(name: string, description: string, userId: number, password: string, treatDate: string, cutOffDate: string, isPublic: boolean, teaSessionImagePath: File):Observable<Map<string,any>>
  {
    const headers = new Headers();
    // headers.append('Content-Type', 'multipart/form-data');
    // headers.append('enctype', 'multipart/form-data');

    const formData = new FormData();
    formData.append('name', name);
    formData.append('description', description);
    formData.append('userId', userId);
    formData.append('password', password);
    formData.append('treatDate', <string><any>treatDate);
    formData.append('cutOffDate', <string><any>cutOffDate);
    formData.append('isPublic', <string><any>isPublic);
    formData.append('teaSessionImagePath', teaSessionImagePath);
    return this.http.post(`${TeaSessionService.URL}/add`,formData, { headers: headers }).map(res => res.json());
  }

  uploadTeaSessionImage(teaSessionId: number, teaSessionImagePath: File):Observable<Map<string,any>>
  {
    const headers = new Headers();
    // headers.append('Content-Type', 'multipart/form-data');
    // headers.append('enctype', 'multipart/form-data');

    const formData:FormData = new FormData();
    formData.append('teaSessionId', <string><any>teaSessionId);
    formData.append('teaSessionImagePath', teaSessionImagePath);
    return this.http.post(`${TeaSessionService.URL}/image/add`,formData, { headers: headers }).map(res => res.json());
  }

  getTeaSessionById(teaSessionId: number):Observable<TeaSessionModel>
  {
    const headers = new Headers();
    headers.append('Content-Type', 'application/x-www-form-urlencoded');

    const params = new URLSearchParams();
    params.append('teaSessionId', <string><any>teaSessionId);
    return this.http.post(`${TeaSessionService.URL}/get-by-id`,params.toString(), { headers: headers }).map(res => res.json());
  }

  findTeaSessionAll():Observable<TeaSessionModel[]>
  {
    return this.http.get(`${TeaSessionService.URL}/find-upcoming`,'').map(res => res.json());
  }

  updateTeaSession(teaSessionId: number, userId: number, name: string, description: string, treatDate: string, cutOffDate: string, teaSessionImagePath: File, isPublic: Boolean, password: String | null):Observable<Map<string,any>>
  {
    const headers = new Headers();
    // headers.append('Content-Type', 'multipart/form-data');
    // headers.append('enctype', 'multipart/form-data');

    const formData = new FormData();
    formData.append('teaSessionId', <string><any>teaSessionId);
    formData.append('userId', <string><any>userId);
    formData.append('name', name);
    formData.append('description', description);
    formData.append('treatDate', treatDate);
    formData.append('cutOffDate', cutOffDate);
    formData.append('teaSessionImagePath', teaSessionImagePath);
    formData.append('isPublic', isPublic);
    formData.append('password', password);
    return this.http.post(`${TeaSessionService.URL}/update-detail`,formData, { headers: headers }).map(res => res.json());
  }

  // updateTeaSessionByVisibility(teaSessionId:number, isPublic:boolean, password: string):Observable<Map<string,any>>
  // {
  //   const headers = new Headers();
  //   headers.append('Content-Type', 'application/x-www-form-urlencoded');
  //
  //   const params = new URLSearchParams();
  //   params.append('teaSessionId', <string><any>teaSessionId);
  //   params.append('isPublic', <string><any>isPublic);
  //   params.append('password', password);
  //   return this.http.post(`${TeaSessionService.URL}/update-privacy`,params.toString(), { headers: headers }).map(res => res.json());
  // }

  deleteTeaSessionById(teaSessionId:number):Observable<Map<string,any>>
  {
    const headers = new Headers();
    headers.append('Content-Type', 'application/x-www-form-urlencoded');

    const params = new URLSearchParams();
    params.append('teaSessionId', <string><any>teaSessionId);
    return this.http.post(`${TeaSessionService.URL}/delete`,params.toString(), { headers: headers }).map(res => res.json());
  }
}