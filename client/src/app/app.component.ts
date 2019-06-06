import { Component, OnInit } from '@angular/core';
import { KubecheckService } from "./services/kubecheck.service";
import { build$ } from 'protractor/built/element';
declare var vis:any;

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'app';

  constructor(private service:KubecheckService) {}

  
  ngOnInit() {

    this.service.getServices().subscribe((res) => {
      this.build(res);
    });
  }

  build(res:any) {

    var index = 1;

    /*
    var nodes = [
      {id: 1, label: 'Node 1'},
      {id: 2, label: 'Node 2'},
      {id: 3, label: 'Node 3'},
      {id: 4, label: 'Node 4'},
      {id: 5, label: 'Node 5'}
    ];

    // create an array with edges
    var edges = [
      {from: 1, to: 3},
      {from: 1, to: 2},
      {from: 2, to: 4},
      {from: 2, to: 5},
      {from: 3, to: 3}
    ];
*/
    var nodes = [];
    var edges = [];

    res.map((service) => {

      var serviceId = index++;
      nodes.push({ id: serviceId, label: service.name });
      if(service.children)
      {
        service.children.map((pod) => {
          var podId =index++;
          nodes.push({id: podId, label: pod.name });
          edges.push({ from: serviceId, to: podId});
        });
      }
    });

  
      // create a network
      var container = document.getElementById('testing');
      var data = {
        nodes: new vis.DataSet(nodes),
        edges: new vis.DataSet(edges)
      };
      var options = {};
      var network = new vis.Network(container, data, options);
  
  }


}
