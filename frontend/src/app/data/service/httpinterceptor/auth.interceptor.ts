import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpHeaders
} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor() {}

  intercept(
    request: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {
    const httpHeaders: HttpHeaders = new HttpHeaders();
    httpHeaders.set('Access-Control-Allow-Origin', '*')
    httpHeaders.set('Access-Control-Allow-Credentials', 'true');
    request = request.clone({
      withCredentials: true,
      headers: httpHeaders
    });
    return next.handle(request);
  }
}
