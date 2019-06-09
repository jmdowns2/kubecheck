import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class KubecheckService {

  constructor(private http: HttpClient) { }

  getServices() {
    var url = `http://localhost:8080/services`;
    return this.http.get<Resource[]>(url);
  }

  getServiceChecks() {
    var url = `http://localhost:8080/services/checks`;
    return this.http.get<String[]>(url);
  }

  getPodChecks() {
    var url = `http://localhost:8080/pods/checks`;
    return this.http.get<String[]>(url);
  }

  check(r:Resource, c:String)
  {
    var url = `http://localhost:8080/${r.type.toLowerCase()}s/${r.namespace}/${r.name}/check/${c}`;
    return this.http.get<String>(url, {});
  }
}
