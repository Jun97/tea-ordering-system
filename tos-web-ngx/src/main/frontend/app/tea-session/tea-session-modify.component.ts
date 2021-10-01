import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TeaSessionService } from '../service/tea-session.service';
import { DatePipe } from '@angular/common';

import { TeaSessionModel } from '../model/tea-session.model'
import { MenuItemModel } from "../model/menu-item.model";
import { UserModel } from "../model/user.model";
import { MenuItemService } from "../service/menu-item.service";
@Component(
{
  templateUrl: './tea-session-modify.component.htm'
})
export class TeaSessionModifyComponent implements OnInit, AfterViewInit
{
    elementTeaSessionFileInput: ElementRef;
    @ViewChild('formTeaSessionImage') set formTeaSessionImageContent(formTeaSessionImageContent: ElementRef) {
        if(formTeaSessionImageContent) { // initially setter gets called with undefined
            this.elementTeaSessionFileInput = formTeaSessionImageContent;
        }
    }
    elementMenuFileInput: ElementRef;
    @ViewChild('formMenuImage') set formMenuImageContent(formMenuImageContent: ElementRef) {
        if(formMenuImageContent) { // initially setter gets called with undefined
            this.elementMenuFileInput = formMenuImageContent;
        }
    }
    currentTeaSession: TeaSessionModel;
    currentUser: UserModel;
    currentMenuItem: MenuItemModel[] = [];
    formMenuItem: MenuItemModel;
    formModelMenuAuthPassword: string = "";

