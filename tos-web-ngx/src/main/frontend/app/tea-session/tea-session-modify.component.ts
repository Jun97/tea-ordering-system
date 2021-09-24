import { Component, OnInit} from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TeaSessionService } from '../service/tea-session.service';
import { FileUploader } from 'ng2-file-upload';
import { DatePipe } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { TeaSessionModel } from '../model/tea-session.model'
import {MenuItemModel} from "../model/menu-item.model";
import {UserModel} from "../model/user.model";
import {Observable, Subscription} from "rxjs";
import {MenuItemService} from "../service/menu-item.service";
@Component(
{
  templateUrl: './tea-session-modify.component.htm'
})
export class TeaSessionModifyComponent implements OnInit
{

    currentTeaSession: TeaSessionModel;
    currentUser: UserModel;
    currentMenuItem: MenuItemModel[];
    formMenuItem: MenuItemModel;
    formModelMenuAuthPassword: string | null;

    modeTeaSession: 'edit' | 'add';
    modeMenuItem: 'edit' | 'add' = 'add';
    menuItemAccessGranted: boolean;

    teaSessionImageMessage: string;
    teaSessionSubmitMessage: string;
    menuAuthMessage: string;
    menuItemImageMessage: string;
    menuItemSubmitMessage: string;

    teaSessionImagePreview: string;


    constructor(private route: ActivatedRoute,
                private router: Router,
                private teaSessionService: TeaSessionService,
                private  menuItemService: MenuItemService,
                private datePipe:DatePipe)
    {}

    ngOnInit() {
        this.getCurrentTeaSessionParam();
        this.fetchCurrentUser();
    }

    private getCurrentTeaSessionParam() {
        this.route.paramMap.subscribe(paramMap => {
          if (paramMap.has('teaSessionId')) {
              this.getTeaSessionById(Number(paramMap.get('teaSessionId')));
              this.modeTeaSession = "edit";
          } else {
              this.modeTeaSession = "add";
          }
        });
    }

    private getTeaSessionById(teaSessionId: number) {
        this.teaSessionService.getTeaSessionById(teaSessionId).subscribe(
            (res: TeaSessionModel) => {
                this.currentTeaSession = res;
                this.currentTeaSession.cutOffDate = this.datePipe.transform(res.cutOffDate, "yyyy-MM-dd");
                this.currentTeaSession.treatDate = this.datePipe.transform(res.treatDate, "yyyy-MM-dd");
                if(this.currentTeaSession.isPublic){
                    this.getMenuItemByTeaSessionId(this.currentTeaSession.teaSessionId, "");
                }
                this.currentTeaSession.password = "";
                console.log(this.currentTeaSession);
            },
            (err: any) => {
                console.log("getTeaSessionByIdErr:",err);
            }
        )
    }

    private getMenuItemByTeaSessionId(teaSessionId: number, password: string) {
        this.menuItemService.findMenuItemByTeaSessionIdAndPassword(teaSessionId, password).subscribe(
            (res:any) => {
                if(!res.error){
                    this.currentMenuItem = res.menuItem;
                }
                this.menuAuthMessage = res.message;

            },
            (err: any) => {
                console.log("findMenuItemByTeaSessionIdAndPassword:", err);
            })
    }

    private fetchCurrentUser() {
        this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
    }

    private addTeaSession() {
        if(this.currentTeaSession.imagePath instanceof File) {
            this.teaSessionService.addTeaSession(
                this.currentTeaSession.name,
                this.currentTeaSession.description,
                this.currentUser.userId,
                this.currentTeaSession.password,
                this.datePipe.transform(this.currentTeaSession.treatDate, 'yyyy-MM-dd'),
                this.datePipe.transform(this.currentTeaSession.cutOffDate,'yyyy-MM-dd'),
                this.currentTeaSession.isPublic,
                this.currentTeaSession.imagePath
            ).subscribe(
                (res:any) => {
                    this.teaSessionSubmitMessage = res.message;
                    if(!res.error){
                        this.currentTeaSession = res.teaSession
                    }

                },
                (err: any) => {
                    console.log("addTeaSessionErr:", err);
                }
            )
        }
    }

