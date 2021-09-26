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
    currentMenuItem: MenuItemModel[] = [];
    formMenuItem: MenuItemModel;
    formModelMenuAuthPassword: string = "";

    modeTeaSession: 'edit' | 'add';
    modeMenuItem: 'edit' | 'add' = 'add';
    menuItemAccessGranted: boolean | undefined = undefined;

    teaSessionImageMessage: string;
    teaSessionSubmitMessage: string;
    menuAuthMessage: string;
    menuItemImageMessage: string;
    menuItemSubmitMessage: string;

    teaSessionImagePreview: string;
    menuItemImagePreview: string;


    constructor(private route: ActivatedRoute,
                private router: Router,
                private teaSessionService: TeaSessionService,
                private  menuItemService: MenuItemService,
                private datePipe:DatePipe)
    {}

    ngOnInit() {
        this.parseCurrentTeaSessionParam();
        this.fetchCurrentUser();
        this.initMenuForm();
    }

    private parseCurrentTeaSessionParam() {
        this.route.paramMap.subscribe(paramMap => {
          if (paramMap.has('teaSessionId')) {
              this.getTeaSessionById(Number(paramMap.get('teaSessionId')));
              this.modeTeaSession = "edit";
          } else {
              this.modeTeaSession = "add";
              this.initTeaSessionForm();
          }
        });
    }

    private getTeaSessionById(teaSessionId: number) {
        this.teaSessionService.getTeaSessionById(teaSessionId).subscribe(
            (res: TeaSessionModel) => {
                this.currentTeaSession = res;

                this.currentTeaSession.cutOffDate = this.datePipe.transform(res.cutOffDate, "yyyy-MM-dd");
                this.currentTeaSession.treatDate = this.datePipe.transform(res.treatDate, "yyyy-MM-dd");
                this.currentTeaSession.password = "";
                if(this.currentTeaSession.isPublic){
                    this.initMenuSection();
                }

                console.log(this.currentTeaSession);
            },
            (err: any) => {
                console.log("getTeaSessionByIdErr:",err);
            }
        )
    }

    private findMenuItemByTeaSessionId(teaSessionId: number, password: string) {
        this.menuItemService.findMenuItemByTeaSessionIdAndPassword(teaSessionId, password).subscribe(
            (res:any) => {
                if(!res.error){
                    this.currentMenuItem = res.menuItem;
                    this.menuItemAccessGranted = true;
                }
                this.menuAuthMessage = res.message;
                console.log("menuItemResponse:", this.currentMenuItem);
            },
            (err: any) => {
                console.log("findMenuItemByTeaSessionIdAndPassword:", err);
            })
    }

    private fetchCurrentUser() {
        this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
    }

    private addTeaSession() {

            this.teaSessionService.addTeaSession(
                this.currentTeaSession.name,
                this.currentUser.userId,
                this.currentTeaSession.password,
                this.datePipe.transform(this.currentTeaSession.treatDate, 'yyyy-MM-dd'),
                this.datePipe.transform(this.currentTeaSession.cutOffDate,'yyyy-MM-dd'),
                this.currentTeaSession.isPublic,
                this.currentTeaSession.description,
                this.currentTeaSession.imagePath instanceof File? this.currentTeaSession.imagePath: undefined
            ).subscribe(
                (res:any) => {
                    this.teaSessionSubmitMessage = res.message;

                    if(!res.error){
                        this.currentTeaSession = res.teaSession;
                        this.currentTeaSession.cutOffDate = this.datePipe.transform(res.teaSession.cutOffDate, "yyyy-MM-dd");
                        this.currentTeaSession.treatDate = this.datePipe.transform(res.teaSession.treatDate, "yyyy-MM-dd");

                        this.menuItemAccessGranted = true;
                        this.initMenuForm();
                    }

                },
                (err: any) => {
                    console.log("addTeaSessionErr:", err);
                }
            )

    }

    private updateTeaSession() {
            this.teaSessionService.updateTeaSession(
                this.currentTeaSession.teaSessionId,
                this.currentUser.userId,
                this.currentTeaSession.name,
                this.datePipe.transform(this.currentTeaSession.treatDate, 'yyyy-MM-dd'),
                this.datePipe.transform(this.currentTeaSession.cutOffDate,'yyyy-MM-dd'),
                this.currentTeaSession.isPublic,
                this.currentTeaSession.password,
                this.currentTeaSession.description,
                this.currentTeaSession.imagePath instanceof File? this.currentTeaSession.imagePath: undefined
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

    private addMenu() {
            this.menuItemService.addMenuItem(
                this.currentTeaSession.teaSessionId,
                this.formMenuItem.name,
                this.formMenuItem.imagePath instanceof File?this.formMenuItem.imagePath: undefined).subscribe(
                (res: any) => {
                    this.menuItemSubmitMessage = res.message;
                    if(!res.error){
                       this.currentMenuItem = [...this.currentMenuItem, res.menuItem];
                       this.initMenuForm();
                    }
                },
                (err: any)=> {
                    console.log("updateMenuItemErr:", err)
                }
            )
    }

    private updateMenu() {
            this.menuItemService.updateMenuItem(
                this.formMenuItem.menuItemId,
                this.formMenuItem.name,
                this.formMenuItem.imagePath instanceof File? this.formMenuItem.imagePath: undefined).subscribe(
                (res: any) => {
                    this.menuItemSubmitMessage = res.message;
                    if(!res.error){
                        let index = this.currentMenuItem.findIndex(x => x.menuItemId === this.formMenuItem.menuItemId);
                        this.currentMenuItem[index] = res.menuItem;
                        this.initMenuForm();
                        this.modeMenuItem = "add"; //Assign back to default mode
                    }
                },
                (err: any)=> {
                    console.log("updateMenuItemErr:", err)
                }
            )
    }

    private deleteMenu(menuItem: MenuItemModel) {
        this.menuItemService.deleteMenuItemById(menuItem.menuItemId).subscribe(
            (res: any) => {
                this.menuItemSubmitMessage = res.message;
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

    private onMenuFileSelected(event:any) {
        if(event.target.files[0] instanceof File){
            if(event.target.files[0].type.match(/image\/*/) == null) {
                this.menuItemImageMessage = "Only images are supported";
            }
            else {
                this.formMenuItem.imagePath = event.target.files[0];

                const reader = new FileReader();
                reader.onload = (_event) => {
                    this.menuItemImagePreview = reader.result as string;
                }
                reader.readAsDataURL(event.target.files[0]);
                console.log("File assigned");
            }
        }
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
        this.findMenuItemByTeaSessionId(this.currentTeaSession.teaSessionId, this.formModelMenuAuthPassword);
    }

    private onMenuSubmit() {
        if(this.modeMenuItem == "edit"){
            this.updateMenu();
        }
        if(this.modeMenuItem == "add"){
            this.addMenu();
        }
    }

    private assignMenuToForm(menuItem: MenuItemModel) {
        Object.assign(this.formMenuItem, menuItem);
        this.modeMenuItem = "edit";

    }

    private initMenuSection() {
        if (this.currentTeaSession.isPublic) {
            this.menuItemAccessGranted = true;
            this.findMenuItemByTeaSessionId(this.currentTeaSession.teaSessionId, "");
        } else{
            this.menuItemAccessGranted = false;
        }
    }

    private initMenuForm() {
        this.formMenuItem = new MenuItemModel(
            null, "", null, null, null
        );
    }

    private clearMenuForm() {
        this.initMenuForm();
        this.modeMenuItem = "add";
    }

    private initTeaSessionForm() {
        this.currentTeaSession = new TeaSessionModel(
            null,
            null,
            null, null,
            null,
            "",
            "",
            null ,
            null, null, null)
    }
}
