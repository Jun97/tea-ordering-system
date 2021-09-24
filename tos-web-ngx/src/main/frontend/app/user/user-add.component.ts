import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../service/user.service';

@Component(
{
  templateUrl: './user-add.component.htm',
})
export class UserAddComponent implements OnInit
{
  errorMessage:string = null;
  form =
  {
    data:
    {
      username: <string> null,
      password: <string> null,
      accountStatus: <boolean> true,
      role: <boolean> false,
    }
  };

  constructor(private route: ActivatedRoute, private router: Router, private usersService:UserService)
  {

  }

  onSubmit()
  {
    this.usersService.verifyUserByEmail(this.form.data.username).subscribe(res=>
    {
      if(res["errorMessage"])
      {
        this.errorMessage = res["errorMessage"];
      }
      else
      {
        this.usersService.register(this.form.data.username, this.form.data.password, this.form.data.accountStatus, this.form.data.role).subscribe(res=>
        {
          this.router.navigate(['/users'], {relativeTo: this.route});
        });
      }
    });
  }

  ngOnInit()
  {

  }
}