    private updateTeaSession() {
        if(this.currentTeaSession.imagePath instanceof File){
            this.teaSessionService.updateTeaSession(
                this.currentTeaSession.teaSessionId,
                this.currentUser.userId,
                this.currentTeaSession.name,
                this.currentTeaSession.description,
                this.datePipe.transform(this.currentTeaSession.treatDate, 'yyyy-MM-dd'),
                this.datePipe.transform(this.currentTeaSession.cutOffDate,'yyyy-MM-dd'),
                this.currentTeaSession.imagePath,
                this.currentTeaSession.isPublic,
                this.currentTeaSession.password
            ).subscribe(
                (res:any) => {
                    this.teaSessionSubmitMessage = res.message;
                    if(!res.error){
                        this.currentTeaSession = res.teaSession;
                        this.currentTeaSession.cutOffDate = this.datePipe.transform(res.teaSession.cutOffDate, "yyyy-MM-dd");
                        this.currentTeaSession.treatDate = this.datePipe.transform(res.teaSession.treatDate, "yyyy-MM-dd");
                    }
                    console.log("updateTea", this.currentTeaSession);

                },
                (err: any) => {
                    console.log("updateTeaSessionErr:", err);
                }
            )
        }

    }

    private addMenuItem() {
        if(this.formMenuItem.imagePath instanceof File) {
            this.menuItemService.addMenuItem(this.currentTeaSession.teaSessionId, this.formMenuItem.name, this.formMenuItem.imagePath).subscribe(
                (res: any) => {
                    this.menuItemSubmitMessage = res.message;
                    if(!res.error){
                       this.currentMenuItem = [...this.currentMenuItem, res.menuItem];
                    }
                },
                (err: any)=> {
                    console.log("updateMenuItemErr:", err)
                }
            )
        }
    }

    private updateMenuItem() {
        if(this.formMenuItem.imagePath instanceof File) {
            this.menuItemService.updateMenuItem(this.formMenuItem.menuItemId, this.formMenuItem.name, this.formMenuItem.imagePath).subscribe(
                (res: any) => {
                    this.menuItemSubmitMessage = res.message;
                    if(!res.error){
                        let index = this.currentMenuItem.findIndex(x => x.menuItemId === this.formMenuItem.menuItemId);
                        this.currentMenuItem[index] = res.menuItem;
                        this.modeMenuItem = "add"; //Assign back to default mode
                    }
                },
                (err: any)=> {
                    console.log("updateMenuItemErr:", err)
                }
            )
        }
    }

    private deleteMenuItem(menuItem: MenuItemModel) {
        this.menuItemService.deleteMenuItemById(menuItem.menuItemId).subscribe(
            (res: any) => {
                if(!res.error){
                    let index = this.currentMenuItem.findIndex(x => x.menuItemId === menuItem.menuItemId);
                    delete this.currentMenuItem[index];
                }
            },
            (err: any) => {
                console.log("deleteMenuItemByIdErr:", err)
            }
        )
    }

    private onTeaSessionFileSelected(event:any) {
        if(event.target.files[0] instanceof File){
            if(event.target.files[0].type.match(/image\/*/) == null) {
                this.teaSessionImageMessage = "Only images are supported";
            }
            else {
                this.currentTeaSession.imagePath = event.target.files[0];

                const reader = new FileReader();
                reader.onload = (_event) => {
                    this.teaSessionImagePreview = reader.result as string;
                }
                reader.readAsDataURL(event.target.files[0]);
                console.log("File assigned");
            }
        }
    }

    private onMenuItemFileSelected(event:any) {
        if(event.target.files[0] instanceof File){
            if(event.target.files[0].match(/image\/*/) == null) {
                this.menuItemImageMessage = "Only images are supported";
            }
            else {
                this.formMenuItem.imagePath = event.target.files[0];

                const reader = new FileReader();
                reader.onload = (_event) => {
                    this.teaSessionImagePreview = reader.result as string;
                }
                reader.readAsDataURL(event.target.files[0]);
                console.log("File assigned");
            }
        }
    }

    private assignMenuItemInForm(menuItem: MenuItemModel) {
        this.formMenuItem = menuItem;
        this.modeMenuItem = "edit";
    }

    private onTeaSessionSubmit() {
        if(this.modeTeaSession == "edit"){
            this.updateTeaSession();
        }
        if(this.modeTeaSession == "add"){
            this.addTeaSession();
        }
    }

    private onMenuPasswordSubmit() {
        this.getMenuItemByTeaSessionId(this.currentTeaSession.teaSessionId, this.formModelMenuAuthPassword);
    }

    private onMenuSubmit() {
        if(this.modeMenuItem == "edit"){
            this.updateMenuItem();
        }
        if(this.modeMenuItem == "add"){
            this.addMenuItem();
        }
    }

    private logDate(){
        console.log(this.currentTeaSession.treatDate);
    }
}
