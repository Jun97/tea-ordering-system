import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

import { HomeContentComponent }  from './home-content.component';
import { TemplateModule } from '../template/template.module';

import { routes } from './home.routes';

@NgModule({
  imports: [ 
    CommonModule,
    RouterModule.forChild(routes),
    FormsModule,
    
    TemplateModule,
  ],
  declarations: [ HomeContentComponent]
})
export class HomeModule {
  public static routes = routes; 
}
