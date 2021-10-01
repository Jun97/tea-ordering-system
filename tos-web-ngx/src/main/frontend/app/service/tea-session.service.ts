import { Injectable } from '@angular/core'
import {Http, Headers, RequestOptions, Response} from '@angular/http';
import { HttpClient, HttpRequest, HttpEvent } from '@angular/common/http';
// import { Observable } from 'rxjs/Observable';
import {Observable, Observer} from 'rxjs';
// import 'rxjs/add/operator/map';
import { TeaSessionModel } from '../model/tea-session.model';
import {map, switchMap} from "rxjs/operators";
import {of} from "rxjs/observable/of";

@Injectable()
export class TeaSessionService
{
  static readonly URL = 'http://localhost:50001/tos-rest/api/tea-session';

  constructor( private http:Http, private httpClient:HttpClient) {}

  addTeaSession(name: string, userId: number, password: string, treatDate: string, cutOffDate: string, isPublic: boolean, description?: string, teaSessionImagePath?: File):Observable<Map<string,any>>
  {
    const headers = new Headers();
    // headers.append('Content-Type', 'multipart/form-data');
    // headers.append('enctype', 'multipart/form-data');

    const formData = new FormData();
    formData.append('name', name);
    formData.append('userId', userId);
    formData.append('password', password);
    formData.append('treatDate', <string><any>treatDate);
    formData.append('cutOffDate', <string><any>cutOffDate);
    formData.append('isPublic', <string><any>isPublic);
    if(teaSessionImagePath != undefined || teaSessionImagePath != null) {
      formData.append('teaSessionImagePath', teaSessionImagePath);
    }
    if(description != undefined || description != null){
      formData.append('description', description);
    }
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

  updateTeaSession(teaSessionId: number, userId: number, name: string,  treatDate: string, cutOffDate: string, isPublic: Boolean, password?: String | null, description?: string, teaSessionImagePath?: File):Observable<Map<string,any>>
  {
    const headers = new Headers();
    // headers.append('Content-Type', 'multipart/form-data');
    // headers.append('enctype', 'multipart/form-data');

    const formData = new FormData();
    formData.append('teaSessionId', <string><any>teaSessionId);
    formData.append('userId', <string><any>userId);
    formData.append('name', name);
    formData.append('treatDate', treatDate);
    formData.append('cutOffDate', cutOffDate);
    formData.append('isPublic', isPublic);
    if(password != undefined) {
      formData.append('password', password);
    }
    if(description != undefined){
      formData.append('description', description);
    }
    if(teaSessionImagePath != undefined){
      formData.append('teaSessionImagePath', teaSessionImagePath);
    }
    return this.http.post(`${TeaSessionService.URL}/update`,formData, { headers: headers }).map(res => res.json());
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

  deleteTeaSessionById(teaSessionId:number, userId: number):Observable<Map<string,any>>
  {
    const headers = new Headers();
    headers.append('Content-Type', 'application/x-www-form-urlencoded');

    const params = new URLSearchParams();
    params.append('teaSessionId', <string><any>teaSessionId);
    params.append('userId', <string><any>userId);
    return this.http.post(`${TeaSessionService.URL}/delete-by-id`,params.toString(), { headers: headers }).map(res => res.json());
  }

  getFileFromUrl(url: string):Observable<File> {
    let filename = url.split('/').pop();
    return this.http
        .get(url, {responseType:2}) //Response as ArrayBuffer for 2
        .pipe(
            map((response: Response) => new File([response.arrayBuffer()], filename))
        );
  }

  getShareLink(teaSessionId: number): Observable<Map<string, any>> {
    const headers = new Headers();
    headers.append('Content-Type', 'application/x-www-form-urlencoded');

    const params = new URLSearchParams();
    params.append('teaSessionId', <string><any>teaSessionId);
    return this.http.post(`${TeaSessionService.URL}/get-share-link`,params.toString(), { headers: headers }).map(res => res.json());
  }

    getFileFromUrl2(url: string):Observable<File> {
      let filename = url.split('/').pop();

      const headers = new Headers();
      headers.append('observe', 'response');
      return this.http
          .get(url, {headers: headers, responseType:2})
          .pipe(
              switchMap((response: Response) => this.getFileFromArrayBuffer(response.arrayBuffer(), filename))
          );
    }

    private getFileFromArrayBuffer(arrayBuffer: ArrayBuffer, filename: string) {
    return Observable.create((observer:Observer<File>)=> {

      let fileReader =  new FileReader();

      fileReader.onerror = err => observer.error(err);
      fileReader.onabort = err => observer.error(err);
      fileReader.onload = () => {
        let file: File = new File([fileReader.result as ArrayBuffer], filename);
        observer.next(file);
      }
      fileReader.onloadend = () => observer.complete();

      return fileReader.readAsArrayBuffer(new Blob([arrayBuffer]));
    })

  }

//   map((response: Response) => {
//
//   const arrayBufferCallback = new Promise<ArrayBuffer>( ((resolve, reject) => {
//       let arrayBuffer: ArrayBuffer;
//       let fileReader =  new FileReader();
//       fileReader.readAsArrayBuffer(new Blob([response]));
//       fileReader.onload = (event) => {
//         arrayBuffer = fileReader.result as ArrayBuffer;
//         resolve (arrayBuffer);
//       }
//     })
// );
//
//
//   const fileCallback:Observable<File> = Observable.fromPromise(arrayBufferCallback)
//     .mergeMap( (arrayBuffer: ArrayBuffer, index: number) => {
//       let file: File = new File([arrayBuffer], filename);
//       return file;
//     }).pipe(
//
//     );
//
// })



}