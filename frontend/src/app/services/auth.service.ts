import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';

const httpOption = {
  headers: new HttpHeaders({
    'Access-Control-Allow-Origin' : 'http://localhost:4200',
  })
}

@Injectable({
  providedIn: 'root'
})

export class AuthService {


  constructor(private cookieService: CookieService, private httpClient: HttpClient) { }

  isLoggedIn() {
    const token = this.cookieService.get('token'); // get token from local storage
    console.log('Token', token);
    if(token) {
      const payload = atob(token.split('.')[1]); // decode payload of token
      const parsedPayload = JSON.parse(payload); // convert payload into an Object
      return parsedPayload.exp > Date.now() / 1000; // check if token is expired
    } else {
      return false;
    }
  }

  addToken(token: string) {
    this.cookieService.set('token', token);
  }

  logout() {
    this.cookieService.delete('token');
    this.httpClient.get('http://localhost:8080/logout', httpOption).subscribe(p=> console.log("Redirected to logout"));
  }
}
