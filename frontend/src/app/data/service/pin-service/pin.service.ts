import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Pin } from '../../schema/pin';
@Injectable({
  providedIn: 'root'
})
export class PinService {
  apiEndpoint: string = environment.APIENDPOINT + 'pin';

  constructor(private http: HttpClient) { }

  insertPin(pin: Pin){
      return this.http.post(this.apiEndpoint +'/createPin', pin);
  }

  getPinsByRadius(radius: number, centerLat: number, centerLng: number){    
    return this.http.get(this.apiEndpoint + '/getPinsByRadius', {
      params: {
        radius, centerLat, centerLng
      }
    });
  }
}
