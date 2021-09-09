import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/of';
import { TestService } from '../service/test.service';
import { TestModel } from '../model/test.model';


@Injectable()
export class ResolveTestByTestId implements Resolve<TestModel> 
{
  constructor(private testService: TestService) {}
  resolve(route: ActivatedRouteSnapshot) 
  {
    const testId = route.params['testId']; 
    if(testId==null)
    {
      return Observable.of(<TestModel>null)
    }
    return this.testService.getTestByTestId(testId);
  }
}


