import { Routes } from '@angular/router';

import { TemplateDefaultHeaderComponent, TemplateDefaultFooterComponent, TemplateDefaultComponent } from '../template/default/template-default.component';
import { HomeContentComponent } from './home-content.component';

export const routes:Routes = [
  {
    path: '',
    component: TemplateDefaultComponent,
    children: [
      { path: '', component: HomeContentComponent, outlet: 'content' },
      { path: '', component: TemplateDefaultHeaderComponent, outlet: 'header' },
      { path: '', component: TemplateDefaultFooterComponent, outlet: 'footer' }
    ]
  },
];