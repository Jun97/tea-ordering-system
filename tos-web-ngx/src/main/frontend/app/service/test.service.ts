import { Injectable } from '@angular/core'
import { Http, Headers, RequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import { TestModel } from '../model/test.model';

@Injectable()
export class TestService
{
  static readonly URL = 'http://localhost:50001/tos-rest';
  
  constructor( private http:Http) {}
  
  findTestAll():Observable<TestModel>
  {
    return this.http.post(`${TestService.URL}/rest/test/find-test-all`,'').map(res => res.json());
  }
  
  getTestByTestId(testId:number):Observable<TestModel>
  {
    const headers = new Headers();
    headers.append('Content-Type', 'application/x-www-form-urlencoded');
    
    const params = new URLSearchParams();
    params.append('testId', <string><any>testId);
    return this.http.post(`${TestService.URL}/rest/test/get-test-by-test-id`,params.toString(), { headers: headers }).map(res => res.json());
  }
  
  updateTestByTestId(testId:number, hello:string, world:number):Observable<Map<string,any>>
  {
    const headers = new Headers();
    headers.append('Content-Type', 'application/x-www-form-urlencoded');
    
    const params = new URLSearchParams();
    params.append('testId', <string><any>testId);
    params.append('hello', hello);
    params.append('world', <string><any>world);
    return this.http.post(`${TestService.URL}/rest/test/update-test-by-test-id`,params.toString(), { headers: headers }).map(res => res.json());
  }
  
  deleteTestByTestId(testId:number):Observable<Map<string,any>>
  {
    const headers = new Headers();
    headers.append('Content-Type', 'application/x-www-form-urlencoded');
    
    const params = new URLSearchParams();
    params.append('testId', <string><any>testId);
    return this.http.post(`${TestService.URL}/rest/test/delete-test-by-test-id`,params.toString(), { headers: headers }).map(res => res.json());
  
  }
  
  addTest(hello:string, world:number):Observable<Map<string,any>>
  {
    const headers = new Headers();
    headers.append('Content-Type', 'application/x-www-form-urlencoded');
    
    const params = new URLSearchParams();
    params.append('hello', hello);
    params.append('world', <string><any>world);
    return this.http.post(`${TestService.URL}/rest/test/add-test`,params.toString(), { headers: headers }).map(res => res.json());
  }
  
}
