import { Component, OnInit } from '@angular/core';
import {MenuItem} from 'primeng/api';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/data/service/auth-service/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnInit {

  items: MenuItem[] = [];
  avtarMenuItems: MenuItem[] = [];

  constructor (private authService: AuthService, private router: Router) {}

    ngOnInit() {
        this.items = [
            {
                label: 'Home',
                routerLink: '/'
            },
            {
                label: 'Blog',
                icon: 'pi pi-fw pi-pencil',
                items: [
                    {label: 'Delete', icon: 'pi pi-fw pi-trash'},
                    {label: 'Refresh', icon: 'pi pi-fw pi-refresh'}
                ]
            },
            {
                label: 'Your Future Trips',
                icon: 'pi pi-car',
                routerLink: '/future-trip'
            }
        ];
        this.avtarMenuItems = [
            {
                label: 'Profile',
                routerLink: '/user-profile',
                icon: 'pi pi-id-card'
            },
            {
                label: 'Logout',
                icon: 'pi pi-sign-out',
                command: (event) => {
                    this.authService.logout();
                }
            }
        ];
    }
}
