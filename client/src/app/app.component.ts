import { Component, OnInit } from '@angular/core';
import { KubecheckService } from "./services/kubecheck.service";
import { build$ } from 'protractor/built/element';
import { BoundText } from '@angular/compiler/src/render3/r3_ast';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent  {
  title = 'app';

  selected:Resource = null;

  constructor(private service:KubecheckService) {}

  onSelection(res:Resource) {
    this.selected = res;
  }


}
