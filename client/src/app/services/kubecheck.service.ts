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
    return this.http.get<Check[]>(url);
  }

  getPodChecks() {
    var url = `http://localhost:8080/pods/checks`;
    return this.http.get<Check[]>(url);
  }

  check(r:Resource, c:Check, options:Array<any>)
  {
    var o:string = options ? options.map((v) => {
      return `${v.name}=${v.value}`
    }).join("&") : "";

    var url = `http://localhost:8080/${r.type.toLowerCase()}s/${r.namespace}/${r.name}/check/${c.name}?${o}`;
    return this.http.get<String>(url, {});
  }
}
