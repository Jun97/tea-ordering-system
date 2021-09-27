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
  currentUser:UserModel;
  teaSession:TeaSessionModel[];

  constructor(private route: ActivatedRoute, private router: Router, private userService:UserService, private teaSessionService:TeaSessionService, private orderService:OrderService)
  {}

  ngOnInit() {
    this.findUpcomingTeaSession();

    this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
    console.log("user:", this.currentUser);
    console.log("isAdmin", this.currentUser.isAdmin);
  }

  private findUpcomingTeaSession() {
    this.teaSessionService.findTeaSessionAll().subscribe( (res:TeaSessionModel[]) =>{
      this.teaSession = res;
      console.log("teaSession:", this.teaSession);
    });
  }

  deleteTeaSession(teaSessionId:number) {
    this.teaSessionService.deleteTeaSessionById(teaSessionId, this.currentUser.userId).subscribe(
        (res: any)=> {
          if(!res.error) {
            let index = this.teaSession.findIndex(x => x.teaSessionId === teaSessionId);
            this.teaSession.splice(index, 1);
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
