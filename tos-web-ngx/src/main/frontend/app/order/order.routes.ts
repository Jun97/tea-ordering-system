import { Routes } from '@angular/router';

import { TemplateDefaultHeaderComponent, TemplateDefaultFooterComponent, TemplateDefaultComponent } from '../template/default/template-default.component';
import { OrderContentComponent } from './order-content.component';

export const routes:Routes = [
 {
    path: ':teaSessionId',
    component: TemplateDefaultComponent,
    children: [
      { path: '', component: OrderContentComponent, outlet: 'content' },
      { path: '', component: TemplateDefaultHeaderComponent, outlet: 'header' },
      { path: '', component: TemplateDefaultFooterComponent, outlet: 'footer' }
    ]
 },
];