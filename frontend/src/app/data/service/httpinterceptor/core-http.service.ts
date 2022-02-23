import {
  HttpErrorResponse,
  HttpHeaders,
  HttpParams,
  HttpClient
} from '@angular/common/http';
import {Inject, Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {Observable, throwError} from 'rxjs';
import {catchError, map} from 'rxjs/operators';
import {RequestOptions} from './core.classes';
import {environment} from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CoreHttpService {
  public errordata: any;

  constructor(private http: HttpClient, private router: Router) {}

  getr = <TResponse>(apiUrl: string): Observable<TResponse | {}> => {
      return this.http.get<TResponse>(apiUrl).pipe(
          map((res) => res),
          catchError(this.handleError)
      );
      // .map(res => res)
      // .catch(this.handleError);
  };

  loginToSystem(reqObject: any) {
      const apiUrl = environment.TOKENENDPOINT;
      this.CheckInternetConnection();
      return this.http
          .post(apiUrl, reqObject, this.getLoginRequestOption())
          .pipe(
              map((res) => res),
              catchError(this.handleError)
          );
      // .map((res) => res)
      // .catch(this.handleError);
  }

  getRequest = <TResponse>(apiUrl: string): Observable<TResponse | {}> => {
      apiUrl = environment.APIENDPOINT + apiUrl;
      this.CheckInternetConnection();
      return this.http.get(apiUrl);
      // .map((res) => res)
      // .catch(this.handleError);
  };

  // tslint:disable-next-line:max-line-length
  getRequestWithParameters = <TParameterObject, TResponse>(
      apiUrl: string,
      parameterObject: TParameterObject
  ): Observable<TResponse | {}> => {
      apiUrl = environment.APIENDPOINT + apiUrl;
      this.CheckInternetConnection();
      let requestParams;
      if (parameterObject != null) {
          requestParams = this.getParametersFromObject(parameterObject);
      }

      const requestOptions = new RequestOptions({params: requestParams});
      return this.http.get<TResponse>(apiUrl, requestOptions);
      // .map((res) => res)
      // .catch(this.handleError);
  };

  postRequest = <TRequest, TResponse>(
      apiUrl: string,
      tRequest: TRequest
  ): Observable<TResponse | {}> => {
      apiUrl = environment.APIENDPOINT + apiUrl;
      this.CheckInternetConnection();
      return this.http
          .post<TResponse>(apiUrl, tRequest, this.getRequestOptionsForFile())
          .pipe(
              map((res) => res),
              catchError(this.handleError)
          );
      // .map((res) => res)
      // .catch(this.handleError);
  };

  putRequest = <TRequest, TResponse>(
      apiUrl: string,
      tRequest: TRequest
  ): Observable<TResponse | {}> => {
      this.CheckInternetConnection();
      return this.http.put<TResponse>(apiUrl, tRequest).pipe(
          map((res) => res),
          catchError(this.handleError)
      );
      // .map((res) => res)
      // .catch(this.handleError);
  };

  deleteRequest = <TResponse>(apiUrl: string): Observable<TResponse | {}> => {
      this.CheckInternetConnection();
      apiUrl = environment.APIENDPOINT + apiUrl;
      return this.http.post<TResponse>(apiUrl, null).pipe(
          map((res) => res),
          catchError(this.handleError)
      );
      // .map((res) => res)
      // .catch(this.handleError);
  };

  postFileRequest = <TRequest, TResponse>(
      apiUrl: string,
      tRequest: TRequest,
      fileMapper: Map<string, File>
  ): Observable<TResponse | {}> => {
      this.CheckInternetConnection();
      const formData: FormData = this.initializeFormData<TRequest, TResponse>(
          fileMapper,
          tRequest
      );
      this.appendToFormData(formData, tRequest);
      return this.http.post<TResponse>(apiUrl, formData).pipe(
          map((res) => res),
          catchError(this.handleError)
      );
      // .map(res => res)
      // .catch(this.handleError);
  };

  putFileRequest = <TRequest, TResponse>(
      apiUrl: string,
      tRequest: TRequest,
      fileMapper: Map<string, File>
  ): Observable<TResponse | {}> => {
      const formData: FormData = this.initializeFormData<TRequest, TResponse>(
          fileMapper,
          tRequest
      );
      // const requestOptions = this.getRequestOptionsForFile();
      return this.http.put<TResponse>(apiUrl, formData).pipe(
          map((res) => res),
          catchError(this.handleError)
      );
      // .map(res => res)
      // .catch(this.handleError);
  };

  private appendToFormData = <TRequest extends {[key: string]: any}>(
      formData: FormData,
      tRequest: TRequest
  ) => {
      for (const property in tRequest) {
          if (!!property && tRequest.hasOwnProperty(property)) {
              formData.append(property, tRequest[property]);
          }
      }
  };

  private CheckInternetConnection() {
      if (!navigator.onLine) {
          //  this.toastr.error('Please check your internet connection');
          // throw Observable.throw('Please check your internet connection');
          throwError('Please check your internet connection');
      } else {
          return;
      }
  }

  private getParametersFromObject = (
      parameterObject: any
  ):
      | HttpParams
      | {
            [param: string]: string | string[];
        } => {
      let requestParams = new HttpParams();
      const properties = Object.keys(parameterObject);

      for (const index in properties) {
          if (properties.hasOwnProperty(index)) {
              const key = properties[index];
              const value = parameterObject.hasOwnProperty(key)
                  ? parameterObject[key]
                  : null;
              if (value === null || value === undefined) {
                  continue;
              }
              requestParams = requestParams.append(key, value);
          }
      }
      return requestParams;
  };

  private getLoginRequestOption() {
      const headers = new HttpHeaders();
      headers.append('Content-Type', 'application/x-www-form-urlencoded');
      const requestOptions = new RequestOptions({headers: headers});
      return requestOptions;
  }

  private getRequestOptionsForFile() {
      const headers = new HttpHeaders();
      headers.append('Content-Type', 'application/x-www-form-urlencoded');
      const requestOptions = new RequestOptions({headers: headers});
      return requestOptions;
  }

  private initializeFormData<TRequest, TResponse>(
      fileMapper: Map<string, File>,
      tRequest: TRequest
  ) {
      const formData: FormData = new FormData();
      fileMapper.forEach((value, key) => {
          formData.append(key, value, value.name);
      });
      this.appendToFormData(formData, tRequest);
      return formData;
  }

  private handleError(error: HttpErrorResponse) {
      // Todo -> Send the error to remote logging infrastructure
      return throwError(error);
  }
}
