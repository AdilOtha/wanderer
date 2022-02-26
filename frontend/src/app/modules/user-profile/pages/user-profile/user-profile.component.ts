import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Validators } from '@angular/forms';
import { UserProfile } from 'src/app/data/schema/user-profile';
import { UserProfileService } from 'src/app/data/service/user-profile/user-profile.service';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { NgxSpinnerService } from "ngx-spinner";

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss'],
})
export class UserProfileComponent implements OnInit {
  title: string = 'User Profile';

  editProfileButtonLabel: string = 'Edit Profile Details';

  modalDisplay: boolean = false;

  submitted: boolean = false;

  profileImage: SafeResourceUrl | string =
    'https://images.unsplash.com/photo-1645785538675-f81e7dbab4b4?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1974&q=80';
  firstName: string = 'Adil';
  lastName: string = 'Otha';
  email: string = 'adil@gmail.com';

  fileLimit: number = 1;
  uploadedFilename: string = '';

  saveForm: FormGroup = this.fb.group({
    firstName: [this.firstName, [Validators.required]],
    lastName: [this.lastName, [Validators.required]],
    profileImage: [''],
  });

  constructor(
    private fb: FormBuilder,
    private userProfileService: UserProfileService,
    private sanitizer: DomSanitizer,
    private spinner: NgxSpinnerService
  ) {}

  ngOnInit(): void {
    console.log('User-profile component loaded');
  }

  get f() {
    return this.saveForm.controls;
  }

  showModal(): void {
    this.saveForm.controls['profileImage'].setValue('');
    this.saveForm.controls['firstName'].setValue(this.firstName);
    this.uploadedFilename = '';
    this.modalDisplay = true;
  }

  onSubmitClick() {
    this.submitted = true;
    if (this.saveForm.valid) {
      console.log('form submitted');
      const userProfile: UserProfile = {
        firstName: this.saveForm.controls['firstName'].value,
        lastName: this.saveForm.controls['lastName'].value,
        profileImage: this.saveForm.controls['profileImage'].value,
      };      
      this.userProfileService.updateUserDetails(userProfile).subscribe({
        next: (data: any) => {
          this.profileImage = this.saveForm.controls['profileImage'].value;
        },
        error: (err: any) => {
          console.log(err);          
          const unsafeImageUrl = URL.createObjectURL(
            this.saveForm.controls['profileImage'].value
          );
          this.profileImage =
            this.sanitizer.bypassSecurityTrustResourceUrl(unsafeImageUrl);
        },
      });
    }
  }

  myUploader(event: any) {
    console.log(event);
    this.saveForm.controls['profileImage'].setValue(event.files[0]);
    console.log(this.saveForm.controls['profileImage'].value);
  }

  onProfileImageClear(event: any) {
    console.log(event);
    this.saveForm.controls['profileImage'].setValue('');
    this.uploadedFilename = '';
    console.log(this.saveForm.controls['profileImage'].value);
    console.log('Cancel Triggered');
  }
}
