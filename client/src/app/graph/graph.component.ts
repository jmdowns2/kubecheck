import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { KubecheckService } from "../services/kubecheck.service";

declare var vis:any;


@Component({
  selector: 'graph',
  templateUrl: './graph.component.html',
  styleUrls: ['./graph.component.css']
})
export class GraphComponent implements OnInit {

  nodes = [];

  @Output() selected = new EventEmitter<Resource>();


  constructor(private service:KubecheckService) {}

  ngOnInit() {

    this.service.getServices().subscribe((res) => {
      this.build(res);
    });
  }

  build(res:Resource[]) {

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
    var colors = ["#1255B3", "#66A4FA", "#3287FF", "#B37600", "#FFB933"];

    var edges = [];
    var margin = { top: 40, right: 30, bottom: 40, left: 30 };
  
    res.map((service) => {

      var serviceId = index++;
      this.nodes.push({ id: serviceId, value: service, font: { multi: true }, label: "<b>"+service.type+"</b>\n"+service.name, color: colors[1], margin: margin });
      if(service.children)
      {
        service.children.map((pod) => {
          var podId =index++;
          this.nodes.push({id: podId, value: pod, font: { multi: true }, label: "<b>"+pod.type+"</b>\n"+pod.name, color: colors[4], margin: margin });
          edges.push({ from: serviceId, to: podId});
        });
      }
    });

  
    // create a network
    var container = document.getElementById('testing');
    var data = {
      nodes: new vis.DataSet(this.nodes),
      edges: new vis.DataSet(edges)
    };
    var options = {
      nodes: {
        borderWidth: 2,
        shadow:true
      },
      edges: {
          width: 2,
          shadow:true
      },
/*
      layout: {
          hierarchical: {
              direction: "UD"
          }
      }
      */
    };
    var network = new vis.Network(container, data, options);
  
    network.on("click", (params) => {
      this.onClick(params);
    }); 

  }

  onClick(params) {
    var id = params.nodes[0];
    var node = this.nodes.filter( (v)=> v.id == id)[0];
    this.selected.emit(node.value);
  }

}
