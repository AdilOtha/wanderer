import { Component, OnInit } from '@angular/core';
import { Firestore, collectionData, collection } from '@angular/fire/firestore';
import { Observable, BehaviorSubject } from 'rxjs';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {  
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
    const collect = collection(this.firestore, 'cities');
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

}
