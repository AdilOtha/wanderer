import { Component, OnInit } from '@angular/core';
import * as geofirex from 'geofirex';
import * as firebase from 'firebase/app';
import { Firestore, collectionData, collection, doc, setDoc } from '@angular/fire/firestore';
import { FirePoint, GeoFireClient } from 'geofirex';
import { Observable, BehaviorSubject, switchMap } from 'rxjs';
import * as GeoFire from 'geofire-common';

@Component({
  selector: 'app-home2',
  templateUrl: './home2.component.html',
  styleUrls: ['./home2.component.scss']
})
export class Home2Component implements OnInit {

  geo!: GeoFireClient;
  points!: Observable<any>;
  coords:any[] = [];
  currentCenter = {
    geopoint: {
      latitude: 34,
      longitude: -113
    }
  }
  radius:BehaviorSubject<number> = new BehaviorSubject(0.5);

  constructor(private firestore: Firestore) {     
    // const center:FirePoint = this.geo.point(40.5, -80.5);
    // const center = {
    //   geopoint: {
    //     latitude: 40.5,
    //     longitude: -80.5
    //   },
    //   geohash: GeoFire.geohashForLocation([40.5,-80.5])
    // }
    // const radius = 0.5;
    // const field = 'pos';

    // this.points = this.radius.pipe(
    //   switchMap(r =>{
    //     return this.geo.query('cities').within(center,r,field);
    //   })
    // );
    const collect = collection(firestore, 'cities');
    this.points = collectionData(collect);
    this.points.subscribe(d=>{
      console.log(d);
      
      this.coords.push({
        geopoint: {
          latitude: parseInt(d[0].geopoint.latitude),
          longitude: parseInt(d[0].geopoint.longitude)
        }
      });
      this.currentCenter = this.coords[0];     
    });
  }

  ngOnInit(): void {
  }

  trackByFn(_:any, doc:any) {
    return doc.id;
  }

  async createPoint(lat:any, lng: any) {
    const data = await collection(this.firestore, 'cities');

    // Use the convenience method
    // collection.setPoint('my-place', lat, lng)

    // Or be a little more explicit 
    const point = {
      geopoint: {
        latitude: lat,
        longitude: lng
      },
      geohash: GeoFire.geohashForLocation([lat,lng])
    }
    // const point = this.geo.point(lat, lng)
    // collection.add('my-place', { position: point.data })
    await setDoc(doc(data, 'cities'), point);
  }

}
