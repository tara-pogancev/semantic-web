import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class QueryService {
  private baseUrl = 'http://localhost:8080/';

  constructor(private _http: HttpClient) {}

  runQuery(query: String) {
    return this._http.post(`${this.baseUrl}chat`, query, {
      responseType: 'text',
    });
  }
}
