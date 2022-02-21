import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { Home2RoutingModule } from './home2-routing.module';
import { Home2Component } from './pages/home2/home2.component';

import { AgmCoreModule } from '@agm/core';

@NgModule({
  declarations: [
    Home2Component
  ],
  imports: [
    CommonModule,
    Home2RoutingModule,
    AgmCoreModule
  ]
})
export class Home2Module { }
