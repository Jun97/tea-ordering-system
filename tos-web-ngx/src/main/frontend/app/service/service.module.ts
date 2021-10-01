import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TestService } from './test.service';
import {UserService} from "./user.service";
import {TeaSessionService} from "./tea-session.service";
import {OrderService} from "./order.service";
import {OrderItemService} from "./order-item.service";

@NgModule({
    imports: [ 
      CommonModule,
    ],
  providers:
  [
    TestService,
      UserService,
      TeaSessionService,
      OrderService,
      OrderItemService
  ],
})
export class ServiceModule {
}
