import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { UserProfile } from '../../schema/user-profile';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

const httpOption = {
  headers: new HttpHeaders({
    'Access-Control-Allow-Origin' : 'http://localhost:4200',
  })
}

@Injectable({
  providedIn: 'root'
})
export class UserProfileService {
  apiEndPoint: string = 'api/v1/wanderer/user/';

  constructor(private http: HttpClient) { }

  getUserDetails(): Observable<any> {
    return this.http.get(this.apiEndPoint + 'getDetails', httpOption);
  }

  updateUserDetails(userProfile: UserProfile): Observable<any>{
    let formData: FormData = new FormData();
    formData.append('firstName', userProfile.firstName);
    formData.append('lastName', userProfile.lastName);
    formData.append('image',userProfile.profileImage);
    // formData.append('profileImage', userProfile.profileImage);

    return this.http.put(this.apiEndPoint + 'updateProfile', formData);
  }
}
