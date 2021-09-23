import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';


import { TeaSessionModel } from '../model/tea-session.model';
import { UserModel } from '../model/user.model';
import { TeaSessionService } from '../service/tea-session.service';
import {UserService} from "../service/user.service";
import {OrderService} from "../service/order.service";
@Component(
{
  templateUrl: './tea-session-content.component.htm'
})
export class TeaSessionContentComponent implements OnInit
{
  currentUser:UserModel = null;
  teaSession:TeaSessionModel[] = [];
  isAdmin:boolean = null;

  constructor(private route: ActivatedRoute, private router: Router, private userService:UserService, private teaSessionService:TeaSessionService, private orderService:OrderService)
  {}

  ngOnInit() {
    this.findUpcomingTeaSession();

    this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
    if(this.currentUser.isAdmin) {
      this.isAdmin = true;
    } else {
      this.isAdmin = false;
    }
  }

  private findUpcomingTeaSession() {
    this.teaSessionService.findTeaSessionAll().subscribe( (res:TeaSessionModel[]) =>{
      this.teaSession = res;
    });
  }

  deleteTeaSession(teaSessionId:number) {
    this.teaSessionService.deleteTeaSessionById(teaSessionId).subscribe(
        (res: any)=> {
          if(!res.error) {
            this.router.navigate(['./complete', 'delete', teaSessionId], {relativeTo: this.route});
          } else {
            console.log(res);
          }
        },
        (err: any)=> {
          console.log(err);
        });
  }

  // updateVisibility(teaSessionId:number, isPublic:boolean, password: string)
  // {
  //   if(password == null){
  //     password = "";
  //   }
  //
  //   this.teaSessionService.updateTeaSessionByVisibility(teaSessionId, isPublic, password).subscribe((res: any)=>
  //   {
  //       this.router.navigate(['./complete', 'edit', teaSessionId], {relativeTo: this.route});
  //   });
  // }

  logout()
  {
    localStorage.removeItem('currentUser');
    this.router.navigate(['/login'], {relativeTo: this.route});
  }
}
