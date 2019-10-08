import { Component, OnInit } from '@angular/core';
import {Title} from '@angular/platform-browser';

@Component({
  selector: 'app-four-ho-four',
  templateUrl: './four-ho-four.component.html',
  styleUrls: ['./four-ho-four.component.css']
})
export class FourHoFourComponent implements OnInit {

  constructor(public titleService: Title) { }

  ngOnInit() {
    this.titleService.setTitle('Page introuvable');
  }

}
