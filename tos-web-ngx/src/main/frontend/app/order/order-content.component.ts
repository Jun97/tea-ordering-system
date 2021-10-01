import { Component, OnInit} from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { TeaSessionService } from '../service/tea-session.service';
import { OrderService } from '../service/order.service';

import { TeaSessionModel } from '../model/tea-session.model';
import { OrderModel } from '../model/order.model';
import {UserModel} from "../model/user.model";
import {MenuItemService} from "../service/menu-item.service";
import {OrderItemModel} from "../model/order-item.model";
import {OrderItemService} from "../service/order-item.service";
@Component(
{
  templateUrl: './order-content.component.htm'
})
export class OrderContentComponent implements OnInit
{
    objectKeys = Object.keys; //Object.keys() returns an array of a given object's own property names, in the same order as we get with a normal loop.

    currentTeaSession: TeaSessionModel;
    currrentUser: UserModel;
    currentOrderSummary: Map<string, any>;
    todayDate: Date = new Date();
    currentTeaSessionId: number;
    currentCipherText: string;

    isMenuItemAccessGranted: boolean = undefined;
    isOrderReadOnly: boolean = undefined;
    isOrderItemUpdated: boolean = false;
    menuAuthMessage: string;

    formModelMenuAuthPassword: string = "";

    constructor(private activatedRoute: ActivatedRoute,
                private router: Router,
                private teaSessionService:TeaSessionService,
                private menuItemService: MenuItemService,
                private orderService:OrderService,
                private orderItemService: OrderItemService,
                private datePipe:DatePipe)
    {}

    ngOnInit() {
      this.currrentUser = JSON.parse(localStorage.getItem('currentUser'));
      this.parseTeaSessionParam();
    }

    private parseTeaSessionParam() {
        this.activatedRoute.paramMap.subscribe(paramMap => {
            if (paramMap.has('teaSessionId')) {
                this.currentTeaSessionId = Number(paramMap.get('teaSessionId'));
                this.getTeaSessionById(this.currentTeaSessionId);
            }
        });

        this.activatedRoute.queryParamMap.subscribe(queryParamMap => {
            if (queryParamMap.has('ciphertext')) {
                this.currentCipherText = queryParamMap.get('ciphertext');

                console.log("hasCipher",queryParamMap.get('ciphertext'));
            }
        });

    }

