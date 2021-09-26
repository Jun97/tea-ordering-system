  import { NgModule } from '@angular/core';
  import { CommonModule, DatePipe } from '@angular/common';
  import { RouterModule } from '@angular/router';
  import { FormsModule, ReactiveFormsModule } from '@angular/forms';

  import { OrderContentComponent }  from './order-content.component';

  import { TemplateModule } from '../template/template.module';

  import { routes } from './order.routes';
  import {MenuItemService} from "../service/menu-item.service";

  @NgModule({
    imports: [
      CommonModule,
      RouterModule.forChild(routes),
      FormsModule,
      ReactiveFormsModule,

      TemplateModule,
    ],
    providers: [
      DatePipe,
      MenuItemService
    ],
    declarations: [ OrderContentComponent ]
  })
  export class OrderModule {
    public static routes = routes;
  }