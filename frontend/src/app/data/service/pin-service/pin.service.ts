import { Injectable } from '@angular/core';
import { Pin } from '../../schema/pin';
import { CoreHttpService } from '../httpinterceptor/core-http.service';
@Injectable({
  providedIn: 'root'
})
export class PinService {
  apiControllerName: string = 'pin';

  constructor(private http: CoreHttpService) { }

  insert(pin: Pin){
      return this.http.postRequest(this.apiControllerName + '/create', pin);
  }

  getPinsByRadius(radius: number, centerLat: number, centerLng: number){    
    return this.http.getRequestWithParameters(this.apiControllerName + '/getPinsByRadius', {
      radius, centerLat, centerLng
    });
  }
}
