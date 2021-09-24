import { Routes } from '@angular/router';

import { TemplateDefaultHeaderComponent, TemplateDefaultFooterComponent, TemplateDefaultComponent } from '../template/default/template-default.component';
import { UserContentComponent }  from './user-content.component';
import { UserAddComponent }  from './user-add.component';

export const routes:Routes = [
 {
    path: '',
    component: TemplateDefaultComponent,
    children: [
      { path: '', component: UserContentComponent, outlet: 'content' },
      { path: '', component: TemplateDefaultHeaderComponent, outlet: 'header' },
      { path: '', component: TemplateDefaultFooterComponent, outlet: 'footer' }
    ]
 },

 {
       path: 'add',
       component: TemplateDefaultComponent,
       children: [
         { path: '', component: UserAddComponent, outlet: 'content' },
         { path: '', component: TemplateDefaultHeaderComponent, outlet: 'header' },
         { path: '', component: TemplateDefaultFooterComponent, outlet: 'footer' }
       ],
 },
];