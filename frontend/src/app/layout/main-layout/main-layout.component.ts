import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-main-layout',
  templateUrl: './main-layout.component.html',
  styleUrls: ['./main-layout.component.scss']
})
export class MainLayoutComponent implements OnInit {

  showHeader :boolean= true;

  constructor(private router:Router) { }


  ngOnInit(): void {
    this.showHeader = this.router.url !== "/login" 
  }
}
