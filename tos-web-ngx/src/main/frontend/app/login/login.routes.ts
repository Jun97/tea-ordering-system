import { Routes } from '@angular/router';

import { TemplateDefaultHeaderComponent, TemplateDefaultFooterComponent, TemplateDefaultComponent } from '../template/default/template-default.component';
import { LoginContentComponent } from './login-content.component';

export const routes:Routes = [
 {
    path: '',
    component: TemplateDefaultComponent,
    children: [
      { path: '', component: LoginContentComponent, outlet: 'content' },
      { path: '', component: TemplateDefaultHeaderComponent, outlet: 'header' },
      { path: '', component: TemplateDefaultFooterComponent, outlet: 'footer' }
    ]
 },
];