    modeTeaSession: 'edit' | 'add';
    modeMenuItem: 'edit' | 'add' = 'add';
    menuItemAccessGranted: boolean | undefined = undefined;
    cutOffAndTreatDateValid: boolean = true;

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
        this.clearMenuForm();
    }

    ngAfterViewInit() {

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

    private fetchCurrentUser() {
        this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
    }

    private getTeaSessionById(teaSessionId: number) {
        this.teaSessionService.getTeaSessionById(teaSessionId).subscribe(
            (res: TeaSessionModel) => {
                this.currentTeaSession = res;

                this.currentTeaSession.cutOffDate = this.datePipe.transform(res.cutOffDate, "yyyy-MM-dd");
                this.currentTeaSession.treatDate = this.datePipe.transform(res.treatDate, "yyyy-MM-dd");
                this.currentTeaSession.password = "";

                this.initMenuSection();

                if(res.imagePath && typeof(res.imagePath) == "string") {
                    this.getImageFileToTeaSessionFormAndPreview(res.imagePath);
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

    private addTeaSession() {
            this.teaSessionService.addTeaSession(
                this.currentTeaSession.name,
                this.currentUser.userId,
                this.currentTeaSession.isPublic? "" : this.currentTeaSession.password,
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
                        this.modeTeaSession = "edit"; //Change to edit mode after Tea Session saved successfully
                        this.clearMenuForm();
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
                this.currentTeaSession.isPublic? undefined : this.currentTeaSession.password,
                this.currentTeaSession.description,
                this.currentTeaSession.imagePath instanceof File? this.currentTeaSession.imagePath: undefined
            ).subscribe(
                (res:any) => {
                    this.teaSessionSubmitMessage = res.message;

                    if(!res.error){
                        this.currentTeaSession = res.teaSession;
                        this.currentTeaSession.password = "";
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
                        res.menuItem.imagePath = this.appendRandomQueryToUrl(res.menuItem.imagePath);
                       this.currentMenuItem = [...this.currentMenuItem, res.menuItem];
                       this.clearMenuForm();

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
                        this.formMenuItem.imagePath? res.menuItem.imagePath = this.appendRandomQueryToUrl(res.menuItem.imagePath):"";
                        console.log(res.menuItem.imagePath);
                        this.currentMenuItem[index] = res.menuItem;
                        this.clearMenuForm();
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
                    this.currentMenuItem.splice(index, 1);
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
            } else {
                this.currentTeaSession.imagePath = event.target.files[0]; //Assign file to form

                const fileReader = new FileReader(); //Assign file to image preview as Base64;
                fileReader.onload = (_event) => this.teaSessionImagePreview = fileReader.result as string;
                fileReader.readAsDataURL(event.target.files[0]);
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
                this.formMenuItem.imagePath = event.target.files[0];//Assign file to form

                const fileReader = new FileReader(); //Assign file to image preview as Base64;
                fileReader.onload = (event) => this.menuItemImagePreview = fileReader.result as string;
                fileReader.readAsDataURL(event.target.files[0]);
                console.log("File assigned");
            }
        }
    }

    private getImageFileToTeaSessionFormAndPreview(url: string) {
        this.teaSessionService.getFileFromUrl(url).subscribe(
            (imageFile:File) => {
                this.currentTeaSession.imagePath = imageFile;
                console.log(imageFile);

                const fileReader = new FileReader();
                fileReader.onload = (event) => this.teaSessionImagePreview = fileReader.result as string;
                fileReader.readAsDataURL(imageFile);
                console.log("File assigned");
            },
            (err: any) => {
                console.log(err);
            })
    }

    private getImageFileToMenuFormAndPreview(url: string) {
        this.menuItemService.getFileFromUrl(url).subscribe(
            (imageFile:File) => {
                this.formMenuItem.imagePath = imageFile;
                console.log(imageFile);

                const fileReader = new FileReader();
                fileReader.onload = (event) => this.menuItemImagePreview = fileReader.result as string;
                fileReader.readAsDataURL(imageFile);
                console.log("File assigned");
        },
            (err: any) => {
                console.log(err);
            })
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
        console.log(menuItem.imagePath, typeof(menuItem.imagePath));
        console.log(typeof(menuItem.imagePath));
        if(typeof(menuItem.imagePath) == "string" && menuItem.imagePath){
            console.log("urlIsString",  menuItem.imagePath);
            this.getImageFileToMenuFormAndPreview(menuItem.imagePath)
        }
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
        this.formMenuItem.imagePath = null;
        this.menuItemImagePreview = undefined;
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
        this.teaSessionImagePreview = undefined;
    }

    private appendRandomQueryToUrl(url: string): string {
        return url + "?q=" + Math.random().toString(10);
    }

    private removeTeaSessionFileInputAndPreview(inputFile: HTMLInputElement) {
        inputFile.value = null;
        let files = Array.from(inputFile.files) //Refer to https://stackoverflow.com/questions/46544189/failed-to-set-an-indexed-property-on-filelist-index-property-setter-is-not-su
        files.splice(0, 1);

        this.currentTeaSession.imagePath = null;
        this.teaSessionImagePreview = undefined;
    }

    private removeMenuFileInputAndPreview(inputFile: HTMLInputElement) {
        inputFile.value = null;
        let files = Array.from(inputFile.files) //Refer to https://stackoverflow.com/questions/46544189/failed-to-set-an-indexed-property-on-filelist-index-property-setter-is-not-su
        files.splice(0, 1);

        this.formMenuItem.imagePath = null;
        this.menuItemImagePreview = null;
    }

    private checkCutOffAndTreatDate(cutOffDate: Date | string, treatDate: Date | string) {
        let convertedCutOffDate: number;
        let convertedTreatDate: number;

        if(cutOffDate && typeof(cutOffDate) == "string"){ //Type narrowing to string / date
            convertedCutOffDate = new Date(cutOffDate).getTime() / 1000;
        } else if (cutOffDate && typeof (cutOffDate) == "object"){
            convertedCutOffDate = new Date(cutOffDate).getTime() / 1000;
        }

        if(treatDate && typeof(treatDate) == "string") { //Type narrowing to string / date
            convertedTreatDate = new Date(treatDate).getTime() / 1000;
        } else if (treatDate && typeof(treatDate) == "object") {
            convertedTreatDate = new Date(treatDate).getTime() / 1000;
        }

        if(convertedCutOffDate > convertedTreatDate){
            console.log("cutOffDate", convertedCutOffDate, typeof(convertedCutOffDate));
            console.log("treatDate", convertedTreatDate, typeof(convertedTreatDate));
            this.cutOffAndTreatDateValid = false;
        } else {
            this.cutOffAndTreatDateValid = true;
        }
    }


}