    private getTeaSessionById(teaSessionId: number) {
        this.teaSessionService.getTeaSessionById(teaSessionId).subscribe(
            (res: TeaSessionModel) => {
                this.currentTeaSession = res;

                // this.currentTeaSession.cutOffDate = this.datePipe.transform(res.cutOffDate, "dd-MM-yyyy");
                // this.currentTeaSession.treatDate = this.datePipe.transform(res.treatDate, "dd-MM-yyyy");
                this.currentTeaSession.password = "";

                    this.initMenuSection();
                    this.getOrderSummary(teaSessionId);

                    this.checkTodayAndCutOffDate(this.todayDate, this.currentTeaSession.cutOffDate);


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
                    this.currentTeaSession.menuItems = res.menuItem;
                    this.isMenuItemAccessGranted = true;
                }
                this.menuAuthMessage = res.message;
                this.addUserOrder();
                console.log("menuItemResponse:", this.currentTeaSession.menuItems);
            },
            (err: any) => {
                console.log("findMenuItemByTeaSessionIdAndPassword:", err);
            })
    }


    private findMenuItemByShareLink(teaSessionId: number, ciphertext: string) {
        this.menuItemService.findMenuItemByShareLink(teaSessionId, ciphertext).subscribe(
            (res:any) => {
                if(!res.error){
                    this.currentTeaSession.menuItems = res.menuItem;
                    this.isMenuItemAccessGranted = true;
                    this.addUserOrder();

                } else {
                    this.isMenuItemAccessGranted = false;
                }

                if(res.message) {
                    this.menuAuthMessage = res.message;
                }
                console.log("menuItemResponse:", this.currentTeaSession.menuItems);
            },
            (err: any) => {
                this.isMenuItemAccessGranted = false;
                console.log("findMenuItemByTeaSessionIdAndPassword:", err);
            })
    }

    private getOrderSummary(teaSessionId: number) {
        this.orderService.getOrderSummaryByTeaSessionId(teaSessionId).subscribe(
            (res: any) => {
                this.currentOrderSummary = res.orderSummary;
            },
            (err: any) => {
                console.log(err);
            }
        )
    }

    private addUserOrder() {
        this.orderService.addOrder(this.currentTeaSession.teaSessionId, this.currrentUser.userId).subscribe(
            (res: any) => {
                if(!res.error) {
                    this.currentTeaSession.orders = [];
                    this.currentTeaSession.orders[0] = res.order;
                    this.findUserOrderItemByOrderId(res.order.orderId);
                }
            },
            (err: any) => {
                console.log(err);
            }
        )
    }

    private findUserOrderItemByOrderId(orderId: number) {
        this.orderItemService.findOrderItemByOrderId(orderId).subscribe(
            (res: OrderItemModel[]) => {
                this.currentTeaSession.orders[0].orderItems = [];
                this.currentTeaSession.orders[0].orderItems = res;
                this.isOrderItemUpdated = true;
            },
            (err: any) => {
                console.log(err);
            });
    }

    private updateUserOrderItem(orderId: number, menuItemId: number, quantity: number) {
        // let quantity = this.modelOrderQuantity[quantityModelIndex];
        this.orderItemService.addOrderItem(orderId, menuItemId, quantity).subscribe(
            (res: any) => {
                console.log(res);
                if(!res.error) {
                    let index = this.currentTeaSession.orders[0].orderItems.findIndex(x => x.orderItemId === res.orderItem.orderItemId);
                    console.log("index",index);
                    if(index != -1) {
                        this.currentTeaSession.orders[0].orderItems[index] = res.orderItem;
                    } else {
                        this.currentTeaSession.orders[0].orderItems = [...this.currentTeaSession.orders[0].orderItems, res.orderItem];
                    }
                }
            },
            (err: any) => {
                console.log(err);
            }
        );
    }

    private deleteUserOderItemById(orderItemId: number) {
        this.orderItemService.deleteOrderItemByOrderItemId(orderItemId).subscribe(
            (res: any) => {
                if (!res.error) {
                    let index = this.currentTeaSession.orders[0].orderItems.findIndex(x => x.orderItemId === orderItemId);
                    this.currentTeaSession.orders[0].orderItems.splice(index, 1)
                }
            },
            (err: any) => {
                console.log(err);
            }
        )
    }

    private initMenuSection() {
        if (this.currentTeaSession.isPublic) {
            this.isMenuItemAccessGranted = true;
            this.findMenuItemByTeaSessionId(this.currentTeaSession.teaSessionId, "");
        } else if(!this.currentTeaSession.isPublic && this.currentCipherText){
            this.findMenuItemByShareLink(this.currentTeaSessionId, this.currentCipherText)
        } else {
            this.isMenuItemAccessGranted = false;
        }
    }

    private onMenuPasswordSubmit() {
        this.findMenuItemByTeaSessionId(this.currentTeaSession.teaSessionId, this.formModelMenuAuthPassword);
    }

    private checkTodayAndCutOffDate(today: Date | string, cutOffDate: Date | string) {
        let convertedCutOffDate: number;
        let convertedToday: number;
        console.log(today, typeof(today));
        console.log(cutOffDate, typeof(cutOffDate));

        if(today && typeof(today) == "string") { //Type narrowing to string / date
            convertedToday = new Date(today).getTime() / 1000;
        } else if (today && typeof(today) == "object") {
            convertedToday = new Date(today).getTime() / 1000;
        } else if (today && typeof(today) == "number") {
            convertedToday = new Date(today).getTime() / 1000;
        }

        if(cutOffDate && typeof(cutOffDate) == "string"){ //Type narrowing to string / date
            convertedCutOffDate = new Date(cutOffDate).getTime() / 1000;
        } else if (cutOffDate && typeof (cutOffDate) == "object"){
            convertedCutOffDate = new Date(cutOffDate).getTime() / 1000;
        } else if (cutOffDate && typeof (cutOffDate) == "number"){
            convertedCutOffDate = new Date(cutOffDate).getTime() / 1000;
        }

        if(convertedToday > convertedCutOffDate){ //Today past cut off date
            console.log("cutOffDate", convertedCutOffDate, typeof(convertedCutOffDate));
            console.log("today", convertedToday, typeof(convertedToday));
            this.isOrderReadOnly = true;
        } else {
            this.isOrderReadOnly = false;
        }
    }
}
