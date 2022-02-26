import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { UserProfile } from '../../schema/user-profile';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserProfileService {
  apiControllerName: string = 'users';

  constructor(private http: HttpClient) { }

  getUserDetails(): Observable<any> {
    return this.http.get('')
  }

  updateUserDetails(userProfile: UserProfile): Observable<any>{
    let formData: FormData = new FormData();
    formData.append('firstName', userProfile.firstName);
    formData.append('profileImage', userProfile.profileImage);

    return this.http.post(environment.APIENDPOINT + this.apiControllerName, formData);
  }
}
