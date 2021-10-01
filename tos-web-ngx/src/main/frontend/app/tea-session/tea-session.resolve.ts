import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/of';
import { TeaSessionService } from '../service/tea-session.service';
import { TeaSessionModel } from '../model/tea-session.model';


@Injectable()
export class ResolveTeaSessionByTeaSessionId implements Resolve<TeaSessionModel>
{
  constructor(private teaSessionService: TeaSessionService) {}
  resolve(route: ActivatedRouteSnapshot)
  {
    const teaSessionId = route.params['teaSessionId'];
    if(teaSessionId==null)
    {
      return Observable.of(<TeaSessionModel>null)
    }
    return this.teaSessionService.getTeaSessionById(teaSessionId);
  }
}

