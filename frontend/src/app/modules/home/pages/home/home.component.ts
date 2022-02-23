import { Component, OnInit } from '@angular/core';
import { Firestore, collectionData, collection } from '@angular/fire/firestore';
import { Observable, BehaviorSubject } from 'rxjs';
import { MouseEvent } from '@agm/core';
import { PinService } from 'src/app/data/service/pin-service/pin.service';
import { Pin } from 'src/app/data/schema/pin';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {  
  pinCreationUpdates!: Observable<any>;
  coords:any[] = [];  

  // google maps zoom level
  zoom: number = 8;

  // initial center position for the map
  latitude: number = 51.678418;
  longitude: number = 7.809007;
  pinLocationChosen: boolean = false;

  markers: marker[] = [
	  {
		  lat: 44.6476,
		  lng: 63.5728,
		  label: 'A',
		  draggable: true,
      isSaved: true
	  },
	  {
		  lat: 51.373858,
		  lng: 7.215982,
		  label: 'B',
		  draggable: false,
      isSaved: true
	  },
	  {
		  lat: 51.723858,
		  lng: 7.895982,
		  label: 'C',
		  draggable: true,
      isSaved: true
	  }
  ]

  radius: number = 5;

  constructor(private firestore: Firestore, private pinService: PinService) {         
    const dataList = collection(this.firestore, 'pin_updates');
    this.pinCreationUpdates = collectionData(dataList);
    this.pinCreationUpdates.subscribe(update=>{
      console.log(update);
      this.pinService.getPinsByRadius(this.radius, this.latitude, this.longitude)
        .subscribe({
          next: (data:any)=>{
            console.log(data);            
          },
          error: (err:HttpErrorResponse) => {
            console.log(err);
          }
        });
    });
    // this.points = collectionData(collect);
    // this.points.subscribe(d=>{
    //   console.log(d);
      
    //   this.coords.push({
    //     geopoint: {
    //       latitude: parseInt(d[0].geopoint.latitude),
    //       longitude: parseInt(d[0].geopoint.longitude)
    //     }
    //   });
    //   this.currentCenter = this.coords[0];     
    // });
  }

  ngOnInit(): void {
    // get user's current location
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(position => {        
       this.latitude = position.coords.latitude;
       this.longitude = position.coords.longitude;       
       this.pinLocationChosen = true;
       console.log({
         latitude: position.coords.latitude,
         longitude: position.coords.longitude
       });
       
     });
   }
  }

  createPin($event: MouseEvent){
    console.log($event);
    if(this.markers[this.markers.length-1].isSaved===false){
      this.markers.splice(this.markers.length-1, 1);      
    }    
    this.markers.push({
      lat: $event.coords.lat,
      lng: $event.coords.lng,
      label: '',
      draggable: true,
      isSaved: false
    });
  }

  clickedPin(selectedMarker: marker, index: number) {
    this.markers[index].isSaved=true;
    console.log(`clicked the marker: ${selectedMarker.label || index}`);
    let pin: Pin = {
      latitude: selectedMarker.lat,
      longitude: selectedMarker.lng,
      locationName: ''
    };

    this.pinService.insert(pin).subscribe({
      next: (data: any)=>{
        console.log(data);        
      },
      error: (err: HttpErrorResponse)=>{
        console.log(err);
      },
      complete: ()=>{
        
      }
    });
  }

  pinDragEnd(m: marker, $event: MouseEvent) {
    console.log('dragEnd', m, $event);
  }

}

interface marker {
	lat: number;
	lng: number;
	label: string;
	draggable: boolean;
  isSaved: boolean;
}
