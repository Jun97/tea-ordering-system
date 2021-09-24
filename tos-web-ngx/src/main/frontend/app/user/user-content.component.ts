import { Component, OnInit} from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';


import { UsersModel } from '../model/users.model';
import {UserService} from "../service/user.service";
import {UserModel} from "../model/user.model";
@Component(
{
  templateUrl: './user-content.component.htm'
})
export class UserContentComponent implements OnInit
{
  users:UserModel[];

  constructor(private usersService:UserService)
  {

  }

  private fetch()
  {
    this.usersService.findUserAll().subscribe(data=>{
        this.users = <UserModel[]><any>data;
    });
  }

  modifyAccountStatus(userId:number, accountStatus:boolean, isAdmin:boolean)
  {
    if(accountStatus)
    {
        accountStatus = false;
    }
    else
    {
        accountStatus = true;
    }
    this.usersService.updateUserPrivilege(userId, accountStatus, isAdmin).subscribe(res=>{});
    location.reload();
  }

  modifyRole(userId:number, accountStatus:boolean, isAdmin:boolean)
  {
    if(isAdmin)
    {
        isAdmin = false;
    }
    else
    {
        isAdmin = true;
    }
    this.usersService.updateUserPrivilege(userId, accountStatus, isAdmin).subscribe(res=>{});
    location.reload();
  }

  ngOnInit()
  {
    this.fetch();
  }
}
