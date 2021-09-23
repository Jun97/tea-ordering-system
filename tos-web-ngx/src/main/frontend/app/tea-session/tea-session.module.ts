  import { NgModule } from '@angular/core';
  import { CommonModule, DatePipe } from '@angular/common';
  import { RouterModule } from '@angular/router';
  import { FormsModule, ReactiveFormsModule } from '@angular/forms';

  import { TeaSessionContentComponent }  from './tea-session-content.component';
  import { TeaSessionModifyComponent }  from './tea-session-modify.component';

  import { TemplateModule } from '../template/template.module';

  import * as resolve from './tea-session.resolve';
  import { routes } from './tea-session.routes';

  @NgModule({
    imports: [
      CommonModule,
      RouterModule.forChild(routes),
      FormsModule,
      ReactiveFormsModule,

      TemplateModule,
    ],
    providers: [
      resolve.ResolveTeaSessionByTeaSessionId,
      DatePipe,
    ],
    declarations: [ TeaSessionContentComponent, TeaSessionModifyComponent ]
  })
  export class TeaSessionModule {
    public static routes = routes;
  }