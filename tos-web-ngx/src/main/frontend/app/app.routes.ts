import { Routes } from '@angular/router';

export const routes: Routes = [
  
  { path: 'home', loadChildren: './home/home.module#HomeModule' },
  { path: 'test', loadChildren: './test/test.module#TestModule' },
  // { path: 'login', loadChildren: './login/login.module#LoginModule' },
  // { path: 'user', loadChildren: './users/users.module#UserModule' },
  // { path: 'order', loadChildren: './orders/orders.module#OrderModule' },
  // { path: 'tea-session', loadChildren: './tea-session/tea-session.module#TeaSessionModule' },
    
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: '**', redirectTo: 'home' },
];
