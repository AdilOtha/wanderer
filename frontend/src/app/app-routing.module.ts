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
      path: 'user-profile',
      loadChildren: () =>
        import('./modules/user-profile/user-profile.module').then(m => m.UserProfileModule)
    },
  ]
}];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
