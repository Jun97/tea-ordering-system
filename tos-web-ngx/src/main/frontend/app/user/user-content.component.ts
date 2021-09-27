import { Component, OnInit} from '@angular/core';


import {UserService} from "../service/user.service";
import {UserModel} from "../model/user.model";
@Component(
{
  templateUrl: './user-content.component.htm'
})
export class UserContentComponent implements OnInit
{
  users:UserModel[];
  currentUser: UserModel;

  userSubmitMessage: string;

  constructor(private usersService:UserService)
  {}

  ngOnInit()
  {
    this.getUserAll();
  }

  private getUserAll()
  {
    this.usersService.findUserAll().subscribe(
        (res: UserModel[])=>{
        this.users = res;
        },
        (err:any) => {
          console.log(err);
        });
  }

  updateUserPrivilege(userId:number, accountStatus:boolean, isAdmin:boolean)
  {
    this.usersService.updateUserPrivilege(userId, accountStatus, isAdmin).subscribe(
        (res:any) => {
          if(!res.error) {
            let index = this.users.findIndex(x => x.userId === userId);
            this.users[index] = res.user;
          }

          this.userSubmitMessage = res.message;
        },
        (err:any) => {
          console.log(err);
        });
  }
}
