import { HttpHeaders, HttpParams } from '@angular/common/http';

export class RequestOptions {
    headers?: HttpHeaders | {
        [header: string]: string | string[];
    };
    params?: HttpParams | {
        [param: string]: string | string[];
    };
    reportProgress?: boolean;
    responseType?: 'json';
    withCredentials?: boolean;
    constructor(initializer: Partial<RequestOptions>) {
        if (!!initializer) { Object.assign(this, initializer); }
    }
}