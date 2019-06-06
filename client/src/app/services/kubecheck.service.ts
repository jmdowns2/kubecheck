import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class KubecheckService {

  constructor(private http: HttpClient) { }

  getServices() {
    var url = `http://localhost:8080/services`;
    return this.http.get<any>(url);
  }
}
