import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';

const routes: Routes = [{
  path: '',
  component: MainLayoutComponent,
  children: [
    {
      path: '',
      loadChildren: () =>
        import('./modules/home/home.module').then(m => m.HomeModule)
    },
    {
      path: 'blog',
      loadChildren: () =>
        import('./modules/blog/blog.module').then(m => m.BlogModule)
    },
    {
       path: 'login',
      loadChildren: () =>
        import('./modules/login/login.module').then(m => m.LoginModule)
        
    }
  ]
}];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
