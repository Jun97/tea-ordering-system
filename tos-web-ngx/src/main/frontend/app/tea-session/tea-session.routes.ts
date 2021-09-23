import { Routes } from '@angular/router';

import { TemplateDefaultHeaderComponent, TemplateDefaultFooterComponent, TemplateDefaultComponent } from '../template/default/template-default.component';
import { TeaSessionContentComponent } from './tea-session-content.component';
//import { TeaSessionCompleteComponent } from './tea-session-complete.component';
import { TeaSessionModifyComponent } from './tea-session-modify.component';
//import { TeaSessionVerifyPasswordComponent } from './tea-session-verify-password.component';
import * as resolve from './tea-session.resolve';

export const routes:Routes = [
 {
    path: '',
    component: TemplateDefaultComponent,
    children: [
      { path: '', component: TeaSessionContentComponent, outlet: 'content' },
      { path: '', component: TemplateDefaultHeaderComponent, outlet: 'header' },
      { path: '', component: TemplateDefaultFooterComponent, outlet: 'footer' }
    ]
 },



 {
     path: 'add',
     component: TemplateDefaultComponent,
     children: [
       { path: '', component: TeaSessionModifyComponent, outlet: 'content' },
       { path: '', component: TemplateDefaultHeaderComponent, outlet: 'header' },
       { path: '', component: TemplateDefaultFooterComponent, outlet: 'footer' }
     ]
 },



 {
     path: 'edit/:teaSessionId',
     component: TemplateDefaultComponent,
     children: [
       { path: '', component: TeaSessionModifyComponent, outlet: 'content'
         , resolve:
         {
           teaSession: resolve.ResolveTeaSessionByTeaSessionId,
         }
       },
       { path: '', component: TemplateDefaultHeaderComponent, outlet: 'header' },
       { path: '', component: TemplateDefaultFooterComponent, outlet: 'footer' }
     ]
 },
];