import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TestService } from './test.service';

@NgModule({
    imports: [ 
      CommonModule,
    ],
  providers:
  [
    TestService
  ],
})
export class ServiceModule {
}
