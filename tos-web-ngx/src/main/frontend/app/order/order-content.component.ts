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
    currentDate: Date = new Date();

    isMenuItemAccessGranted: boolean = undefined;
    isOrderReadOnly: boolean = undefined;
    isOrderItemUpdated: boolean = false;
    menuAuthMessage: string;

    formModelMenuAuthPassword: string = "";
    modelOrderQuantity: number[] = [];

    constructor(private route: ActivatedRoute,
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
        this.route.paramMap.subscribe(paramMap => {
            if (paramMap.has('teaSessionId')) {
                this.getTeaSessionById(Number(paramMap.get('teaSessionId')));
            }
        });
    }

    private getTeaSessionById(teaSessionId: number) {
        this.teaSessionService.getTeaSessionById(teaSessionId).subscribe(
            (res: TeaSessionModel) => {
                this.currentTeaSession = res;

                this.currentTeaSession.cutOffDate = this.datePipe.transform(res.cutOffDate, "dd-MM-yyyy");
                this.currentTeaSession.treatDate = this.datePipe.transform(res.treatDate, "dd-MM-yyyy");
                this.currentTeaSession.password = "";
                if(this.currentTeaSession.isPublic){
                    this.initMenuSection();
                    this.getOrderSummary(teaSessionId);
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

    private getOrderSummary(teaSessionId: number) {
        this.orderService.getOrderSummaryByTeaSessionId(this.currentTeaSession.teaSessionId).subscribe(
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
                    delete this.currentTeaSession.orders[0].orderItems[index];
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
        } else{
            this.isMenuItemAccessGranted = false;
        }
    }

    private onMenuPasswordSubmit() {
        this.findMenuItemByTeaSessionId(this.currentTeaSession.teaSessionId, this.formModelMenuAuthPassword);
    }
}
