import { Routes } from '@angular/router';

export const routes: Routes = [
  
  { path: 'home', loadChildren: './home/home.module#HomeModule' },
  { path: 'test', loadChildren: './test/test.module#TestModule' },
  { path: 'login', loadChildren: './login/login.module#LoginModule' },
  { path: 'tea-session', loadChildren: './tea-session/tea-session.module#TeaSessionModule' },
  { path: 'user', loadChildren: './user/user.module#UserModule' },
  { path: 'order', loadChildren: './order/order.module#OrderModule' },
    
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: '**', redirectTo: 'home' },
];
