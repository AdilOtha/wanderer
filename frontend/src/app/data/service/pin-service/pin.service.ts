import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Pin } from '../../schema/pin';
@Injectable({
  providedIn: 'root',
})
export class PinService {
  controllerEndPoint: string = 'pin';

  constructor(private http: HttpClient) {}

  insertPin(pin: Pin) {
    return this.http.post(
      environment.APIENDPOINT + this.controllerEndPoint + '/createPin',
      pin
    );
  }

  getPinsByRadius(radius: number, centerLat: number, centerLng: number) {
    return this.http.get(
      environment.APIENDPOINT + this.controllerEndPoint + '/getPinsByRadius',
      {
        params: {
          radius,
          centerLat,
          centerLng,
        },
      }
    );
  }

  updatePin(
    pinId: number,
    locationName: string,
    latitude: number,
    longitude: number
  ) {
    const params: HttpParams = new HttpParams()
    .set('pinId', pinId)
    .set('locationName', locationName)
    .set('latitude', latitude)
    .set('longitude', longitude)
    return this.http.put(
      environment.APIENDPOINT + this.controllerEndPoint + '/updatePin',
      null,
      {
        params: params
      }
    );
  }
}
