import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../service/user.service';
import {UserModel} from "../model/user.model";
import {Observable} from "rxjs";

@Component(
{
  templateUrl: './user-add.component.htm',
})
export class UserAddComponent implements OnInit
{
    @ViewChild('formEmail') formEmail: ElementRef;

    userSubmitMessage: string;
    userEmailExistsMessage: string;
    isUserEmailUnique: boolean = undefined;

    userForm: UserModel;

    constructor(private route: ActivatedRoute, private router: Router, private usersService:UserService)
    {}

    ngOnInit() {
        this.initUserForm();
    }

    ngAfterViewInit() {
        Observable.fromEvent(this.formEmail.nativeElement, 'input') //Register for email debounce check
            .map((event: Event) => (<HTMLInputElement>event.target).value)
            .debounceTime(1000)
            .distinctUntilChanged()
            .subscribe(data => this.checkEmailExists());
    }

    initUserForm() {
        this.userForm = new UserModel(null,
            "",
            "",
            true,
            null,
            false,
            null, null);
    }

    onFormSubmit() {
        this.usersService.register(this.userForm.email, this.userForm.password, true, false).subscribe(
            (res: any)=> {
              this.userSubmitMessage = res.message;
            },
            (err: any) => {
                console.log(err);
            });
    }

    checkEmailExists() {
        this.usersService.verifyUserByEmail(this.userForm.email).subscribe(
            (res: any) => {
                if(!res.error){
                    this.isUserEmailUnique = true;
                } else {
                    this.isUserEmailUnique = false;
                }
                this.userEmailExistsMessage = res.message;
            },
            (err: any) => {
                console.log(err);
            });
    }
}
