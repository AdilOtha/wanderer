import { Component, OnInit } from '@angular/core';
import { Firestore, collectionData, collection } from '@angular/fire/firestore';
import { debounceTime, Observable, Subject } from 'rxjs';
import { MouseEvent } from '@agm/core';
import { PinService } from 'src/app/data/service/pin-service/pin.service';
import { Pin } from 'src/app/data/schema/pin';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  providers: [],
})
export class HomeComponent implements OnInit {
  //Firestore Data
  pinCreationUpdates!: Observable<any>;
  pinUpdateCollection: string = 'pin_updates';

  // Pin Center and Radius Subjects for 2-way communication between UI and Keyboard input
  centerChangeSubject: Subject<any> = new Subject();
  radiusChangeSubject: Subject<any> = new Subject();

  // google maps zoom level
  zoom: number = 8;

  // default radius (in meters)
  radius: number = 50000;

  // initial center position for the map
  latitude: number = 21.7645;
  longitude: number = 72.1519;

  // Saved Pins
  savedPins: Pin[] = [
    // {
    //   latitude: 44.6476,
    //   longitude: 63.5728,
    //   locationName: 'A',
    //   isSaved: true,
    // },
    // {
    //   latitude: 51.373858,
    //   longitude: 7.215982,
    //   locationName: 'B',
    //   isSaved: true,
    // },
    // {
    //   latitude: 51.723858,
    //   longitude: 7.895982,
    //   locationName: 'C',
    //   isSaved: true,
    // },
  ];

  // Pins created by User
  newlyCreatedPins: Pin[] = [];

  // User Identity Data
  userId!: number;

  // Pin Modal Data
  mapPanelHeader: string = 'Navigator';

  pinModalHeaderLabel: string = 'Add/Edit Pin Details';
  displayPinModal: boolean = false;

  currentPin!: Pin;
  currentPinIndex!: number;

  pinForm: FormGroup = this.fb.group({
    locationName: ['', [Validators.required]],
    // latitude: ['', [Validators.required]],
    // longitude: [{ value: '', disabled: true }],
    // userId: [''],
  });
  submitted: boolean = false;

  constructor(
    private firestore: Firestore,
    private pinService: PinService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.userId = 3;
    // get user's current location
    this.setCurrentLocation();

    const dataList = collection(this.firestore, this.pinUpdateCollection);
    this.pinCreationUpdates = collectionData(dataList);
    this.pinCreationUpdates.subscribe((update) => {
      console.log(update);
      this.getPinsByRadiusFromService();
    });

    this.centerChangeSubject.pipe(debounceTime(1000)).subscribe((data: any) => {
      this.latitude = data.lat;
      this.longitude = data.lng;
      this.getPinsByRadiusFromService();
    });

    this.radiusChangeSubject
      .pipe(debounceTime(1000))
      .subscribe((newRadius: any) => {
        console.log(newRadius);

        this.radius = parseInt(newRadius.toString());

        this.getPinsByRadiusFromService();
      });
  }

  createPin($event: MouseEvent) {
    console.log($event);
    console.log(this.savedPins);

    if (
      this.savedPins.length > 0 &&
      !this.savedPins[this.savedPins.length - 1].isSaved
    ) {
      this.savedPins.splice(this.savedPins.length - 1, 1);
    }
    this.savedPins.push({
      latitude: $event.coords.lat,
      longitude: $event.coords.lng,
      locationName: '',
      isSaved: false,
      pinId: -1,
      userId: this.userId,
    });
  }

  openPinModal(pin: Pin, index: number) {
    this.setCurrentPin(pin, index);
    this.displayPinModal = true;
  }

  setCurrentPin(pin: Pin, index: number) {
    this.currentPin = pin;
    this.currentPinIndex = index;
    this.pinForm.controls['locationName'].patchValue(
      this.savedPins[index].locationName
    );
    console.log('Current Pin Index: ' + this.currentPinIndex);
  }

  get pinFormControls() {
    return this.pinForm.controls;
  }

