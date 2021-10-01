  import { NgModule } from '@angular/core';
  import { CommonModule, DatePipe } from '@angular/common';
  import { RouterModule } from '@angular/router';
  import { FormsModule, ReactiveFormsModule } from '@angular/forms';

  import { UserContentComponent }  from './user-content.component';
  import { UserAddComponent }  from './user-add.component';

  import { TemplateModule } from '../template/template.module';

  import { routes } from './user.routes';

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
    ],
    declarations: [ UserContentComponent, UserAddComponent ]
  })
  export class UserModule {
    public static routes = routes;
  }