import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

import { TestContentComponent }  from './test-content.component';
import { TestModifyComponent }  from './test-modify.component';
import { TestCompleteComponent }  from './test-complete.component';

import { TemplateModule } from '../template/template.module';

import * as resolve from './test.resolve';
import { routes } from './test.routes';

@NgModule({
  imports: [ 
    CommonModule,
    RouterModule.forChild(routes),
    FormsModule,
    
    TemplateModule,
  ],
  providers: [
    resolve.ResolveTestByTestId,
  ],
  declarations: [ TestContentComponent, TestModifyComponent, TestCompleteComponent ]
})
export class TestModule {
  public static routes = routes; 
}
