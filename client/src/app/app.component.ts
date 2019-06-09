import { Component, OnInit } from '@angular/core';
import { KubecheckService } from "./services/kubecheck.service";
import { build$ } from 'protractor/built/element';
import { BoundText } from '@angular/compiler/src/render3/r3_ast';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'app';

  selected:Resource = null;
  selectedChecks:String[] = [];
  checkResult:String = "";

  podChecks:String[] = [];
  serviceChecks:String[] = [];


  constructor(private service:KubecheckService) {}

  ngOnInit() {
    this.service.getPodChecks().subscribe((res) => { 
      this.podChecks = res;
    });
    this.service.getServiceChecks().subscribe((res) => { 
      this.serviceChecks = res;
    });
  }

  onSelection(res:Resource) {
    this.checkResult = "";
    this.selected = res;
    this.selectedChecks = this.getChecksForSelection();
  }

  getChecksForSelection() {
    if(!this.selected) return [];
    switch(this.selected.type)
    {
      case "Pod":
        return this.podChecks;
      case "Service": 
        return this.serviceChecks;
      default:
        console.error("Not implemented "+this.selected.type);
        return [];
    }
  }

  doCheck(check:String)
  {
    this.checkResult = "";
    this.service.check(this.selected, check).subscribe((res) => {
      this.checkResult = JSON.stringify(res);
    });
  }


}