  savePin() {
    this.submitted = true;

    if (this.pinForm.valid) {
      console.log('Saved Pin Index: ' + this.currentPinIndex);

      this.savedPins[this.currentPinIndex].isSaved = true;
      console.log(
        `clicked the marker: ${
          this.currentPin.locationName || this.currentPinIndex
        }`
      );
      let pin: Pin = {
        latitude: this.currentPin.latitude,
        longitude: this.currentPin.longitude,
        locationName: this.pinForm.controls['locationName'].value,
        userId: this.currentPin.userId,
        pinId: -1,
        isSaved: true,
      };

      // if Pin exists then update Pin
      if (this.currentPin.pinId !== -1) {
        pin.pinId = this.currentPin.pinId;
        this.pinService
          .updatePin(pin.pinId, pin.locationName, pin.latitude, pin.longitude)
          .subscribe({
            next: (data: any) => {
              if (data === null) {
                console.log('Error while updating pin');
              } else {
                data.isSaved = true;
                this.savedPins[this.currentPinIndex] = data;
              }
              this.displayPinModal = false;
              this.submitted = false;
            },
            error: (err: any) => {
              console.log(err);
              this.displayPinModal = false;
              this.submitted = false;
            },
          });
      } else { // else if Pin does not exist then save Pin
        this.pinService.insertPin(pin).subscribe({
          next: (data: any) => {
            console.log(data);
            data.isSaved = true;
            if (pin.pinId && pin.pinId !== -1) {
              this.newlyCreatedPins.push(data);
            }
            this.savedPins[this.currentPinIndex] = data;
            this.displayPinModal = false;
            this.submitted = false;
          },
          error: (err: HttpErrorResponse) => {
            console.log(err);
            this.displayPinModal = false;
            this.submitted = false;
          },
        });
      }
    }
  }

  getPinsByRadiusFromService() {
    const radiusInKms: number = this.radius / 1000;
    this.pinService
      .getPinsByRadius(radiusInKms, this.latitude, this.longitude)
      .subscribe({
        next: (data: any) => {
          this.savedPins = data;
          for (let pin of data) {
            pin.isSaved = true;
          }
          this.savedPins = [...this.savedPins, ...this.newlyCreatedPins];

          console.log(this.savedPins);
        },
        error: (err: HttpErrorResponse) => {
          console.log(err);
        },
      });
  }

  pinDragEnd(m: Pin, index: number, event: MouseEvent) {
    console.log('dragEnd', m, event);
    m.latitude = event.coords.lat;
    m.longitude = event.coords.lng;
  }

  identifyPin(index: number, pin: Pin) {
    return pin.pinId;
  }

  centerChange(event: any) {
    this.centerChangeSubject.next(event);
  }

  radiusChange(event: any) {
    console.log(event);

    this.radiusChangeSubject.next(event);
  }

  convertKilometerToMeter(valueInKilometers: number) {
    const unit = 1000.0;
    console.log(valueInKilometers);
    this.radius = valueInKilometers * unit;
  }

  setCurrentLocation() {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition((position) => {
        this.latitude = position.coords.latitude;
        this.longitude = position.coords.longitude;
        console.log({
          latitude: position.coords.latitude,
          longitude: position.coords.longitude,
        });
      });
    }
  }

  // showError(error: any) {
  //   console.log(error);

  //   switch(error.code) {
  //     case error.PERMISSION_DENIED:
  //       this.toastr.warning('User denied the request for Geolocation');
  //       // this.messageService.add({severity:'warn', summary: 'Warn', detail: 'User denied the request for Geolocation.'});
  //       break;
  //     case error.POSITION_UNAVAILABLE:
  //       // this.messageService.add({severity:'warn', summary: 'Warn', detail: 'Location information is unavailable.'});
  //       break;
  //     case error.TIMEOUT:
  //       // this.messageService.add({severity:'warn', summary: 'Warn', detail: 'The request to get user location timed out.'});
  //       break;
  //     case error.UNKNOWN_ERROR:
  //       // this.messageService.add({severity:'warn', summary: 'Warn', detail: 'An unknown error occurred.'});
  //       break;
  //   }
  // }
}
