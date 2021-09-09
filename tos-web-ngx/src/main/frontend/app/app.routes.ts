import { Routes } from '@angular/router';

export const routes: Routes = [
  
  { path: 'home', loadChildren: './home/home.module#HomeModule' },
  { path: 'test', loadChildren: './test/test.module#TestModule' },
    
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: '**', redirectTo: 'home' },
];
