import { Routes } from '@angular/router';

import { TemplateDefaultHeaderComponent, TemplateDefaultFooterComponent, TemplateDefaultComponent } from '../template/default/template-default.component';
import { TestContentComponent } from './test-content.component';
import { TestModifyComponent } from './test-modify.component';
import { TestCompleteComponent } from './test-complete.component';
import * as resolve from './test.resolve';

export const routes:Routes = [
  {
    path: '',
    component: TemplateDefaultComponent,
    children: [
      { path: '', component: TestContentComponent, outlet: 'content' },
      { path: '', component: TemplateDefaultHeaderComponent, outlet: 'header' },
      { path: '', component: TemplateDefaultFooterComponent, outlet: 'footer' }
    ]
  },
  {
    path: 'add',
    component: TemplateDefaultComponent,
    children: [
      { path: '', component: TestModifyComponent, outlet: 'content'
      },
      { path: '', component: TemplateDefaultHeaderComponent, outlet: 'header' },
      { path: '', component: TemplateDefaultFooterComponent, outlet: 'footer' },
    ]
  },
  {
    path: 'modify/:testId',
    component: TemplateDefaultComponent,
    children: [
      { path: '', component: TestModifyComponent, outlet: 'content'
        , resolve:
        {
          test: resolve.ResolveTestByTestId,
        }
      },
      { path: '', component: TemplateDefaultHeaderComponent, outlet: 'header' },
      { path: '', component: TemplateDefaultFooterComponent, outlet: 'footer' }
    ]
  },
  {
    path: 'complete/:mode',
    component: TemplateDefaultComponent,
    children: [
      { path: '', component: TestCompleteComponent, outlet: 'content' },
      { path: '', component: TemplateDefaultHeaderComponent, outlet: 'header' },
      { path: '', component: TemplateDefaultFooterComponent, outlet: 'footer' },
    ]
  },
];