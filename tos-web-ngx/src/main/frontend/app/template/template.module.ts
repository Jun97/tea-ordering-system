import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { FormsModule }   from '@angular/forms';

import { TemplateDefaultHeaderComponent, TemplateDefaultFooterComponent, TemplateDefaultComponent }  from './default/template-default.component';


@NgModule({
    imports: [ 
        CommonModule,
        FormsModule,
        RouterModule,
    ],
    declarations: [ 
        TemplateDefaultHeaderComponent,        
        TemplateDefaultFooterComponent,
        TemplateDefaultComponent,
    ]
})
export class TemplateModule { }
