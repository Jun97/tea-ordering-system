import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import {UserService} from "../service/user.service";

@Component(
{
  templateUrl: './login-content.component.htm'
})
export class LoginContentComponent implements OnInit
{
  form =
  {
    data:
    {
        email: <string> null,
        password: <string> null,
    }
  };
  errorMessage: string;

      constructor(private route: ActivatedRoute, private router: Router, private usersService:UserService) {

      }

    ngOnInit() {

    }

  onSubmit()
  {
    this.usersService.login(this.form.data.email, this.form.data.password).subscribe(
    (res: any) => {
            if(res.error)
            {
                this.errorMessage = res.message;
            } else {
                console.log(res)
                localStorage.setItem('currentUser', JSON.stringify(res.user));
                this.router.navigate(['/tea-session'], {relativeTo: this.route});

            }
        },
        (err: any) => {
            this.errorMessage = err.toString();
            console.log(err);
        });
  }


}
