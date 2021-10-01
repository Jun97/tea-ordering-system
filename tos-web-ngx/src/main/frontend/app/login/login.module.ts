  import { NgModule } from '@angular/core';
  import { CommonModule } from '@angular/common';
  import { RouterModule } from '@angular/router';
  import { FormsModule } from '@angular/forms';

  import { LoginContentComponent }  from './login-content.component';

  import { TemplateModule } from '../template/template.module';

  import { routes } from './login.routes';

  @NgModule({
    imports: [
      CommonModule,
      RouterModule.forChild(routes),
      FormsModule,

      TemplateModule,
    ],
    declarations: [ LoginContentComponent ]
  })
  export class LoginModule {
    public static routes = routes;
  }