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
      path: 'home2',
      loadChildren: () =>
        import('./modules/home2/home2.module').then(m => m.Home2Module)
    },
  ]
}];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
